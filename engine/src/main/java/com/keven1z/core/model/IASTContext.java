package com.keven1z.core.model;

import com.keven1z.core.consts.CommonConst;
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
    /**
     * 启动模式
     */
    private String mode;

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isOfflineEnabled() {
        return CommonConst.MODE_OFFLINE.equals(getMode());
    }

    public void clear() {
        if (this.policyContainer != null) {
            this.policyContainer.clear();
        }
        TaintSpy.clear();
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
}
