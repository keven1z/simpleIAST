package com.keven1z.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * json工具类
 * @author keven1z
 */
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
        return mapper.readValue(jsonString, cls);
    }

    public static <T> T parseObject(Object jsonObject, Class<T> cls){
        return mapper.convertValue(jsonObject, cls);
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        return mapper.readValue(json, typeReference);
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> List<T> toList(String json, TypeReference<List<T>> listTypeReference) throws JsonProcessingException {
        return mapper.readValue(json, listTypeReference);
    }

    /**
     * json字符串转换成List集合
     * (需要实体类)
     *
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
    /**
     * 通用反序列化方法，支持泛型类型和整个对象的自定义反序列化器
     *
     * @param jsonString JSON 字符串
     * @param targetClass 要注册反序列化器的类
     * @param deserializer 自定义反序列化器
     * @param <T>         返回的目标类型
     * @return 反序列化后的对象
     */
    public static <T> T toObject(String jsonString, Class<T> targetClass, JsonDeserializer<? extends T> deserializer)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(targetClass,deserializer);
        mapper.registerModule(module);
        return mapper.readValue(jsonString, targetClass);
    }
}
