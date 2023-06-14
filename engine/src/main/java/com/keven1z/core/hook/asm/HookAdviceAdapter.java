package com.keven1z.core.hook.asm;


import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.ClassUtils;
import java.lang.spy.SimpleIASTSpyManager;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class HookAdviceAdapter extends AdviceAdapter {
    private final String methodName;
    private final String desc;
    private final String className;
    private final Policy policy;
    private final boolean isStatic;

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api       the ASM API version implemented by this visitor. Must be one
     *                  of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param access    the method's access flags (see {@link Opcodes}).
     * @param className
     * @param name      the method's name.
     * @param desc      the method's descriptor (see {@link Type Type}).
     */
    public HookAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, Policy policy) {
        super(api, mv, access, name, desc);
        this.methodName = name;
        this.desc = desc;
        this.className = className;
        this.policy = policy;
        this.isStatic = ClassUtils.isStatic(access);
    }


    @Override
    protected void onMethodEnter() {
        if (policy.getEnter() != CommonConst.ON) {
            return;
        }
        inject(-1, true);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (policy.getExit() == CommonConst.OFF) {
            return;
        }
        //如果有异常抛出，不做任何操作
        if (isThrow(opcode)){
            return;
        }
        inject(opcode, false);
    }

    private void inject(int opcode, boolean isEnter) {
        //如果是onMethod，无返回值
        if (isEnter) {
            push((Type) null);
        } else {
            pushReturnValue(opcode);
        }
        //如果是静态方法，push null
        if (!isStatic) {
            loadThis();
        } else {
            push((Type) null);
        }
        loadArgArray();
        push(className);
        push(methodName);
        push(desc);
        push(this.policy.getType().name());
        push(this.policy.getName());
        push(isEnter);
        Type type = Type.getType(SimpleIASTSpyManager.class);
        invokeStatic(type, AsmMethods.ASM_METHOD_HOOKSCHEDULER$spyMethod);
    }

    private void pushReturnValue(int opcode) {
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
    private boolean isThrow(int opcode) {
        return opcode == ATHROW;
    }

}
