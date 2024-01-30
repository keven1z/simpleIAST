package com.keven1z.core.hook.transforms;

import com.keven1z.core.EngineController;
import com.keven1z.core.hook.asm.IASTClassVisitor;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.utils.AsmUtils;
import com.keven1z.core.utils.ClassUtils;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.TransformerProtector;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.keven1z.core.Config.IS_DUMP_CLASS;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;


/**
 * 自定义Transformer，用于修改被hook的class
 */
public class HookTransformer implements ClassFileTransformer {
    private final PolicyContainer policy;
    private final Instrumentation instrumentation;
    private int transformCount = 0;
    /**
     * 设置最大transformer数量防止意外情况，导致出现不可预知的问题
     */
    private final static int MAX_TRANSFORM_COUNT = 1000;
    private final Logger hookLogger = Logger.getLogger("hook.info");
    private final Logger logger = Logger.getLogger(HookTransformer.class);
    private final Set<String> transformClasses;

    public HookTransformer(PolicyContainer policyContainer, Instrumentation instrumentation) {
        this.policy = policyContainer;
        this.instrumentation = instrumentation;
        this.instrumentation.addTransformer(this, true);
        this.transformClasses = new HashSet<>();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        //如果transformer数量大于阈值，不进行transform，发出警告
        if (transformCount > MAX_TRANSFORM_COUNT) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.TRANSFORM_ERROR, "TransformCount exceeded the threshold.current transformCount is " + transformCount + ",class:" + className);
            }
            return classfileBuffer;
        }

        //若className为空，不进行任何操作，直接返回
        if (null == className) {
            return classfileBuffer;
        }

        if (ClassUtils.isComeFromIASTFamily(className, loader) || className.startsWith("org/objectweb") || EngineController.context.isInBlackList(className)) {
            return classfileBuffer;
        }
        if (className.contains("proxy")) {
            return classfileBuffer;
        }
        try {
            return doTransform(loader, className, classBeingRedefined, classfileBuffer);
        } catch (Throwable throwable) {
            //由于可能出现的错误日志较多的情况，默认debug模式才打开
            if (LogTool.isDebugEnabled()) {
                LogTool.error(ErrorType.TRANSFORM_ERROR, "DoTransform " + className + " error", throwable);
            }
            return classfileBuffer;
        }

    }

    private byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, byte[] classfileBuffer) throws IOException {
        if (classBeingRedefined != null) {
            String name = classBeingRedefined.getName().replace(".", "/");
            if (!name.equals(className)) {
                return classfileBuffer;
            }
        }

        //判断该类为ReTransform类
//        if (!isReTransform) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        //不hook接口类
        if (ClassUtils.isInterface(classReader.getAccess())) {
            return classfileBuffer;
        }
        //判断是否在hook点策略中
        if (!PolicyUtils.isHook(className, policy, classReader.getInterfaces(), classReader.getSuperName(), loader)) {
//            hookLogger.info(className);
            return classfileBuffer;
        }
//        }
        //transform class +1
        ++transformCount;

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
        classReader.accept(new IASTClassVisitor(classWriter, className), ClassReader.EXPAND_FRAMES);

        this.transformClasses.add(className);
        return dumpClassIfNecessary(className, classWriter.toByteArray());
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
//            logger.info("dump {} to {} success.", className, dumpClassFile);
        } catch (IOException e) {
//            logger.warn("dump {} to {} failed.", className, dumpClassFile, e);
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

                if (PolicyUtils.isHook(this.policy, clazz)) {
                    // hook已经加载的类，或者是回滚已经加载的类
                    instrumentation.retransformClasses(clazz);
                }

            } catch (Throwable t) {
                LogTool.error(ErrorType.TRANSFORM_ERROR, "Failed to reTransform class " + clazz.getName() + ": " + t.getMessage(), t);
            }
        }
    }

    /**
     * 查找已transform的hook点
     */
    public List<Class<?>> findForReTransform() {
        final List<Class<?>> classes = new ArrayList<>();
        TransformerProtector.instance.enterProtecting();

        Class<?>[] loadedClasses = this.instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : loadedClasses) {
            try {
                String normalizeClass = ClassUtils.normalizeClass(clazz.getName());
                if (ClassUtils.isComeFromIASTFamily(normalizeClass, clazz.getClassLoader())) {
                    continue;
                }
                if (!instrumentation.isModifiableClass(clazz)) {
                    continue;
                }
                if (clazz.getName().startsWith("java.lang.invoke.LambdaForm")) {
                    continue;
                }
                if (this.transformClasses.contains(normalizeClass)) {
                    if (LogTool.isDebugEnabled()) {
                        logger.warn("Class has been transformed,class name is" + clazz.getName());
                    }
                    continue;
                }
                if (EngineController.context.isInBlackList(normalizeClass)) {
                    continue;
                }
                if (PolicyUtils.isHook(this.policy, clazz)) {
                    classes.add(clazz);
                }
            } catch (Throwable cause) {
                logger.debug("remove from findForReTransform, because loading class:" + clazz.getName() + "occur an exception");
            } finally {
                TransformerProtector.instance.exitProtecting();
            }
        }
        return classes;
    }
}
