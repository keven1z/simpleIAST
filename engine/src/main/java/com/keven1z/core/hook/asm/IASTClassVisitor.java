package com.keven1z.core.hook.asm;

import com.keven1z.core.hook.asm.adapter.*;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.MethodHookConfig;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.objectweb.asm.commons.Method;
import java.util.ArrayList;
import java.util.List;
import static com.keven1z.core.utils.CommonUtils.toInternalClassName;
import static org.objectweb.asm.Opcodes.*;

/**
 * 自定义的ClassVisitor，用于访问类的方法
 */
public class IASTClassVisitor extends ClassVisitor {
    protected final String className;
    //监控进入hook点，但未成功匹配方法描述，弹出警告告知method或者desc错误
    private boolean isVisitMethod;
    protected final Logger logger = Logger.getLogger(this.getClass());
    /**
     * 策略名称:http body 读取标志hook点
     */
    private final static String HTTP_BODY = "http_body";
    /**
     * 策略名称:http 入口点
     */
    private final static String HTTP_ENTER = "http_enter";
    /**
     * 策略名称:http body读取hook点
     */
    private final static String HTTP_BODY_READ = "http_body_read";
    private final String nativePrefix;
    private List<ProxyMethod> proxyNativeAsmMethods;

    public IASTClassVisitor(ClassVisitor classVisitor,
                            String className,
                            String nativePrefix) {
        super(Opcodes.ASM9, classVisitor);
        this.className = className;
        this.nativePrefix = nativePrefix;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.proxyNativeAsmMethods = new ArrayList<>();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (!IastHookManager.getManager().shouldHookMethod(this.className, name, descriptor)){
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        if (!isNative(access)) {
            return rewriteNormalMethod(access, name, descriptor, signature, exceptions);
        } else {
            return rewriteNativeMethod(access, name, descriptor, signature, exceptions);
        }
    }

    /**
     * 重写普通方法
     *
     * @param access 方法的访问权限修饰符
     * @param name   方法名
     * @param descriptor 方法描述符
     * @param signature 方法的泛型签名
     * @param exceptions 方法抛出的异常类型数组
     * @return 改写后的方法访问器
     */
    private MethodVisitor rewriteNormalMethod(final int access,
                                              final String name,
                                              final String descriptor,
                                              final String signature,
                                              final String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        methodVisitor = new JSRInlinerAdapter(methodVisitor, access, name, descriptor, signature, exceptions);
        MethodHookConfig methodHookConfig = IastHookManager.getManager().getHookMethod(className, name, descriptor);
        MethodHookConfig.TaintTracking taintTracking = methodHookConfig.getTaintTracking();
        if (taintTracking.isSource() || taintTracking.isSink() || taintTracking.isPropagator()) {
            isVisitMethod = true;   //标记已经访问过方法
            methodVisitor = visitTaintMethod(access, name, descriptor, methodVisitor, methodHookConfig);
        }
        if (taintTracking.isHttp()){
            isVisitMethod = true;
            methodVisitor = visitHTTPMethod(access, name, descriptor, methodVisitor, methodHookConfig);
        }
//        HookPolicy hookPolicy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getSingles());
//        if (hookPolicy != null) {
//            isVisitMethod = true;
//            //打印错误日志
//            return visitNormalMethod(access, name, descriptor, methodVisitor, hookPolicy);
//        }
//
//        hookPolicy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getHttp());
//        if (hookPolicy != null) {
//            isVisitMethod = true;
//            //打印错误日志
//            methodVisitor = visitHTTPMethod(access, name, descriptor, methodVisitor, hookPolicy);
//        }
//
//        hookPolicy = PolicyUtils.getHookedPolicyByBisection(className, name, descriptor, EngineController.context.getPolicy().getTaintPolicy());
//        if (hookPolicy != null) {
//            isVisitMethod = true;
//            //打印错误日志
//            return visitTaintMethod(access, name, descriptor, methodVisitor, hookPolicy);
//        }
        return methodVisitor;
    }

    /**
     * 重写原生方法，将原生方法的实现替换为代理方法
     *
     * @param access 方法的访问权限修饰符
     * @param name 方法名
     * @param descriptor 方法描述符
     * @param signature 方法的泛型签名
     * @param exceptions 方法抛出的异常类型数组
     * @return 改写后的方法访问器
     */
    private MethodVisitor rewriteNativeMethod(final int access,
                                              final String name,
                                              final String descriptor,
                                              final String signature,
                                              final String[] exceptions) {
        MethodHookConfig methodHookConfig = IastHookManager.getManager().getHookMethod(className, name, descriptor);
        //去掉native
        int newAccess = access & ~ACC_NATIVE;
        final MethodVisitor mv = super.visitMethod(newAccess, name, descriptor, signature, exceptions);
        return new IASTAdviceAdapter(api, new JSRInlinerAdapter(mv, newAccess, name,
                descriptor, signature, exceptions), newAccess, className, name, descriptor, methodHookConfig) {
            @Override
            public void visitEnd() {
                if (!name.startsWith(nativePrefix)) {
                    if (this.methodHookConfig.getHookPositions().isEntry()) {
//                        inject(-1, true);
                    }
                    final String proxyMethodName = nativePrefix + name;
                    final ProxyMethod proxyMethod = new ProxyMethod(access, proxyMethodName, desc);
                    final String owner = toInternalClassName(className);
                    if (!isStaticMethod()) {
                        loadThis();
                    }
                    storeArgArray();
                    if (isStaticMethod()) {
                        this.visitMethodInsn(Opcodes.INVOKESTATIC, owner, proxyMethod.getName(), proxyMethod.getDescriptor(), false);
                    } else {
                        //wrapper的方法永远都是private
                        this.visitMethodInsn(Opcodes.INVOKESPECIAL, owner, proxyMethod.getName(), proxyMethod.getDescriptor(), false);
                    }

                    proxyNativeAsmMethods.add(proxyMethod);
                    if (this.methodHookConfig.getHookPositions().isExit()) {
//                        inject(getReturnType().getOpcode(IRETURN), false);
                    }
                    returnValue();
                }
                super.visitEnd();
            }
        };
    }

    /**
     * 访问HTTP方法并返回MethodVisitor对象
     *
     * @param access 方法的访问修饰符
     * @param name 方法名
     * @param descriptor 方法的描述符
     * @param jsrInlinerAdapter 方法的MethodVisitor适配器
     * @param methodHookConfig hook点配置
     * @return 返回适配后的MethodVisitor对象，如果策略不是HTTP_CIRCLE、HTTP_BODY或HTTP_BODY_READ则直接返回传入的jsrInlinerAdapter
     */
    public MethodVisitor visitHTTPMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, MethodHookConfig methodHookConfig) {
        String httpStage = methodHookConfig.getTaintTracking().getHttpStage();
        if (HTTP_ENTER.equals(httpStage)) {
            return new HttpAdviceAdapter(api, jsrInlinerAdapter, access,this.className, name, descriptor, methodHookConfig);
        } else if (HTTP_BODY.equals(httpStage)) {
            return new HttpBodyAdviceAdapter(api, jsrInlinerAdapter, access, this.className,name, descriptor,methodHookConfig);
        } else if (HTTP_BODY_READ.equals(httpStage)) {
            return new HttpBodyReadAdviceAdapter(api, jsrInlinerAdapter, access,this.className, name, descriptor,methodHookConfig);
        }
        return jsrInlinerAdapter;
    }

    /**
     * 访问污点方法
     */
    public MethodVisitor visitTaintMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, MethodHookConfig methodHookConfig) {

        return new TaintAdviceAdapter(Opcodes.ASM9, jsrInlinerAdapter, access, className, name, descriptor, methodHookConfig);
    }

    /**
     * 访问普通hook点
     */
    public MethodVisitor visitNormalMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, MethodHookConfig methodHookConfig) {

        return new SingleAdviceAdapter(Opcodes.ASM9, jsrInlinerAdapter, access, className, name, descriptor, methodHookConfig);
    }

    /**
     * 是否native方法
     */
    //
    private boolean isNative(final int access) {
        return (access & ACC_NATIVE) != 0;
    }

    @Override
    public void visitEnd() {
        proxyNativeAsmMethods.forEach(method -> {
            final boolean isStatic = (ACC_STATIC & method.access) != 0;
            final int access = ACC_PRIVATE | ACC_NATIVE | ACC_FINAL | (isStatic ? ACC_STATIC : 0);
            cv.visitMethod(access, method.getName(), method.getDescriptor(), null, null)
                    .visitEnd();
        });
        super.visitEnd();

        if (!isVisitMethod) {
            if (LogTool.isDebugEnabled()) {
                logger.info("Failed to visit method,className:" + this.className + ",Please check method or desc in policy.json");
            }
        }
    }

    private static class ProxyMethod extends Method {
        private final int access;

        public ProxyMethod(int access, String name, String descriptor) {
            super(name, descriptor);
            this.access = access;
        }
    }
}
