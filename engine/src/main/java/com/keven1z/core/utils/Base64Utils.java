package com.keven1z.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author keven1z
 * @since 2024/07/28
 */
public class Base64Utils {
    /**
     * 使用 Base64 对字符串进行编码
     *
     * @param input 待编码的字符串
     * @return 编码后的字符串
     */
    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用 Base64 对字节数组进行编码
     *
     * @param input 待编码的字节数组
     * @return 编码后的字符串
     */
    public static String encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    /**
     * 使用 Base64 对字符串进行解码
     *
     * @param input 待解码的字符串
     * @return 解码后的字符串
     */
    public static String decode(String input) {
        byte[] decodedBytes = Base64.getDecoder().decode(input);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 使用 Base64 对字符串进行解码并返回字节数组
     *
     * @param input 待解码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeToBytes(String input) {
        return Base64.getDecoder().decode(input);
    }
}
