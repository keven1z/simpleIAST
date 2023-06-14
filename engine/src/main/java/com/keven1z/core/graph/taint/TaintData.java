package com.keven1z.core.graph.taint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.TaintUtils;

import java.util.List;
import java.util.Objects;

import static com.keven1z.core.hook.spy.HookThreadLocal.INVOKE_ID;

/**
 * @author keven1z
 * @Date 2021/6/11
 * @Description hook类的信息
 */
public class TaintData {
    /**
     * 污点类调用的唯一标志
     */
    private final int invokeId;
    private String className;
    private String method;
    private String desc;

    /**
     * 污点值的类型
     */
    private String taintValueType;
    @JsonIgnore
    private int fromObjectHashCode;
    @JsonIgnore
    private int toObjectHashCode;
    /**
     * 该hook点所属的阶段，污染源、传播、污染汇聚点等阶段
     */
    private PolicyTypeEnum type;
    /**
     * 若是漏洞，标记其漏洞名称
     */
    private String VulnType;
    /**
     * 过滤节点信息表
     */
    private List<TaintData> sanitizerNodes;
    /**
     * 污点进入的值
     */
    private String fromValue;
    /**
     * 污染传出的值
     */
    private String toValue;
    /**
     * 返回值
     */
    private String returnValue;
    /**
     * 过滤条件值
     */
    private String conditions;

    /**
     * 是否进行过滤处理
     */
    private boolean isSanitizer;

    public TaintData(String className, String method, String desc) {
        this.className = className;
        this.method = method;
        this.desc = desc;
        this.invokeId = INVOKE_ID.getAndIncrement();
    }

    public int getInvokeId() {
        return invokeId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getToValue() {
        return toValue;
    }

    public void setToValue(String toValue) {
        this.toValue = TaintUtils.format(toValue);
    }

    public int getFromObjectHashCode() {
        return fromObjectHashCode;
    }

    public void setFromObjectHashCode(int fromObjectHashCode) {
        this.fromObjectHashCode = fromObjectHashCode;
    }

    public int getToObjectHashCode() {
        return toObjectHashCode;
    }

    public void setToObjectHashCode(int toObjectHashCode) {
        this.toObjectHashCode = toObjectHashCode;
    }

    public PolicyTypeEnum getType() {
        return type;
    }

    public void setType(PolicyTypeEnum type) {
        this.type = type;
    }

    public String getVulnType() {
        return VulnType;
    }

    public void setVulnType(String vulnType) {
        VulnType = vulnType;
    }


    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = TaintUtils.format(returnValue);
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public boolean isSanitizer() {
        return isSanitizer;
    }

    public void setSanitizer(boolean sanitizer) {
        isSanitizer = sanitizer;
    }

    public List<TaintData> getSanitizerNodes() {
        return sanitizerNodes;
    }

    public void setSanitizerNodes(List<TaintData> sanitizerNodes) {
        this.sanitizerNodes = sanitizerNodes;
    }

    public String getTaintValueType() {
        return taintValueType;
    }

    public void setTaintValueType(String taintValueType) {
        this.taintValueType = taintValueType;
    }

    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        if (PolicyTypeEnum.SINK.equals(this.type)) {
            this.fromValue = fromValue;
        } else {
            this.fromValue = TaintUtils.format(fromValue);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaintData taintData = (TaintData) o;
        return invokeId == taintData.invokeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(invokeId);
    }

    @Override
    public String toString() {
        return "TaintData{" +
                "invokeId=" + invokeId +
                ", sign='" + className + "." + method +
                ", " + type +
                '}';
    }


    public synchronized void addSanitizer(List<TaintData> taintData) {
        this.sanitizerNodes = taintData;
    }

}
