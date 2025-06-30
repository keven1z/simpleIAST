package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.consts.SourceType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class SourceHandlerFactory {
    // 使用EnumMap保证枚举键的顺序和效率
    private static final Map<SourceType, Supplier<SourceHandler>> handlerSuppliers = new EnumMap<>(SourceType.class);

    // 默认处理器注册（静态初始化）
    static {
        registerHandler(SourceType.HTTP_STRING_PARAM, StringParamHandler::new);
        registerHandler(SourceType.HTTP_SPRING_PARAM, SpringParamHandler::new);
        registerHandler(SourceType.HTTP_HEADER_PARAM, HeaderParamHandler::new);
        registerHandler(SourceType.HTTP_MULTIPART_PARAM, MultipartParamHandler::new);
        registerHandler(SourceType.HTTP_BODY_PARAM, BodyParamHandler::new);
        registerHandler(SourceType.HTTP_BEAN_PARAM, BeanParamHandler::new);

    }

    /**
     * 注册/覆盖处理器（线程安全）
     * @param type Source类型
     * @param supplier 处理器的无参构造方法引用（如 MyHandler::new）
     */
    public static synchronized void registerHandler(SourceType type, Supplier<SourceHandler> supplier) {
        handlerSuppliers.put(type, supplier);
    }

    /**
     * 获取处理器实例（懒加载 + 线程安全）
     * @param type Source类型
     * @return 处理器实例
     * @throws IllegalArgumentException 如果类型未注册
     */
    public static SourceHandler getHandler(SourceType type) {
        Supplier<SourceHandler> supplier = handlerSuppliers.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("未注册的Source类型: " + type);
        }
        return supplier.get(); // 每次调用生成新实例（避免状态残留）
    }
}
