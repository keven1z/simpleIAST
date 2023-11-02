package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.spy.SimpleIASTSpyManager;

public class HttpBodyAdviceAdapter extends IASTAdviceAdapter {
    public HttpBodyAdviceAdapter(int api, MethodVisitor mv, int access,  String name, String desc) {
        super(api, mv, access, name, desc);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isThrow(opcode)) {
            return;
        }
        pushReturnValue(opcode);
        Type type = Type.getType(SimpleIASTSpyManager.class);
        invokeStatic(type, AsmMethods.ASM_METHOD_HTTPSPY$_setRequestBody);
    }




}
