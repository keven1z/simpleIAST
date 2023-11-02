package com.keven1z.core.hook.asm.adapter;


import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.Policy;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.spy.SimpleIASTSpyManager;

public class HttpAdviceAdapter extends IASTAdviceAdapter {
    private final Policy policy;

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public HttpAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc, Policy policy) {
        super(api, mv, access, name, desc);
        this.policy = policy;
    }


    @Override
    protected void onMethodEnter() {
        if (policy.getEnter() != CommonConst.ON) {
            return;
        }
        Type type = Type.getType(SimpleIASTSpyManager.class);
        loadArg(0);
        loadArg(1);
        invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_requestStarted);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (policy.getExit() == CommonConst.OFF) {
            return;
        }
        //如果有异常抛出，不做任何操作
        if (isThrow(opcode)) {
            return;
        }
        Type type = Type.getType(SimpleIASTSpyManager.class);
        loadArg(0);
        loadArg(1);
        invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_requestEnded);
    }

}
