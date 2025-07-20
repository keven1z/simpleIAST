package com.keven1z.core.utils.http;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;


import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * HTTP客户端注册中心（支持全局复用和运行时初始化）
 * <p>
 * 1. 通过Class类型获取客户端实例
 * 2. 支持应用启动时批量初始化
 * 3. 线程安全的懒加载机制
 * </p>
 *
 * @author keven1z
 * @Date 2024/4/10
 * @since 0.3.0
 */
public final class HttpClientRegistry implements Closeable {
    private static final Logger LOGGER = Logger.getLogger(HttpClientRegistry.class);

    private static volatile HttpClientRegistry instance;
    private final Map<Class<?>, Closeable> clientMap = new ConcurrentHashMap<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private HttpClientRegistry() {
    }
    private static class Inner {
        private static final HttpClientRegistry clientRegistry = new HttpClientRegistry();
    }
    /**
     * 获取单例实例
     */
    public static HttpClientRegistry getInstance() {
        return Inner.clientRegistry;
    }

    /**
     * 初始化所有客户端（应用启动时调用）
     *
     * @param clientSuppliers 客户端构造器映射
     * @throws IllegalStateException 重复初始化时抛出
     */
    public void initAll(Map<Class<?>, Supplier<? extends Closeable>> clientSuppliers) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("注册中心已被初始化");
        }

        clientSuppliers.forEach((clazz, supplier) -> {
            Validate.notNull(clazz, "客户端Class不能为空");
            Validate.notNull(supplier, "客户端构造器不能为空");
            getClientInternal(clazz, supplier);
        });

        LOGGER.info("成功初始化{}个HTTP客户端"+clientSuppliers.size());
    }

    /**
     * 获取客户端实例（线程安全懒加载）
     *
     * @param clazz 客户端Class对象
     * @param <T>   客户端类型
     * @return 已存在的或新建的客户端实例
     * @throws IllegalStateException 未初始化且未提供构造器时抛出
     */
    @SuppressWarnings("unchecked")
    public <T extends Closeable> T getClient(Class<T> clazz) {
        return (T) getClientInternal(clazz, null);
    }

    /**
     * 获取客户端实例（带懒加载构造器）
     *
     * @param clazz    客户端Class对象
     * @param supplier 客户端构造器（可选）
     * @param <T>      客户端类型
     * @return 客户端实例
     */
    @SuppressWarnings("unchecked")
    public <T extends Closeable> T getClient(Class<T> clazz, Supplier<T> supplier) {
        return (T) getClientInternal(clazz, supplier);
    }

    private Closeable getClientInternal(Class<?> clazz, Supplier<? extends Closeable> supplier) {
        Closeable client = clientMap.get(clazz);
        if (client == null) {
            synchronized (clazz) {
                client = clientMap.get(clazz);
                if (client == null) {
                    if (supplier == null && !initialized.get()) {
                        throw new IllegalStateException("客户端未初始化且未提供构造器: " + clazz.getName());
                    }
                    client = supplier != null ? supplier.get() : createDefaultClient(clazz);
                    clientMap.put(clazz, client);
                    LOGGER.debug("创建客户端实例: " + clazz.getSimpleName());
                }
            }
        }
        return client;
    }

    /**
     * 创建默认客户端（可根据需要重写）
     */
    private Closeable createDefaultClient(Class<?> clazz) {
        throw new UnsupportedOperationException("未实现默认构造器: " + clazz.getName());
    }

    @Override
    public void close() throws IOException {
        clientMap.forEach((clazz, client) -> {
            try {
                client.close();
                LOGGER.debug("成功关闭客户端: " + clazz.getSimpleName());
            } catch (Exception e) {
                LOGGER.warn("关闭客户端失败: {}" + clazz.getSimpleName(), e);
            }
        });
        clientMap.clear();
        initialized.set(false);
        LOGGER.info("已释放所有HTTP客户端资源");
    }

    /**
     * 获取当前已注册的客户端数量
     */
    public int getClientCount() {
        return clientMap.size();
    }
}
