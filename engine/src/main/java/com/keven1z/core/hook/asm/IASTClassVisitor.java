package com.keven1z.core.hook.asm;

import com.keven1z.core.EngineController;
import com.keven1z.core.hook.asm.adapter.HttpAdviceAdapter;
import com.keven1z.core.hook.asm.adapter.HttpBodyAdviceAdapter;
import com.keven1z.core.hook.asm.adapter.HttpBodyReadAdviceAdapter;
import com.keven1z.core.hook.asm.adapter.SingleAdviceAdapter;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.hook.asm.adapter.TaintAdviceAdapter;
import com.keven1z.core.utils.PolicyUtils;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;

/**
 * 自定义的ClassVisitor，用于访问类的方法
 */
public class IASTClassVisitor extends ClassVisitor {
    protected final String className;
    //监控进入hook点，但未成功匹配方法描述，弹出警告告知method或者desc错误
    private boolean isVisitMethod;
    protected final Logger hookLogger = Logger.getLogger("hook.info");
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

    public IASTClassVisitor(ClassVisitor classVisitor, String className) {
        super(Opcodes.ASM9, classVisitor);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        //如果是本地方法，不进行hook
        if (isNative(access)) {
            return methodVisitor;
        }
        JSRInlinerAdapter jsrInlinerAdapter = new JSRInlinerAdapter(methodVisitor, access, name, descriptor, signature, exceptions);

        Policy policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getHttp());
        if (policy != null) {
            //打印错误日志
            return visitHTTPMethod(access, name, descriptor, jsrInlinerAdapter, policy);
        }
        policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getSingles());
        if (policy != null) {
            //打印错误日志
            return visitNormalMethod(access, name, descriptor, jsrInlinerAdapter, policy);
        }

        policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy().getTaintPolicy());
        if (policy != null) {
            //打印错误日志
            return visitTaintMethod(access, name, descriptor, jsrInlinerAdapter, policy);
        }

        return methodVisitor;
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
        return (access & Opcodes.ACC_NATIVE) != 0;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
//        if (!isVisitMethod) {
//            System.out.println("[SimpleIAST] Failed to visit method,className:" + this.className + ",Please check method or desc in policy.json");
//            if (LogTool.isDebugEnabled()) {
//                Logger.getLogger(getClass()).info("Failed to visit method,className:" + this.className + ",Please check method or desc in policy.json");
//            }
//        }
    }
}
