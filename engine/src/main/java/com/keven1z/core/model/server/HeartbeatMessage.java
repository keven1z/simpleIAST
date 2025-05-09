package com.keven1z.core.model.server;

/**
 * 心跳包信息类
 * @author keven1z
 * @since 2025/04/08
 */
public class HeartbeatMessage {
    private long timestamp;
    private double cpuUsage;
    private double memoryUsage;

    public HeartbeatMessage() {
    }

    public HeartbeatMessage(long timestamp, double cpuUsage, double memoryUsage) {
        this.timestamp = timestamp;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
