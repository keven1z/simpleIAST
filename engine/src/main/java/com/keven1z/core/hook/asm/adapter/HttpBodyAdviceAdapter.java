package com.keven1z.core.hook.asm.adapter;

import com.keven1z.core.hook.asm.AsmMethods;
import org.objectweb.asm.MethodVisitor;
import static com.keven1z.core.hook.asm.AsmMethods.ASM_TYPE_SPY;

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
        invokeStatic(ASM_TYPE_SPY, AsmMethods.ASM_METHOD_HTTPSPY$_setRequestBody);
    }




}
