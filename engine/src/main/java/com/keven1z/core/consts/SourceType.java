package com.keven1z.core.consts;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参数来源类型枚举
 * <p>定义HTTP请求中不同参数来源的类型标识</p>
 *
 * @author keven1z
 * @created 2025/4/17
 */
public enum SourceType {
    /**
     * HTTP请求参数为string的情况(非标准请求未位置,仅用于policy)
     */
    HTTP_STRING_PARAM("http_string_param"),
    /**
     * HTTP请求参数通过spring框架获取的情况(非标准请求位置,仅用于policy)
     */
    HTTP_SPRING_PARAM("http_spring_param"),
    /**
     * HTTP请求参数通过bean获取的情况(非标准请求位置,仅用于policy)
     */
    HTTP_BEAN_PARAM("http_bean_param"),

    /**
     * URL查询参数（如?name=value）
     */
    HTTP_QUERY_PARAM("http_query_param"),
    /**
     * 表单提交参数（application/x-www-form-urlencoded）
     */
    HTTP_FORM_PARAM("http_form_param"),
    /**
     * JSON格式请求体参数（application/json）
     */
    HTTP_JSON_PARAM("http_json_param"),
    /**
     * XML格式请求体参数（application/xml）
     */
    HTTP_XML_PARAM("http_xml_param"),
    /**
     * HTTP请求头参数
     */
    HTTP_HEADER_PARAM("http_header_param"),
    /**
     * Cookie中携带的参数
     */
    HTTP_COOKIE_PARAM("http_cookie_param"),
    /**
     * 文件上传或多部分表单参数（multipart/form-data）
     */
    HTTP_MULTIPART_PARAM("http_multipart_param"),
    HTTP_BODY_PARAM("http_body_param"),
    ;
    // 名称到枚举的映射
    private static final Map<String, SourceType> nameToEnum =
            Arrays.stream(values())
                    .collect(Collectors.toMap(SourceType::getName, e -> e));
    private final String name;

    SourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    /**
     * 根据名称获取对应的枚举值
     * @param name 枚举名称
     * @return 对应的枚举值
     * @throws IllegalArgumentException 如果找不到对应的枚举值
     */
    public static SourceType fromName(String name) {
        SourceType result = nameToEnum.get(name);
        if (result == null) {
            throw new IllegalArgumentException("No enum constant " + SourceType.class.getName() + "." + name);
        }
        return result;
    }
}

