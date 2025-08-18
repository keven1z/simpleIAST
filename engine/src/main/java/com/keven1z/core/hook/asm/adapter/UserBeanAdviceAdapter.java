package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import org.objectweb.asm.MethodVisitor;

import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

/**
 * @author keven1z
 * @date 2025/8/15
 */
public class UserBeanAdviceAdapter extends IASTAdviceAdapter {
    /**
     * Creates a UserBeanAdviceAdapter {@link UserBeanAdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor.
     * @param access    the method's access flags .
     * @param className the class's name
     * @param name      the method's name.
     * @param desc      the method's descriptor.
     */
    public UserBeanAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc) {
        super(api, mv, access, className, name, desc, null);
    }

    @Override
    protected void onMethodEnter() {
        if (methodName.startsWith("set")) {
            pushNull();
            loadArgArray();
            loadThis();
            push(className);
            push(methodName);
            push(desc);
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_userBeanTaint);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        if (methodName.startsWith("get")) {
            pushReturnValue(opcode);
            loadArgArray();
            loadThis();
            push(className);
            push(methodName);
            push(desc);
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_userBeanTaint);
        }
    }
}
