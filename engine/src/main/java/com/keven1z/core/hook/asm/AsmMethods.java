package com.keven1z.core.hook.asm;

import com.keven1z.core.utils.ReflectionUtils;
import java.lang.spy.SimpleIASTSpyManager;
import org.objectweb.asm.commons.Method;

import static com.keven1z.core.hook.asm.AsmMethods.InnerHelper.getAsmMethod;

public interface AsmMethods {
    class InnerHelper {
        private InnerHelper() {
        }

        static Method getAsmMethod(final Class<?> clazz,
                                   final String methodName,
                                   final Class<?>... parameterClassArray) {
            java.lang.reflect.Method method = ReflectionUtils.unCaughtGetClassDeclaredJavaMethod(clazz, methodName, parameterClassArray);
            assert method != null;
            return Method.getMethod(method);
        }
    }

    /**
     * asm method of {@link SimpleIASTSpyManager#spyMethod(Object, Object, Object[], String, String, String, String, String, boolean)}
     */
    Method ASM_METHOD_HOOKSCHEDULER$spyMethod = getAsmMethod(
            SimpleIASTSpyManager.class,
            "spyMethod",
            Object.class,Object.class, Object[].class,String.class, String.class, String.class, String.class,String.class,boolean.class
    );
}
