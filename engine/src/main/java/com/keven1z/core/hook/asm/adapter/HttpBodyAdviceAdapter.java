package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.MethodHookConfig;
import org.objectweb.asm.MethodVisitor;
import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

public class HttpBodyAdviceAdapter extends IASTAdviceAdapter {


    /**
     * Creates a HookAdviceAdapter {@link AdviceAdapter}.
     *
     * @param api              the ASM API version implemented by this visitor
     * @param mv
     * @param access           the method's access flags
     * @param className        the class's name
     * @param name             the method's name.
     * @param desc             the method's descriptor
     */
    public HttpBodyAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, MethodHookConfig methodHookConfig) {
        super(api, mv, access, className, name, desc, methodHookConfig);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        pushReturnValue(opcode);
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_setRequestBody);
    }
}
