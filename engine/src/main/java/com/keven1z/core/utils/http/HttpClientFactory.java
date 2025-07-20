package com.keven1z.core.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 *  HTTP客户端工厂（符合阿里编码规范）
 *  <p>
 *   1. 使用连接池提升性能
 *   2. 支持自定义配置
 *   3. 线程安全实现
 *   </p>
 * @author keven1z
 * @Date 2025/4/8
 */
public class HttpClientFactory {
    private static final PoolingHttpClientConnectionManager sharedPool = new PoolingHttpClientConnectionManager();
    /**
     * 默认最大连接数
     */
    private static final int DEFAULT_MAX_TOTAL = 200;
    /**
     * 默认最大连接数
     */
    private static final int DEFAULT_TIMEOUT_VALIDATE_AFTER_INACTIVITY = 30_000;

    /**
     * 单路由默认最大连接数
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 50;

    /**
     * 默认连接超时时间（毫秒）
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    /**
     * 默认Socket超时时间（毫秒）
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    private static final int DEFAULT_REQUEST_TIMEOUT = 5000;

    /**
     * 最大重试次数
     */
    private static final int DEFAULT_MAX_RETRY_COUNT = 2;

    /**
     * 私有构造方法（工具类必须隐藏构造器）
     */
    private HttpClientFactory() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    static {
        sharedPool.setMaxTotal(DEFAULT_MAX_TOTAL);
        sharedPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        sharedPool.setValidateAfterInactivity(DEFAULT_TIMEOUT_VALIDATE_AFTER_INACTIVITY);
    }

    public static CloseableHttpClient createPooledClient() {
        return HttpClients.custom()
                .setConnectionManager(sharedPool) // 共享连接池
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                        .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                        .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                        .build())
                .setRetryHandler(new DefaultHttpRequestRetryHandler(DEFAULT_MAX_RETRY_COUNT, true))
                .build();
    }
}
