package com.keven1z.core.policy;

import java.util.List;

/**
 * IAST Hook配置
 * @author keven1z
 * @date 2025/6/8
 */
public class IastHookConfig {
    /**
     * 版本号
     */
    private String version;
    /**
     * hook配置列表
     */
    private List<ClassHookConfig> hooks;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ClassHookConfig> getHooks() {
        return hooks;
    }

    public void setHooks(List<ClassHookConfig> hooks) {
        this.hooks = hooks;
    }
}
