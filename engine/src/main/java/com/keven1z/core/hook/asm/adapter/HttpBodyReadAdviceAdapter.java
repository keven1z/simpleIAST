package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.MethodHookConfig;
import org.objectweb.asm.MethodVisitor;
import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

public class HttpBodyReadAdviceAdapter extends IASTAdviceAdapter {
    private static final String READ_BODY_1 = "()I";
    private static final String READ_BODY_2 = "([B)I";
    private static final String READ_BODY_3 = "([BII)I";

    /**
     * Creates a HookAdviceAdapter
     *
     * @param api              the ASM API version implemented by this visitor.
     * @param access           the method's access flags
     * @param className        the class's name
     * @param name             the method's name.
     * @param desc             the method's descriptor
     */
    public HttpBodyReadAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, MethodHookConfig methodHookConfig) {
        super(api, mv, access, className, name, desc, methodHookConfig);
    }


    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        pushReturnValue(opcode);
        loadThis();
        if (READ_BODY_1.equals(desc)) {
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_1);
        } else if (READ_BODY_2.equals(desc)) {
            loadArg(0);
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_2);
        } else if (READ_BODY_3.equals(desc)) {
            loadArg(0);
            loadArg(1);
            loadArg(2);
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_3);
        }
    }
}
