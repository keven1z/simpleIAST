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
     * asm method of {@link SimpleIASTSpyManager#$_taint(Object, Object, Object[], String, String, String, String, String)}
     */
    Method ASM_METHOD_HOOKSCHEDULER$_taint = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_taint",
            Object.class, Object.class, Object[].class, String.class, String.class, String.class, String.class, String.class
    );
    /**
     * asm method of {@link SimpleIASTSpyManager#$_single(Object, Object, Object[], String, String, String, String, String, boolean)}
     */
    Method ASM_METHOD_HOOKSCHEDULER$_single = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_single",
            Object.class, Object.class, Object[].class, String.class, String.class, String.class, String.class, String.class, boolean.class
    );
    /**
     * asm method of {@link com.keven1z.core.hook.http.HttpSpy#$_setRequestBody(Object)}
     */
    Method ASM_METHOD_HTTPSPY$_setRequestBody = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_setRequestBody",
            Object.class
    );
    /**
     * asm method of {@link com.keven1z.core.hook.http.HttpSpy#$_requestStarted(Object, Object)}
     */
    Method ASM_METHOD_HTTPSPY$_requestStarted = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_requestStarted",
            Object.class, Object.class
    );
    /**
     * asm method of {@link com.keven1z.core.hook.http.HttpSpy#$_requestEnded(Object, Object)}
     */
    Method ASM_METHOD_HTTPSPY$_requestEnded = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_requestEnded",
            Object.class, Object.class
    );
    /**
     * asm method of {@link com.keven1z.core.hook.http.HttpSpy#$_onReadInvoked(Integer, Object, byte[], int, int)}
     */
    Method ASM_METHOD_HTTPSPY$_onReadInvoked_3 = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_onReadInvoked",
            Integer.class,
            Object.class,
            byte[].class,
            int.class,
            int.class
    );
    Method ASM_METHOD_HTTPSPY$_onReadInvoked_2 = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_onReadInvoked",
            Integer.class,
            Object.class,
            byte[].class
    );
    Method ASM_METHOD_HTTPSPY$_onReadInvoked_1 = getAsmMethod(
            SimpleIASTSpyManager.class,
            "$_onReadInvoked",
            Integer.class,
            Object.class
    );
}
