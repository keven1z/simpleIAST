package com.keven1z.core.utils;

import java.util.Collection;

public class StringUtils {
    /**
     * 判断字符是否存在以数组中的元素开头
     */
    public static boolean isStartsWithElementInArray(String str, String[] arr) {
        if (isEmpty(str) || arr.length == 0) {
            return false;
        }
        for (String element : arr) {
            if (str.startsWith(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Collection中是否存在以str结尾的元素
     */
    public static boolean isEndWithElementInList(String str, Collection<String> collection) {
        if (isEmpty(str) || collection.size() == 0) {
            return false;
        }
        for (String element : collection) {
            if (isEmpty(element)) {
                continue;
            }
            if (str.endsWith(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 字符串
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * java'TomcatV7 classname to internal'TomcatV7 classname
     *
     * @return internal'TomcatV7 classname
     */
    public static String toInternalClassName(String javaClassName) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(javaClassName)) {
            return javaClassName;
        }
        return javaClassName.replace('.', '/');
    }

    /**
     * internal'TomcatV7 classname to java'TomcatV7 classname
     * java/lang/String to java.lang.String
     *
     * @param internalClassName internal'TomcatV7 classname
     * @return java'TomcatV7 classname
     */
    public static String toJavaClassName(String internalClassName) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(internalClassName)) {
            return internalClassName;
        }
        return internalClassName.replace('/', '.');
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
