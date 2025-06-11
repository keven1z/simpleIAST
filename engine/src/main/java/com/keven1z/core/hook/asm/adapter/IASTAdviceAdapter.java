package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.policy.MethodHookConfig;
import com.keven1z.core.utils.ClassUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author keven1z
 * @since 2023/10/19
 */
public class IASTAdviceAdapter extends AdviceAdapter {
    protected final String methodName;
    protected final String desc;
    protected final String className;
    protected final MethodHookConfig methodHookConfig;
    protected final boolean isStatic;
    private final Type[] argumentTypeArray;
    /**
     * Creates a HookAdviceAdapter {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param className the class's name
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public IASTAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, MethodHookConfig methodHookConfig) {
        super(api, mv, access, name, desc);
        this.methodName = name;
        this.desc = desc;
        this.className = className;
        this.methodHookConfig = methodHookConfig;
        this.isStatic = ClassUtils.isStatic(access);
        this.argumentTypeArray = Type.getArgumentTypes(desc);
    }
    protected void pushThisObject() {
        if (!isStaticMethod()) {
            loadThis();
        } else {
            pushNull();
        }
    }
    final protected boolean isStaticMethod() {
        return (methodAccess & ACC_STATIC) != 0;
    }
    final protected void storeArgArray() {
        for (int i = 0; i < argumentTypeArray.length; i++) {
            loadArg(i);
        }
    }

    protected void pushReturnValue(int opcode) {
        if (opcode == RETURN) {
            visitInsn(ACONST_NULL);
        } else if (opcode == ARETURN || opcode == ATHROW) {
            dup();
        } else {
            if (opcode == LRETURN || opcode == DRETURN) {
                dup2();
            } else {
                dup();
            }
            box(Type.getReturnType(this.methodDesc));
        }
    }

    protected boolean isThrow(int opcode) {
        return opcode == ATHROW;
    }

    final protected void pushNull() {
        push((Type) null);
    }
}
