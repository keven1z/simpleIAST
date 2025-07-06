package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

public class ArrayAdviceAdapter extends IASTAdviceAdapter {
    private static final String HOOK_ARRAY_CLASS_NAME = "java/lang/String";
    private boolean isArrayHooked = false;

    /**
     * Creates a ArrayAdviceAdapter {@link ArrayAdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor.
     * @param mv        the method visitor to which this adapter will delegate calls.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param className the class's name
     * @param name      the method's name.
     * @param desc      the method's descriptor .
     */
    public ArrayAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc) {
        super(api, mv, access, className, name, desc, null);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        if (HOOK_ARRAY_CLASS_NAME.equals(type) && opcode == Opcodes.ANEWARRAY) {
            isArrayHooked = true;
        }
    }

    @Override
    public void visitInsn(int opcode) {
        if (isArrayHooked && (opcode == Opcodes.AASTORE )) {//|| opcode == Opcodes.BASTORE
            // 保存 value, index, arrayref 到局部变量
            int valueSlot = newLocal(Type.getType(Object.class));
            int indexSlot = newLocal(Type.INT_TYPE);
            int arraySlot = newLocal(Type.getType(Object.class));

            // 保存到局部变量（顺序：value, index, arrayref）
            super.visitVarInsn(Opcodes.ASTORE, valueSlot);   // value
            super.visitVarInsn(Opcodes.ISTORE, indexSlot);   // index
            super.visitVarInsn(Opcodes.ASTORE, arraySlot);   // arrayref

            // 调用静态追踪方法 trackArrayStore(array, index, value)
            super.visitVarInsn(Opcodes.ALOAD, arraySlot);    // arrayref
            super.visitVarInsn(Opcodes.ILOAD, indexSlot);    // index
            super.visitVarInsn(Opcodes.ALOAD, valueSlot);    // value
            push(className);
            push(methodName);
            push(desc);
            invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_arrayTaint);
            // 恢复 AASTORE 所需操作数
            super.visitVarInsn(Opcodes.ALOAD, arraySlot);
            super.visitVarInsn(Opcodes.ILOAD, indexSlot);
            super.visitVarInsn(Opcodes.ALOAD, valueSlot);
        }
        super.visitInsn(opcode);
    }
}
