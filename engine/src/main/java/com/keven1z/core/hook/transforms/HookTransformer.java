package com.keven1z.core.hook.transforms;

import com.keven1z.core.EngineController;
import com.keven1z.core.hook.asm.BeanDetectorVisitor;
import com.keven1z.core.hook.asm.HardcodedClassVisitor;
import com.keven1z.core.hook.asm.IASTClassVisitor;
import com.keven1z.core.hook.server.detectors.*;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.utils.*;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.keven1z.core.model.Config.IS_DUMP_CLASS;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;


/**
 * 自定义Transformer，用于修改被hook的class
 */
public class HookTransformer implements ClassFileTransformer {
    private final Instrumentation instrumentation;
    /**
     * transform class计数
     */
    private int transformCount = 0;
    /**
     * 设置最大transformer数量防止意外情况，导致出现不可预知的问题
     */
    private static final int MAX_TRANSFORM_COUNT = 1000;
    private final Logger logger = Logger.getLogger(HookTransformer.class);
    private final Set<String> hasTransformedClasses;
    private final String nativePrefix;
    /**
     * native代理方法的前缀
     */
    public static final String SANDBOX_SPECIAL_PREFIX = "$$SIMPLE$$";

    public HookTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.instrumentation.addTransformer(this, true);
        this.hasTransformedClasses = new HashSet<>();
        this.nativePrefix = SANDBOX_SPECIAL_PREFIX;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        /* 若className为空，不进行任何操作，直接返回 */
        if (null == className) {
            return classfileBuffer;
        }

        /* 如果transformer数量大于阈值，不进行transform，发出警告 */
        if (isTransformCountExceeded(transformCount, className)) {
            return classfileBuffer;
        }
        /* 判断是否已经进行transform,如已进行,记录警告日志 */
        if (hasTransformedClasses.contains(className)) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.TRANSFORM_WARN, String.format("%s has already been instrumented, instrumenting it again with ClassLoader=%s", className, loader.getClass().getName()));
            }
        }

        // 检查是否是特殊类需要跳过
        if (shouldSkipSpecialClass(className, loader, classBeingRedefined)) {
            return classfileBuffer;
        }

        TransformerProtector.instance.enterProtecting();
        try {
            return doTransform(loader, className, protectionDomain, classfileBuffer);
        } catch (Throwable throwable) {
            LogTool.error(ErrorType.TRANSFORM_ERROR, "DoTransform " + className + " error", throwable);
            return classfileBuffer;
        } finally {
            TransformerProtector.instance.exitProtecting();
        }

    }

    private byte[] doTransform(ClassLoader loader, String className, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IOException {
        // 识别server类型
        identifyServerType(loader, className, protectionDomain);

        // 加载class
        ClassReader classReader = new ClassReader(classfileBuffer);
        /* 不hook接口类 */
        if (ClassUtils.isInterface(classReader.getAccess())) {
            return classfileBuffer;
        }
        hardcodedCheckBeforeHook(classReader, className);
        /* 确定hook的className,若该类的接口是hook点,hookClassName和className不一致
        * 1. 如果hook一个接口以及接口的实现类,则仅会hook接口的实现类的方法,不会hook接口的方法
        * 2. 如果用户类实现一个接口类,该接口类同时为hook点,则仅会作为接口类的实现类进行hook,不会作为user class进行hook
        * */
        String hookClassName = null;
        boolean isUserClass = false;
        boolean isUserBeanClass = false;
        if (IastHookManager.getManager().shouldHookClass(className)) {
            hookClassName = className;
        } else {
            // 判断是否需要hook祖先类
            Set<String> ancestors = ClassUtils.getAncestors(classReader.getInterfaces(), classReader.getSuperName(), loader);
            if (!ancestors.isEmpty() && IastHookManager.getManager().shouldHookAncestors(className, ancestors)) {
                hookClassName = IastHookManager.getManager().getMatchingAncestor(ancestors);
            }
        }
        if (hookClassName == null && isUserDefinedClass(loader, protectionDomain)) {
            /*  如果未hook到且为用户类,进一步进行用户类的字节码操作*/
            hookClassName = className;
            logUserDefinedClass(className);
            isUserClass = true;
            isUserBeanClass = isUserBeanClass(classfileBuffer);
        }

        if (hookClassName == null) {
            return classfileBuffer;
        }
        /* transform class +1 */
        ++transformCount;

        // 创建classWriter
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
            /*
             * 注意，为了自动计算帧的大小，有时必须计算两个类共同的父类。
             * 缺省情况下，ClassWriter将会在getCommonSuperClass方法中计算这些，通过在加载这两个类进入虚拟机时，使用反射API来计算。
             * 但是，如果你将要生成的几个类相互之间引用，这将会带来问题，因为引用的类可能还不存在。
             * 在这种情况下，你可以重写getCommonSuperClass方法来解决这个问题。
             *
             * 通过重写 getCommonSuperClass() 方法，更正获取ClassLoader的方式，改成使用指定ClassLoader的方式进行。
             * 规避了原有代码采用Object.class.getClassLoader()的方式
             */
            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                return AsmUtils.getCommonSuperClass(type1, type2, loader);
            }
        };

        //通过自定义classVisitor进行类的改造
        classReader.accept(new IASTClassVisitor(classWriter,
                        hookClassName,
                        this.nativePrefix
                        , isUserClass,
                        isUserBeanClass),
                ClassReader.EXPAND_FRAMES
        );
        // 记录已转换的类名
        this.hasTransformedClasses.add(className);
        // 如果需要，则导出类文件
        return dumpClassIfNecessary(className, classWriter.toByteArray());
    }

    private boolean isTransformCountExceeded(int transformCount, String className) {
        if (transformCount > MAX_TRANSFORM_COUNT) {
            if (LogTool.isDebugEnabled()) {
                LogTool.error(ErrorType.TRANSFORM_ERROR,
                        "TransformCount exceeded the threshold.current transformCount is " +
                                transformCount + ",class:" + className);
            }
            return true;
        }
        return false;
    }

    private boolean shouldSkipSpecialClass(String className, ClassLoader loader, Class<?> classBeingRedefined) {
        if (classBeingRedefined == null) {
            // 来自IAST家族的类、特定包下的类或在黑名单中的类
            if (ClassUtils.isComeFromIASTFamily(className, loader) ||
                    className.startsWith("org/objectweb") ||
                    EngineController.context.isClassNameBlacklisted(className)) {
                return true;
            }

            // 需要跳过的代理类
            return ClassUtils.shouldSkipProxyClass(className);
        }
        return false;
    }

    private static byte[] dumpClassIfNecessary(String className, byte[] data) {
        if (!IS_DUMP_CLASS) {
            return data;
        }
        final File dumpClassFile = new File("./iast-class-dump/" + className + ".class");
        final File classPath = new File(dumpClassFile.getParent());

        // 创建类所在的包路径
        if (!classPath.mkdirs() && !classPath.exists()) {
            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(HookTransformer.class).warn("create dump classpath=" + classPath + "failed.");
            }
            return data;
        }

        // 将类字节码写入文件
        try {
            writeByteArrayToFile(dumpClassFile, data);
        } catch (IOException ignored) {
        }

        return data;
    }

    /**
     * hook已经加载的类，或者是回滚已经加载的类
     */
    public void reTransform() {
        List<Class<?>> loadedClasses = findForReTransform();
        for (Class<?> clazz : loadedClasses) {
            try {
                instrumentation.retransformClasses(clazz);
            } catch (UnmodifiableClassException e) {
                // 处理类无法被修改的情况
                logger.warn("Class " + clazz.getName() + " cannot be reTransformed because it is unmodifiable.");
            } catch (Exception t) {
                logger.error("Failed to reTransform class " + clazz.getName() + ": " + t.getMessage(), t);
            }
        }
    }

    /**
     * 查找已加载到内存中hook点
     *
     * @return 可重新转换的类列表
     */
    private List<Class<?>> findForReTransform() {
        final List<Class<?>> classes = new ArrayList<>();
        Class<?>[] loadedClasses = this.instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : loadedClasses) {
            TransformerProtector.instance.enterProtecting();
            try {
                String className = clazz.getName();
                String normalizeClass = ClassUtils.normalizeClass(className);

                if (ClassUtils.isComeFromIASTFamily(normalizeClass, clazz.getClassLoader())
                        || !instrumentation.isModifiableClass(clazz)
                        || clazz.getName().startsWith("java.lang.invoke.LambdaForm")
                        || EngineController.context.isClassNameBlacklisted(normalizeClass)) {
                    continue;
                }

                if (this.hasTransformedClasses.contains(normalizeClass)) {
                    if (LogTool.isDebugEnabled()) {
                        logger.debug("Class has been transformed, class name: " + className);
                    }
                    continue;
                }

                if (ClassUtils.shouldSkipProxyClass(normalizeClass)) {
                    if (LogTool.isDebugEnabled()) {
                        logger.debug("Class is proxy, skip proxy class, class name: " + className);
                    }
                    continue;
                }
                String internalClassName = CommonUtils.toInternalClassName(className);
                if (IastHookManager.getManager().shouldHookClass(internalClassName)) {
                    classes.add(clazz);
                } else {
                    Set<String> ancestors = ClassUtils.getAncestors(clazz.getInterfaces(), clazz.getSuperclass(), clazz.getClassLoader());
                    if (IastHookManager.getManager().shouldHookAncestors(internalClassName, ancestors)) {
                        classes.add(clazz);
                    }
                }
            } catch (RuntimeException | IOException e) {
                logger.error("remove from findForReTransform, because loading class: " + clazz.getName() + " occur an exception");
            } catch (Exception ex) {
                logger.error("unknown error from findForReTransform, because loading class: " + clazz.getName() + " occur an exception");
            } finally {
                TransformerProtector.instance.exitProtecting();
            }
        }
        return classes;
    }

    public String getNativePrefix() {
        return nativePrefix;
    }

    private static final List<ServerDetector> SERVER_HOOKS = new ArrayList<>();

    static {
        SERVER_HOOKS.add(new TomcatDetector());
        SERVER_HOOKS.add(new SpringbootDetector());
        SERVER_HOOKS.add(new JettyDetector());
        SERVER_HOOKS.add(new WebLogicDetector());
        SERVER_HOOKS.add(new GlassfishDetector());
        SERVER_HOOKS.add(new WildFlyDetector());
        SERVER_HOOKS.add(new ResinDetector());
        SERVER_HOOKS.add(new TongWebDetector());
        SERVER_HOOKS.add(new UndertowDetector());
    }

    private void identifyServerType(ClassLoader loader, String className, ProtectionDomain protectionDomain) {
        // 已经识别到ServerType，不再重复检测
        if (!CommonUtils.isEmpty(ApplicationModel.getContainerName())) {
            return;
        }
        for (final ServerDetector hook : SERVER_HOOKS) {
            if (hook.isClassMatched(className)) {
                try {
                    boolean detectionSuccessful = hook.processServerInfo(loader, protectionDomain);
                    if (detectionSuccessful) {
                        String serverType = ApplicationModel.getContainerName();
                        logger.info("Detect server successfully, server type: " + serverType);
                        return;
                    } else {
                        LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Failed to detect server type by " + hook);
                    }
                } catch (Exception e) {
                    LogTool.error(ErrorType.UNEXPECTED_ERROR, "Exception occurred while detecting server type", e);
                }
            }
        }
    }

    private void hardcodedCheckBeforeHook(ClassReader classReader, String className) {
        HardcodedClassVisitor classVisitor = new HardcodedClassVisitor(className);
        // ClassReader.SKIP_CODE: 跳过代码块，只解析class文件结构
        //此处仅仅访问属性值，所以跳过方法体，且不进行扩展帧操作
        classReader.accept(classVisitor, ClassReader.SKIP_CODE);
    }

    private static final String[] USER_CLASS_LOCATIONS = {
            "BOOT-INF/classes",
            "WEB-INF/classes",
            "target/classes",
            "APP-INF/classes"
    };

    /**
     * 检测用户定义的类
     */
    private boolean isUserDefinedClass(ClassLoader loader, ProtectionDomain protectionDomain) {
        if (loader == null || protectionDomain == null) {
            return false;
        }
        String loaderName = loader.getClass().getName();
        if (loaderName.startsWith("jdk.internal.") ||  // JDK 内部加载器
                loaderName.equals("sun.misc.Launcher$ExtClassLoader") || // 扩展类加载器
                loaderName.startsWith("org.apache.")) { // 某些容器级加载器（如 Tomcat 的共享库加载器）)
            return false;
        }
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null || codeSource.getLocation() == null) {
            return false;
        }
        String codeLocation = codeSource.getLocation().toString();
        for (String location : USER_CLASS_LOCATIONS) {
            if (codeLocation.contains(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 记录用户定义的类
     */
    private void logUserDefinedClass(String className) {
        EngineController.context.addUserClass(className);
    }

    private boolean isUserBeanClass(byte[] classFileBuffer) {
        ClassReader cr = new ClassReader(classFileBuffer);
        BeanDetectorVisitor detector = new BeanDetectorVisitor();
        cr.accept(detector, ClassReader.SKIP_CODE);
        return detector.isBean();
    }
}
