package com.keven1z.core.monitor;


import com.keven1z.core.error.http.HeartbeatSendException;
import com.keven1z.core.model.server.HeartbeatMessage;
import com.keven1z.core.utils.OSUtils;
import com.keven1z.core.utils.http.HttpClientRegistry;
import com.keven1z.core.utils.http.HttpHeartbeatClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 心跳线程
 *
 * @author keven1z
 * @since 2023/11/3
 */

public class HeartbeatMonitor extends Monitor {
    private HttpHeartbeatClient heartbeatClient;  // 心跳客户端（如Socket/HTTP实现）
    private final CircuitBreaker circuitBreaker;    // 熔断器
    private final long heartbeatInterval = 30000L;           // 心跳间隔（毫秒）
    private final int failureThreshold = 5;
    private final int resetTimeoutMs = 60;

    private final AtomicInteger consecutiveFailures = new AtomicInteger(0); // 连续失败计数

    public HeartbeatMonitor() {
        this.circuitBreaker = new CircuitBreaker(failureThreshold, resetTimeoutMs);
    }

    @Override
    public String getThreadName() {
        return "SimpleIAST-Heartbeat-Thread";
    }

    @Override
    public boolean isForServer() {
        return true;
    }

    @Override
    public void doRun() throws Exception {
        if (this.heartbeatClient == null) {
            this.heartbeatClient = HttpClientRegistry.getInstance().getClient(HttpHeartbeatClient.class);
        }
        // 检查熔断器状态
        if (!circuitBreaker.allowRequest()) {
            handleCircuitBreakerOpen();
            return;
        }

        // 发送心跳包
        sendHeartbeat();

        // 休眠直到下一个心跳周期
        TimeUnit.MILLISECONDS.sleep(heartbeatInterval);

    }

    private void sendHeartbeat() {
        try {
            HeartbeatMessage heartbeatMsg = generateHeartbeatMessage();
            boolean success =  heartbeatClient.sendMessage(heartbeatMsg);
            // 更新熔断器状态
            if (success) {
                circuitBreaker.recordSuccess();
                consecutiveFailures.set(0);
            } else {
                consecutiveFailures.incrementAndGet();
                circuitBreaker.recordFailure();
                handleHeartbeatFailure();
            }
        } catch (HeartbeatSendException e) {
            consecutiveFailures.incrementAndGet();
            circuitBreaker.recordFailure();
            handleHeartbeatFailure();
        }
    }

    protected  HeartbeatMessage generateHeartbeatMessage(){
        long currentTimeMillis = System.currentTimeMillis();
        return new HeartbeatMessage(currentTimeMillis, OSUtils.getSystemCpuLoad(),OSUtils.getSystemMemory());
    }

    private void handleCircuitBreakerOpen() {
        logger.warn("CircuitBreaker is OPEN. Skipping heartbeat.");
    }

    private void handleHeartbeatFailure() {
        logger.error("Heartbeat failed. Consecutive failures: " + consecutiveFailures.get());
    }

}
