package com.keven1z.core.model;

import com.keven1z.core.Config;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.policy.PolicyContainer;

import java.lang.instrument.Instrumentation;
import java.util.List;


/**
 * Agent上下文对象
 */
public class IASTContext {
    private IASTContext() {
    }

    public static IASTContext getContext() {
        return Inner.context;
    }

    private static class Inner {
        private static final IASTContext context = new IASTContext();
    }

    /**
     * 策略
     */
    private PolicyContainer policyContainer;

    private Instrumentation instrumentation;
    private List<String> blackList;
    /**
     * 与服务端通信的认证token
     */
    private String token;
    /**
     * agent绑定的应用名称
     */
    private String bindApplicationName;
    /**
     * 是否启动debug日志
     */
    private boolean isDebug;
    /*
     * 服务器端url
     */
    private String serverUrl;

    public PolicyContainer getPolicy() {
        return policyContainer;
    }

    public void setPolicy(PolicyContainer policyContainer) {
        this.policyContainer = policyContainer;
    }

    public PolicyContainer getPolicyContainer() {
        return policyContainer;
    }

    public void setPolicyContainer(PolicyContainer policyContainer) {
        this.policyContainer = policyContainer;
    }

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public String getBindApplicationName() {
        return bindApplicationName;
    }

    public void setBindApplicationName(String bindApplicationName) {
        this.bindApplicationName = bindApplicationName;
    }

    public boolean isOfflineEnabled() {
        return Config.getConfig().isDebug();
    }

    public void clear() {
        if (this.policyContainer != null) {
            this.policyContainer.clear();
        }
        TaintSpy.getInstance().clear();
        ApplicationModel.clear();
    }

    public void setBlackList(List<String> blackList) {
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


    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
