package com.keven1z.core.model.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.TaintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.keven1z.core.hook.HookThreadLocal.INVOKE_ID;

/**
 * @author keven1z
 * @since 2021/6/11
 * @Description hook类的信息
 */
public class TaintData {
    /**
     * 污点类调用的唯一标志
     */
    @JsonIgnore
    private final int invokeId;
    private String className;
    private String method;
    private String desc;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private Object thisObject;
    @JsonIgnore
    private Object toObject;
    @JsonIgnore
    private List<Integer> toObjectHashCode;
    /**
     * 该hook点所属的阶段，污染源、传播、污染汇聚点等阶段
     */
    private String stage;
    /**
     * 若是漏洞，标记其漏洞名称
     */
    @JsonIgnore
    private String vulnType;
    /**
     * 过滤节点信息表
     */
    private List<TaintData> sanitizerNodes;

    @JsonIgnore
    private Object fromObject;

    /**
     * 污点进入的值
     */
    private String fromValue;
    /**
     * 污染传出的值
     */
    private String toValue;

    /**
     * 返回对象
     */
    @JsonIgnore
    private Object returnObject;
    /**
     * 过滤条件值
     */
    private String conditions;
    @JsonIgnore
    private Object[] parameters;
    /**
     * 是否进行过滤处理
     */
    @JsonIgnore
    private boolean hasSanitizer;
    private List<String> stackList;

    public TaintData(String className, String method, String desc, PolicyTypeEnum policyType) {
        this.className = className;
        this.method = method;
        this.desc = desc;
        this.stage = policyType.name();
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
        this.toValue = toValue;
    }

    public List<Integer> getToObjectHashCode() {
        return toObjectHashCode;
    }

    public void setToObjectHashCode(Integer toObjectHashCode) {
        if (this.toObjectHashCode == null) {
            this.toObjectHashCode = new ArrayList<>(5);
        }
        this.toObjectHashCode.add(toObjectHashCode);
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getVulnType() {
        return vulnType;
    }

    public void setVulnType(String vulnType) {
        this.vulnType = vulnType;
    }


    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public boolean isHasSanitizer() {
        return hasSanitizer;
    }

    public void setHasSanitizer(boolean hasSanitizer) {
        this.hasSanitizer = hasSanitizer;
    }

    public List<TaintData> getSanitizerNodes() {
        return sanitizerNodes;
    }

    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        if (fromValue == null) {
            return;
        }

        if (PolicyTypeEnum.SINK.name().equals(this.stage)) {
            this.fromValue = fromValue;
        } else {
            this.fromValue = TaintUtils.format(fromValue);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getThisObject() {
        return thisObject;
    }

    public void setThisObject(Object thisObject) {
        this.thisObject = thisObject;
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
                ", " + stage +
                '}';
    }

    public List<String> getStackList() {
        return stackList;
    }

    public void setStackList(List<String> stackList) {
        this.stackList = stackList;
    }

    public synchronized void addSanitizer(List<TaintData> taintData) {
        this.sanitizerNodes = taintData;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }


    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getToObject() {
        return toObject;
    }

    public Object getFromObject() {
        return fromObject;
    }

    public void setFromObject(Object fromObject) {
        this.fromObject = fromObject;
    }

    public void setToObject(Object toObject) {
        this.toObject = toObject;
        this.setToObjectHashCode(System.identityHashCode(toObject));
        this.setToValue(toObject.toString());
    }

    public void clear() {
        if (this.stackList != null) {
            this.stackList.clear();
        }
    }
}
