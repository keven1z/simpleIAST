package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.spy.SimpleIASTSpyManager;

public class HttpBodyReadAdviceAdapter extends IASTAdviceAdapter {
    private static final String READ_BODY_1 = "()I";
    private static final String READ_BODY_2 = "([B)I";
    private static final String READ_BODY_3 = "([BII)I";
    private final String desc;

    public HttpBodyReadAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
        this.desc = desc;
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        pushReturnValue(opcode);
        Type type = Type.getType(SimpleIASTSpyManager.class);
        loadThis();
        if (READ_BODY_1.equals(desc)) {
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_1);
        } else if (READ_BODY_2.equals(desc)) {
            loadArg(0);
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_2);
        } else if (READ_BODY_3.equals(desc)) {
            loadArg(0);
            loadArg(1);
            loadArg(2);
            invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_onReadInvoked_3);
        }
    }
}
