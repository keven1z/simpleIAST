package com.keven1z.core.hook.asm;

import com.keven1z.core.EngineController;
import com.keven1z.core.hook.asm.adapter.*;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.PolicyUtils;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;
import java.util.List;

import static com.keven1z.core.consts.CommonConst.ON;
import static com.keven1z.core.utils.StringUtils.toInternalClassName;
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
     * 策略名称:http 生命周期提取hook点
     */
    private final static String HTTP_CIRCLE = "http_circle";
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
        //如果是本地方法，不进行hook
        if (!isNative(access)) {
            return rewriteNormalMethod(access, name, descriptor, signature, exceptions);
        } else {
            return rewriteNativeMethod(access, name, descriptor, signature, exceptions);
        }
    }

    private MethodVisitor rewriteNormalMethod(final int access,
                                              final String name,
                                              final String descriptor,
                                              final String signature,
                                              final String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

        methodVisitor = new JSRInlinerAdapter(methodVisitor, access, name, descriptor, signature, exceptions);

        Policy policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getSingles());
        if (policy != null) {
            isVisitMethod = true;
            //打印错误日志
            return visitNormalMethod(access, name, descriptor, methodVisitor, policy);
        }

        policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getHttp());
        if (policy != null) {
            isVisitMethod = true;
            //打印错误日志
            methodVisitor = visitHTTPMethod(access, name, descriptor, methodVisitor, policy);
        }

        policy = PolicyUtils.getHookedPolicyByBisection(className, name, descriptor, EngineController.context.getPolicy().getTaintPolicy());
        if (policy != null) {
            isVisitMethod = true;
            //打印错误日志
            return visitTaintMethod(access, name, descriptor, methodVisitor, policy);
        }
        return methodVisitor;
    }

    private MethodVisitor rewriteNativeMethod(final int access,
                                              final String name,
                                              final String descriptor,
                                              final String signature,
                                              final String[] exceptions) {
        Policy policy = PolicyUtils.getHookedPolicyByBisection(className, name, descriptor, EngineController.context.getPolicy().getTaintPolicy());
        if (policy == null) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
        //去掉native
        int newAccess = access & ~ACC_NATIVE;
        final MethodVisitor mv = super.visitMethod(newAccess, name, descriptor, signature, exceptions);
        return new HookAdviceAdapter(api, new JSRInlinerAdapter(mv, newAccess, name,
                descriptor, signature, exceptions), newAccess, className, name, descriptor, policy) {
            @Override
            public void visitEnd() {
                if (!name.startsWith(nativePrefix)) {
                    if (policy.getEnter() == ON) {
                        inject(-1, true);
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
                    if (policy.getExit() == ON) {
                        inject(getReturnType().getOpcode(IRETURN), false);
                    }
                    returnValue();
                }
                super.visitEnd();
            }
        };
    }

    public MethodVisitor visitHTTPMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, Policy policy) {
        String policyName = policy.getName();
        if (HTTP_CIRCLE.equals(policyName)) {
            return new HttpAdviceAdapter(api, jsrInlinerAdapter, access, name, descriptor, policy);
        } else if (HTTP_BODY.equals(policyName)) {
            return new HttpBodyAdviceAdapter(api, jsrInlinerAdapter, access, name, descriptor);
        } else if (HTTP_BODY_READ.equals(policyName)) {
            return new HttpBodyReadAdviceAdapter(api, jsrInlinerAdapter, access, name, descriptor);
        }
        return jsrInlinerAdapter;
    }

    /**
     * 访问污点方法
     */
    public MethodVisitor visitTaintMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, Policy policy) {

        return new TaintAdviceAdapter(Opcodes.ASM9, jsrInlinerAdapter, access, className, name, descriptor, policy);
    }

    /**
     * 访问普通hook点
     */
    public MethodVisitor visitNormalMethod(int access, String name, String descriptor, MethodVisitor jsrInlinerAdapter, Policy policy) {

        return new SingleAdviceAdapter(Opcodes.ASM9, jsrInlinerAdapter, access, className, name, descriptor, policy);
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
