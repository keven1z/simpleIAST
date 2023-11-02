package com.keven1z.core.policy;

import java.util.ArrayList;
import java.util.List;

public class PolicyContainer {
    /**
     * 预估策略集合容量
     */
    private static final int INITIAL_CAPACITY = 80;
    /**
     * 污染源策略集合
     */
    private final List<Policy> source = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 污染传播策略集合
     */
    private final List<Policy> propagation = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 污染汇聚点策略集合
     */
    private final List<Policy> sink = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 流量监控策略集合
     */
    private final List<Policy> http = new ArrayList<>(INITIAL_CAPACITY);
    /**
     * 过滤点策略集合
     */
    private final List<Policy> sanitizers = new ArrayList<>(INITIAL_CAPACITY);
    private final List<Policy> singles = new ArrayList<>(INITIAL_CAPACITY);

    /**
     * 接口类策略集合
     */
    private final List<Policy> interfacePolicy = new ArrayList<>(INITIAL_CAPACITY);

    public List<Policy> getSource() {
        return source;
    }

    public List<Policy> getPropagation() {
        return this.propagation;
    }

    public List<Policy> getSink() {
        return this.sink;
    }

    public List<Policy> getHttp() {
        return this.http;
    }

    public List<Policy> getSanitizers() {
        return sanitizers;
    }

    public List<Policy> getSingles() {
        return singles;
    }

    public void addPolicy(Policy policy) {
        PolicyTypeEnum policyType = policy.getType();
        //如果为接口，直接加入接口策略中
        if (policy.getInter()) {
            this.interfacePolicy.add(policy);
        }

        if (PolicyTypeEnum.SOURCE.equals(policyType)) {
            this.source.add(policy);
        } else if (PolicyTypeEnum.PROPAGATION.equals(policyType)) {
            this.propagation.add(policy);
        } else if (PolicyTypeEnum.SINK.equals(policyType)) {
            this.sink.add(policy);
        } else if (PolicyTypeEnum.HTTP.equals(policyType)) {
            this.http.add(policy);
        } else if (PolicyTypeEnum.SANITIZER.equals(policyType)) {
            this.sanitizers.add(policy);
        } else if (PolicyTypeEnum.SINGLE.equals(policyType)) {
            this.singles.add(policy);
        }
    }

    /**
     * @return hook点真实的数量（不包含接口hook点数量）
     */
    public int getPolicySize() {
        return this.getSink().size() + this.getSource().size() + this.getPropagation().size() + this.getSanitizers().size();
    }

    public List<Policy> getTaintPolicy() {
        ArrayList<Policy> policies = new ArrayList<>();
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

    public List<Policy> getInterfacePolicy() {
        return interfacePolicy;
    }
}
