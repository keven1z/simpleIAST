package com.keven1z.core.hook.asm.adapter;


import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.HookPolicy;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.spy.SimpleIASTSpyManager;


public class SingleAdviceAdapter extends HookAdviceAdapter {

    /**
     * Creates a SingleAdviceAdapter {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param className the class's name
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public SingleAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, HookPolicy hookPolicy) {
        super(api, mv, access, className, name, desc, hookPolicy);
    }


    @Override
    protected void onMethodEnter() {
        if (hookPolicy.getEnter() != CommonConst.ON) {
            return;
        }

        inject(-1, true);

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

        inject(opcode, false);

    }
    protected void inject(int opcode, boolean isEnter){
        if (isEnter) {
            push((Type) null);
        } else {
            pushReturnValue(opcode);
        }
        //如果是静态方法，push null
        pushThisObject();
        loadArgArray();
        push(className);
        push(methodName);
        push(desc);
        push(this.hookPolicy.getType().name());
        push(this.hookPolicy.getName());
        push(this.hookPolicy.isRequireHttp());
        Type type = Type.getType(SimpleIASTSpyManager.class);
        invokeStatic(type, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_single);
    }
}
