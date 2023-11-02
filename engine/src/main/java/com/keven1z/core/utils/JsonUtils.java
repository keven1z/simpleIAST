package com.keven1z.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        //在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SS
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //在序列化时自定义时间日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //在序列化时忽略值为 null 的属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    public static <T> T toObject(String jsonString, Class<T> cls) throws JsonProcessingException {
        checkJsonStringQuietly(jsonString);
        return mapper.readValue(jsonString,cls);
    }
    public static String toString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
    public static <T> List<T> toList(String json, TypeReference<List<T>> listTypeReference) throws JsonProcessingException {
        return mapper.readValue(json,listTypeReference);
    }
    /**
     * json字符串转换成List集合
     * (需要实体类)
     * @param json
     */
    public static <T> T toArrayQuietly(String json, Class<T> cla) {
        checkJsonStringQuietly(json);

        try {
            return mapper.readValue(json, cla);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json is empty");
        }
    }

    private static void checkJsonStringQuietly(String json) {
        if (json == null) {
            throw new RuntimeException("json is null");
        }
        if ("".equals(json)) {
            throw new RuntimeException("json is empty");
        }
    }
}
