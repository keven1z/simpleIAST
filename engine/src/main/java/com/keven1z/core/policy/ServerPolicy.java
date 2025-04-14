package com.keven1z.core.policy;

import java.util.List;

/**
 * 服务端统一策略
 * 包含所有需要从服务端下发的配置项
 */
public class ServerPolicy {
    // Agent控制配置
    private boolean agentEnabled;
    private boolean detectEnabled;//是否开启检测
    private int maxCpuUsage; // CPU使用率阈值(0-100)
    private int maxMemoryUsage; // CPU使用率阈值(0-100)

    // 检测模块开关
    private List<String> forceDisabledRules;

    // 高级配置
    private boolean debugMode;
    private String logLevel;

    // 白名单配置
    private List<String> excludePackages;
    private List<String> excludeClasses;
    private List<String> excludeUrls;
    // 修订时间
    private String  modifiedTime;

    public ServerPolicy() {
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public List<String> getExcludeClasses() {
        return excludeClasses;
    }

    public void setExcludeClasses(List<String> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    public List<String> getExcludePackages() {
        return excludePackages;
    }

    public void setExcludePackages(List<String> excludePackages) {
        this.excludePackages = excludePackages;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public List<String> getForceDisabledRules() {
        return forceDisabledRules;
    }

    public void setForceDisabledRules(List<String> forceDisabledRules) {
        this.forceDisabledRules = forceDisabledRules;
    }

    public int getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public void setMaxMemoryUsage(int maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

    public int getMaxCpuUsage() {
        return maxCpuUsage;
    }

    public void setMaxCpuUsage(int maxCpuUsage) {
        this.maxCpuUsage = maxCpuUsage;
    }

    public boolean isAgentEnabled() {
        return agentEnabled;
    }

    public void setAgentEnabled(boolean agentEnabled) {
        this.agentEnabled = agentEnabled;
    }

    public boolean isDetectEnabled() {
        return detectEnabled;
    }

    public void setDetectEnabled(boolean detectEnabled) {
        this.detectEnabled = detectEnabled;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
