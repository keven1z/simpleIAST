package com.keven1z.core.policy;

import com.keven1z.core.consts.PolicyType;

import java.util.ArrayList;
import java.util.List;

public class HookPolicyContainer {
    /**
     * 预估策略集合容量
     */
    private static final int INITIAL_CAPACITY = 80;
    /**
     * 污染源策略集合
     */
    private final List<HookPolicy> source = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 污染传播策略集合
     */
    private final List<HookPolicy> propagation = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 污染汇聚点策略集合
     */
    private final List<HookPolicy> sink = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 流量监控策略集合
     */
    private final List<HookPolicy> http = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 过滤点策略集合
     */
    private final List<HookPolicy> sanitizers = new ArrayList<>(INITIAL_CAPACITY);
    private final List<HookPolicy> singles = new ArrayList<>(INITIAL_CAPACITY);

    /**
     * 接口类策略集合
     */
    private final List<HookPolicy> interfaceHookPolicy = new ArrayList<>(INITIAL_CAPACITY);

    public List<HookPolicy> getSource() {
        return source;
    }

    public List<HookPolicy> getPropagation() {
        return this.propagation;
    }

    public List<HookPolicy> getSink() {
        return this.sink;
    }

    public List<HookPolicy> getHttp() {
        return this.http;
    }

    public List<HookPolicy> getSanitizers() {
        return sanitizers;
    }

    public List<HookPolicy> getSingles() {
        return singles;
    }

    public void addPolicy(HookPolicy hookPolicy) {
        PolicyType policyType = hookPolicy.getType();
        //如果为接口，直接加入接口策略中
        if (hookPolicy.getInter()) {
            this.interfaceHookPolicy.add(hookPolicy);
        }

        if (PolicyType.SOURCE.equals(policyType)) {
            this.source.add(hookPolicy);
        } else if (PolicyType.PROPAGATION.equals(policyType)) {
            this.propagation.add(hookPolicy);
        } else if (PolicyType.SINK.equals(policyType)) {
            this.sink.add(hookPolicy);
        } else if (PolicyType.HTTP.equals(policyType)) {
            this.http.add(hookPolicy);
        } else if (PolicyType.SANITIZER.equals(policyType)) {
            this.sanitizers.add(hookPolicy);
        } else if (PolicyType.SINGLE.equals(policyType)) {
            this.singles.add(hookPolicy);
        }
    }

    /**
     * @return hook点真实的数量（不包含接口hook点数量）
     */
    public int getPolicySize() {
        return this.getSink().size() + this.getSource().size() + this.getPropagation().size() + this.getSanitizers().size();
    }

    public List<HookPolicy> getTaintPolicy() {
        ArrayList<HookPolicy> policies = new ArrayList<>();
        policies.addAll(this.getSource());
        policies.addAll(this.getPropagation());
        policies.addAll(this.getSanitizers());
        policies.addAll(this.getSink());
        return policies;
    }

    /**
     * 清空策略
     */
    public void clear() {
        this.sanitizers.clear();
        this.http.clear();
        this.sink.clear();
        this.propagation.clear();
        this.sanitizers.clear();
        this.getInterfacePolicy().clear();
    }

    public List<HookPolicy> getInterfacePolicy() {
        return interfaceHookPolicy;
    }
}
