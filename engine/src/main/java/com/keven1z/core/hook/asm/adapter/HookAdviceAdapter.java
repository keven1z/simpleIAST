package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.ClassUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;


/**
 * @author keven1z
 * @date 2023/10/23
 */
public class HookAdviceAdapter extends IASTAdviceAdapter {
    protected final String methodName;
    protected final String desc;
    protected final String className;
    protected final Policy policy;
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
    public HookAdviceAdapter(int api, MethodVisitor mv, int access, String className, String name, String desc, Policy policy) {
        super(api, mv, access, name, desc);
        this.methodName = name;
        this.desc = desc;
        this.className = className;
        this.policy = policy;
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
    protected void inject(int opcode, boolean isEnter) {
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
        push(this.policy.getType().name());
        push(this.policy.getName());
        push(this.policy.getFrom());
        push(this.policy.getTo());
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HOOKSCHEDULER$_taint);
    }
}
