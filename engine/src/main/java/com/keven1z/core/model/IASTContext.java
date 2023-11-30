package com.keven1z.core.model;

import com.keven1z.core.Config;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.policy.PolicyContainer;

import java.lang.instrument.Instrumentation;
import java.util.List;


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
    private String token;
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
    public boolean isInBlackList(String className) {
        if (blackList == null || className == null) {
            return false;
        }
        for (String blackStr : blackList) {
            if (className.startsWith(blackStr) || className.endsWith(blackStr)) {
                return true;
            }
        }
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
