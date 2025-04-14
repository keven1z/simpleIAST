package com.keven1z.core.policy;

import com.keven1z.core.utils.http.HttpClientRegistry;
import com.keven1z.core.utils.http.PolicyClient;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 策略管理核心类
 */
public class ServerPolicyManager {
    private static final String STRATEGY_LOCK = "policy_lock";
    private final Logger LOGGER = Logger.getLogger(getClass());

    private final PolicyClient policyClient;
    private final LocalStrategyCache localCache;
    private volatile ServerPolicy currentPolicy;

    public static ServerPolicyManager getInstance() {
        return ServerPolicyManager.Inner.policyManager;
    }

    private static class Inner {
        public static final ServerPolicyManager policyManager = new ServerPolicyManager();
    }

    private ServerPolicyManager() {
        this.policyClient = HttpClientRegistry.getInstance().getClient(PolicyClient.class);
        this.localCache = new LocalStrategyCache();
    }

    public PolicyClient getPolicyClient() {
        return policyClient;
    }

    /**
     * 初始化加载策略
     */
    public void loadInitialPolicy() {
        try {
            // 1. 尝试从本地加载
            ServerPolicy localServerPolicy = localCache.load();

            // 2. 如果本地不存在或强制刷新，则从远程获取
            if (localServerPolicy == null) {
                ServerPolicy remoteServerPolicy = policyClient.fetchPolicies();
                if (remoteServerPolicy != null) {
                    localCache.save(remoteServerPolicy);
                    currentPolicy = remoteServerPolicy;
                    return;
                }
            }

            // 3. 使用本地策略
            currentPolicy = localServerPolicy;
        } catch (Exception e) {
            LOGGER.error("Failed to load initial strategy", e);
            currentPolicy = getFallbackStrategy();
        }
    }

    /**
     * 检查并更新策略
     */
    public PolicyUpdateResult checkUpdate() {
        synchronized (STRATEGY_LOCK) {
            try {
                ServerPolicy remoteServerPolicy = policyClient.fetchPolicies();
                if (remoteServerPolicy == null) {
                    return PolicyUpdateResult.noUpdate();
                }

                // 版本比较
                if (currentPolicy == null ||
                        remoteServerPolicy.getModifiedTime().compareTo(currentPolicy.getModifiedTime()) > 0) {
                    localCache.save(remoteServerPolicy);
                    ServerPolicy oldPolicy = currentPolicy;
                    currentPolicy = remoteServerPolicy;
                    return PolicyUpdateResult.updated(oldPolicy, remoteServerPolicy);
                }

                return PolicyUpdateResult.noUpdate();
            } catch (Exception e) {
                LOGGER.warn("Failed to check strategy update", e);
                return PolicyUpdateResult.failed(e);
            }
        }
    }

    /**
     * 分层回退策略
     */
    private ServerPolicy getFallbackStrategy() {
        // 第一优先级：尝试从本地缓存加载
        ServerPolicy cached = localCache.load();
        if (cached != null) {
            return cached;
        }

        // 第二优先级：返回硬编码的默认策略
        return createHardcodedFallback();
    }

    /**
     * 获取回退策略
     */
    public ServerPolicy createHardcodedFallback() {
        // 默认策略配置
        ServerPolicy fallback = new ServerPolicy();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(TimeZone.getTimeZone("Asia/Shanghai").toZoneId());
        String timestamp = LocalDateTime.now().format(formatter);
        fallback.setModifiedTime(timestamp);
        fallback.setAgentEnabled(true);
        fallback.setDetectEnabled(true);
        fallback.setDebugMode(false);//回退策略默认不打开debug模式
        return fallback;
    }
}
