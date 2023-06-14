package com.keven1z.core.policy;

import com.keven1z.core.log.LogTool;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolicyContainer {
    private List<Policy> source = new ArrayList<>();
    private List<Policy> propagation = new ArrayList<>();
    private List<Policy> sink = new ArrayList<>();
    private List<Policy> http = new ArrayList<>();
    private List<Policy> sanitizers = new ArrayList<>();

    public List<Policy> getSource() {
        return source;
    }

    public void setSource(List<Policy> source) {
        this.source = source;
    }

    public List<Policy> getPropagation() {
        return propagation;
    }

    public void setPropagation(List<Policy> propagation) {
        this.propagation = propagation;
    }

    public List<Policy> getSink() {
        return sink;
    }

    public void setSink(List<Policy> sink) {
        this.sink = sink;
    }

    public List<Policy> getHttp() {
        return http;
    }

    public void setHttp(List<Policy> http) {
        this.http = http;
    }

    public List<Policy> getSanitizers() {
        return sanitizers;
    }

    public void setSanitizers(List<Policy> sanitizers) {
        this.sanitizers = sanitizers;
    }

    private final Logger logger = Logger.getLogger(getClass().getPackage().getName());

    public void addPolicy(Policy policy) {
        PolicyTypeEnum policyType = policy.getType();
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
        }

        if (LogTool.isDebugEnabled()) {
            logger.info("add policy:" + policy);
        }
    }

    public List<Policy> getAllPolicies() {
        List<Policy> hookPolicies = new ArrayList<>();
        hookPolicies.addAll(Collections.unmodifiableList(this.getSink()));
        hookPolicies.addAll(Collections.unmodifiableList(this.getSink()));
        hookPolicies.addAll(Collections.unmodifiableList(this.getHttp()));
        hookPolicies.addAll(Collections.unmodifiableList(this.getPropagation()));
        hookPolicies.addAll(Collections.unmodifiableList(this.getSource()));
        hookPolicies.addAll(Collections.unmodifiableList(this.getSanitizers()));
        return hookPolicies;
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
    }
}
