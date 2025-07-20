package com.keven1z.core.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtils {
    /**
     * 判断字符是否存在以数组中的元素开头
     */
    public static boolean isStartsWithElementInArray(String str, String[] arr) {
        if (isEmpty(str)) {
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
        if (isEmpty(str) || collection.isEmpty()) {
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
    private static final String URL_REGEX = "^((https?|ftp)://([^/\\s]+?)(:\\d{1,5})?|file:/{1,3})" +  // 协议部分
            "([^\\s?#]*)" +  // 路径部分
            "(\\?[^#]*)?" +   // 查询参数
            "(#.*)?$";        // 锚点
    /**
     * 判断是否为URL
     *
     * @return true 为url格式
     */
    public static boolean isURL(String str) {
        //转换为小写
        str = str.toLowerCase();
        return str.matches(URL_REGEX);
    }

    /**
     * 判断元素是否不在集合中
     *
     * @param element 要检查的元素
     * @param list    要检查的字符串集合
     * @return 如果元素在集合中，则返回true；否则返回false
     */
    public static boolean absentFromCollection(String element, List<String> list) {
        if (element == null || list == null) {
            return true;
        }
        return !list.contains(element);
    }

    /**
     * 解析URL查询字符串为键值对映射
     *
     * @param query 需要解析的URL字符串（应包含合法的查询参数）
     * @return 参数键值对映射，遵循以下规则：
     *         - 使用UTF-8字符集进行URL解码
     *         - 当存在重复参数名时，保留第一个出现的参数值（遵循RFC 3986不强制唯一键的规范）
     *         - 返回的Map不可修改（unmodifiableMap）
     * @throws IllegalArgumentException 如果URL格式不合法
     */
    public static Map<String, String> parseQuery(String query) {
        if (isEmpty(query)) return Collections.emptyMap();

        return Arrays.stream(query.split("&"))
                .map(pair -> {
                    // 先拆分键值对再解码，保留%26的原始语义
                    int idx = pair.indexOf("=");
                    if (idx < 0) {
                        return new String[]{pair, pair};
                    }
                    String key = pair.substring(0, idx);
                    String value = idx < pair.length() - 1 ? pair.substring(idx + 1) : "";
                    return new String[]{key, value};
                })
                .filter(arr -> !arr[0].isEmpty())
                .collect(Collectors.toMap(
                        arr -> decodeComponent(arr[0], StandardCharsets.UTF_8),
                        arr -> decodeComponent(arr[1], StandardCharsets.UTF_8),
                        (first, second) -> first
                ));
    }
    /**
     * URL解码（兼容Java8且处理+号转空格）
     */
    private static String decodeComponent(String s, Charset charset) {
        try {
            // 手动处理+号替换为空格（兼容Java8 URLDecoder行为）
            return URLDecoder.decode(s.replace("+", "%2B"), charset.name())
                    .replace("%2B", "+");
        } catch (IllegalArgumentException|UnsupportedEncodingException e) {
            return s; // 返回原始字符串用于容错
        }
    }
}
