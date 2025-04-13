package com.keven1z.core.monitor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 熔断器状态：CLOSED（正常）、OPEN（熔断）、HALF_OPEN（半开试探）
 */
public class CircuitBreaker {
    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final int failureThreshold;   // 触发熔断的连续失败次数阈值
    private final long resetTimeoutMs;    // 熔断后等待恢复时间（毫秒）
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private volatile long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, long resetTimeoutMs) {
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMs = resetTimeoutMs;
    }

    public enum State { CLOSED, OPEN, HALF_OPEN }

    // 是否允许请求通过
    public boolean allowRequest() {
        if (state.get() == State.OPEN) {
            // 熔断状态：检查是否超时进入半开状态
            if (System.currentTimeMillis() - lastFailureTime > resetTimeoutMs) {
                state.compareAndSet(State.OPEN, State.HALF_OPEN);
                return true;  // 允许一次试探请求
            }
            return false;
        }
        return true;  // CLOSED 或 HALF_OPEN 状态允许请求
    }

    // 标记成功（重置状态）
    public void recordSuccess() {
        state.set(State.CLOSED);
        failureCount.set(0);
    }

    // 标记失败
    public void recordFailure() {
        failureCount.incrementAndGet();
        if (failureCount.get() >= failureThreshold) {
            state.set(State.OPEN);
            lastFailureTime = System.currentTimeMillis();
        }
    }

    public State getState() {
        return state.get();
    }
}
