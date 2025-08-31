package com.keven1z.core.model;

import com.keven1z.core.hook.taint.TaintSpy;
import com.keven1z.core.policy.IastHookConfig;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;


/**
 * Agent上下文对象
 */
public class IASTContext {
    private IastHookConfig iastHookConfig;

    private IASTContext() {
        userClasses = new HashSet<>();
    }
    private String agentVersion;
    public static IASTContext getContext() {
        return Inner.context;
    }

    private static class Inner {
        private static final IASTContext context = new IASTContext();
    }

    private Instrumentation instrumentation;
    private Set<String> blackList;
    /**
     * 与服务端通信的认证token
     */
    private String token;
    /**
     * agent绑定的项目名称
     */
    private String bindProjectName;
    /**
     * 是否记录详细日志
     */
    private Boolean enableDetailedLogging;
    /*
     * 服务器端url
     */
    private String serverUrl;
    private final Set<String> userClasses;

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public String getBindProjectName() {
        return bindProjectName;
    }

    public void setBindProjectName(String bindProjectName) {
        this.bindProjectName = bindProjectName;
    }

    public boolean isOfflineEnabled() {
        return Config.getConfig().isOfflineStart();
    }

    public void clear() {
        TaintSpy.getInstance().clear();
        ApplicationModel.clear();
        userClasses.clear();
    }

    public void setBlackList(Set<String> blackList) {
        this.blackList = blackList;
    }

    /**
     * 判定hook点是否在黑名单中
     */
    public boolean isClassNameBlacklisted(String className) {
        if (blackList == null || blackList.isEmpty()) {
            // 如果黑名单为空，则认为不在黑名单中
            return false;
        }

        int classNameLength = className.length();
        for (String blackStr : blackList) {
            int blackStrLength = blackStr.length();
            if (classNameLength >= blackStrLength &&
                    (className.startsWith(blackStr) || className.endsWith(blackStr))) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getUserClasses() {
        return userClasses;
    }

    public IastHookConfig getIastHookConfig() {
        return iastHookConfig;
    }

    public void addUserClass(String userClasses) {
        this.userClasses.add(userClasses);
    }

    public boolean isEnableDetailedLogging() {
        return enableDetailedLogging;
    }

    public void setEnableDetailedLogging(Boolean enableDetailedLogging) {
        if (enableDetailedLogging == null) {
            this.enableDetailedLogging = Config.getConfig().isEnableDetailedLogging();
        }else {
            this.enableDetailedLogging = enableDetailedLogging;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getBlackList() {
        return blackList;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }
    public void setHookConfig(IastHookConfig iastHookConfig) {
        this.iastHookConfig = iastHookConfig;
    }
}
