/*
 * Copyright 2017-2021 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keven1z.core.utils;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by tyy on 3/27/17.
 * All rights reserved
 * 反射工具类
 */
public class ReflectionUtils {
    public final static Class<?>[] STRING_CLASS = new Class[]{String.class};
    public final static Class<?>[] EMPTY_CLASS = new Class[]{};

    /**
     * 根据方法名调用对象的某一个方法
     *
     * @param object     调用方法的对象
     * @param methodName 方法名称
     * @param paramTypes 参数类型列表
     * @param parameters 参数列表
     * @return 方法返回值
     */
    public static Object invokeMethod(Object object, String methodName, Class[] paramTypes, Object... parameters) {
        if (object == null) {
            return null;
        }
        return invokeMethod(object, object.getClass(), methodName, paramTypes, parameters);
    }

    /**
     * 反射调用方法，并把返回值进行强制转换为String
     *
     * @return 被调用函数返回的String
     * @see #invokeMethod(Object, String, Class[], Object...)
     */
    public static String invokeStringMethod(Object object, String methodName, Class[] paramTypes, Object... parameters) {
        if (object == null){
            return null;
        }
        Object ret = invokeMethod(object, methodName, paramTypes, parameters);
        return ret != null ? (String) ret : null;
    }

    /**
     * 反射获取对象的字段包括私有的
     *
     * @param object    被提取字段的对象
     * @param fieldName 字段名称
     * @return 字段的值
     */
    public static Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * 反射获取class的静态字段
     * @param clazz 被提取字段的class
     * @param fieldName 字段名称
     * @return 字段的值
     */
    public static Object getStaticField(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(null);
    }
    /**
     * 反射获取父类对象的字段包括私有的
     *
     * @param paramClass 被提取字段的对象
     * @param fieldName  字段名称
     * @return 字段的值
     */
    public static Object getSuperField(Object paramClass, String fieldName) {
        Object object = null;
        try {
            Field field = paramClass.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception e) {
//            LogTool.traceError(ErrorType.RUNTIME_ERROR, e.getMessage(), e);
        }
        return object;
    }

    /**
     * 调用某一个类的静态方法
     *
     * @param className  类名
     * @param methodName 方法名称
     * @param paramTypes 参数类型列表
     * @param parameters 参数列表
     * @return 方法返回值
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] paramTypes, Object... parameters) {
        try {
            Class clazz = Class.forName(className);
            return invokeMethod(null, clazz, methodName, paramTypes, parameters);
        } catch (Exception e) {
//            LogTool.traceError(ErrorType.RUNTIME_ERROR, "failed to invoke static method: " + e.getMessage(), e);
            return null;
        }
    }

    public static Object invokeMethod(Object object, Class clazz, String methodName, Class[] paramTypes, Object... parameters) {
        try {
            Method method = clazz.getMethod(methodName, paramTypes);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(object, parameters);
        } catch (Exception e) {
            String message = "Reflection call " + methodName + " failed: " + e.getMessage();
            if (clazz != null) {
                message = "Reflection call " + clazz.getName() + "." + methodName + " failed: " + e.getMessage();
            }
            return null;
        }
    }

    public static boolean isPrimitiveType(Object object) {
        try {
            return ((Class<?>) object.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取Java类的方法
     * 该方法不会抛出任何声明式异常
     *
     * @param clazz               类
     * @param name                方法名
     * @param parameterClassArray 参数类型数组
     * @return Java方法
     */
    public static Method unCaughtGetClassDeclaredJavaMethod(final Class<?> clazz,
                                                            final String name,
                                                            final Class<?>... parameterClassArray) {
        try {
            return clazz.getDeclaredMethod(name, parameterClassArray);
        } catch (NoSuchMethodException e) {
                LogTool.error(ErrorType.REFLECT_ERROR, "Failed to getDeclaredMethod,method name:" + name);
        }
        return null;
    }

}
