package com.keven1z.core.hook.asm.adapter;


import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.HookPolicy;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

public class HttpAdviceAdapter extends IASTAdviceAdapter {
    private final HookPolicy hookPolicy;

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public HttpAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc, HookPolicy hookPolicy) {
        super(api, mv, access, name, desc);
        this.hookPolicy = hookPolicy;
    }


    @Override
    protected void onMethodEnter() {
        if (hookPolicy.getEnter() != CommonConst.ON) {
            return;
        }
        loadArg(0);
        loadArg(1);
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_requestStarted);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (hookPolicy.getExit() == CommonConst.OFF) {
            return;
        }
        //如果有异常抛出，不做任何操作
        if (isThrow(opcode)) {
            return;
        }
        loadArg(0);
        loadArg(1);
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_requestEnded);
    }

}
