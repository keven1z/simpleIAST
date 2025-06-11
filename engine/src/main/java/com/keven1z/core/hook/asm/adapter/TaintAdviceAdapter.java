package com.keven1z.core.hook.asm.adapter;


import com.keven1z.core.consts.HookType;
import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.MethodHookConfig;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

public class TaintAdviceAdapter extends IASTAdviceAdapter {
    private static final String HOOK_ARRAY_CLASS_NAME = "java/lang/String";
    /**
     * Creates a TaintAdviceAdapter {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param className the class's name
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public TaintAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, MethodHookConfig methodHookConfig) {
        super(api, mv, access, className, name, desc, methodHookConfig);
    }


    @Override
    protected void onMethodEnter() {
        if (!methodHookConfig.getHookPositions().isEntry()) {
            return;
        }

        injectTaintHook(-1, true);
    }

    @Override
    protected void onMethodExit(int opcode) {
        //如果有异常抛出，不做任何操作
        if (isThrow(opcode)) {
            return;
        }

        if (!methodHookConfig.getHookPositions().isExit()) {
            return;
        }
        injectTaintHook(opcode, false);
    }
    protected void injectTaintHook(int opcode, boolean isEnter) {
        MethodHookConfig.TaintTracking taintTracking = methodHookConfig.getTaintTracking();

        if (taintTracking.isSource()){
            injectTaintTracking(opcode, isEnter, HookType.SOURCE.name());
        }
        if (taintTracking.isSink()){
            injectTaintTracking(opcode, isEnter, HookType.SINK.name());
        }
        if (taintTracking.isPropagator()) {
            injectTaintTracking(opcode, isEnter, HookType.PROPAGATION.name());
        }
    }
    protected void injectTaintTracking(int opcode, boolean isEnter,String hookType) {
        if (isEnter) {
            push((Type) null);
        } else {
            pushReturnValue(opcode);
        }
        pushThisObject();
        loadArgArray();
        push(className);
        push(methodName);
        push(desc);
        push(hookType);
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_taint);
    }
// hook array
//    @Override
//    public void visitTypeInsn(int opcode, String type) {
//        if (HOOK_ARRAY_CLASS_NAME.equals(type) && opcode == Opcodes.ANEWARRAY) {
//            System.out.println(type);
//        }
//        super.visitTypeInsn(opcode, type);
//    }
}
