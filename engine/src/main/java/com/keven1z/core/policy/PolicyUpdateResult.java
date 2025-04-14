package com.keven1z.core.policy;

/**
 * 策略更新结果封装对象
 * 用于表示策略检查/更新的结果状态
 */
public class PolicyUpdateResult {
    private final boolean updated;          // 是否有更新
    private final ServerPolicy oldStrategy;     // 旧策略(更新前)
    private final ServerPolicy newStrategy;     // 新策略(更新后)
    private final String message;           // 结果描述信息
    private final Throwable error;          // 异常信息(如果有)

    // 私有构造方法
    private PolicyUpdateResult(boolean updated,
                                 ServerPolicy oldStrategy,
                                 ServerPolicy newStrategy,
                                 String message,
                                 Throwable error) {
        this.updated = updated;
        this.oldStrategy = oldStrategy;
        this.newStrategy = newStrategy;
        this.message = message;
        this.error = error;
    }
    /**
     * 创建"有更新"的结果
     */
    public static PolicyUpdateResult updated(ServerPolicy oldPolicy, ServerPolicy newPolicy) {
        return new PolicyUpdateResult(
                true,
                oldPolicy,
                newPolicy,
                String.format("Policy updated from %s to %s",
                        oldPolicy.getModifiedTime(), newPolicy.getModifiedTime()),
                null
        );
    }

    /**
     * 创建"无更新"的结果
     */
    public static PolicyUpdateResult noUpdate() {
        return new PolicyUpdateResult(
                false,
                null,
                null,
                "Policy is already up-to-date",
                null
        );
    }

    /**
     * 创建"更新失败"的结果
     */
    public static PolicyUpdateResult failed(Throwable error) {
        return new PolicyUpdateResult(
                false,
                null,
                null,
                "Policy update failed: " + error.getMessage(),
                error
        );
    }

    public boolean isUpdated() {
        return updated;
    }

    public ServerPolicy getOldStrategy() {
        return oldStrategy;
    }

    public ServerPolicy getNewStrategy() {
        return newStrategy;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getError() {
        return error;
    }
}
