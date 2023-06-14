package com.keven1z.core.hook.asm;

import com.keven1z.core.Config;
import com.keven1z.core.EngineController;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.PolicyUtils;
import org.apache.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;

/**
 * 自定义的ClassVisitor，用于访问类的方法
 */
public class HookClassVisitor extends ClassVisitor {
    private final String className;
    //监控进入hook点，但未成功匹配方法描述，弹出警告告知method或者desc错误
    private boolean isVisitMethod;

    public HookClassVisitor(ClassWriter classWriter, String className) {
        super(Opcodes.ASM9, classWriter);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        //如果是本地方法，不进行hook
        if (isNative(access)) {
            return methodVisitor;
        }
        Policy policy = PolicyUtils.getHookedPolicy(className, name, descriptor, EngineController.context.getPolicy());
        if (policy == null) {
            //打印错误日志
            return methodVisitor;
        }
        isVisitMethod = true;

        JSRInlinerAdapter jsrInlinerAdapter = new JSRInlinerAdapter(methodVisitor, access, name, descriptor, signature, exceptions);
        return new HookAdviceAdapter(Opcodes.ASM9, jsrInlinerAdapter, access, className, name, descriptor, policy);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (!isVisitMethod) {
            System.out.println("[SimpleIAST] Failed to visit method,className:" + this.className + ",Please check method or desc in policy.json");
            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(getClass()).info("Failed to visit method,className:" + this.className + ",Please check method or desc in policy.json");
            }
        }
    }

    // 是否native方法
    private boolean isNative(final int access) {
        return (access & Opcodes.ACC_NATIVE) != 0;
    }

}
