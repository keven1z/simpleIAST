package com.keven1z.core.policy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author keven1z
 * @date 2023/02/22
 */
public class Policy implements Serializable {
    public Policy(String className, String method, String desc) {
        this.className = className;
        this.method = method;
        this.desc = desc;
    }

    public Policy() {

    }

    /**
     * 策略名称
     */
    private String name;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String method;
    /**
     * 描述名
     */
    private String desc;
    /**
     * 是否hook方法进入
     */
    private int enter;
    /**
     * 是否hook方法退出
     */
    private int exit;
    /**
     * 是否开启
     */
    private int state;
    /**
     * 污点来自
     */
    private String from;
    /**
     * 污点去向
     */
    private String to;
    private boolean isHooked;
    /**
     * 策略类型
     */
    private PolicyTypeEnum type;
    private boolean inter;
    private String conditions;
    private Set<String> exclude = new HashSet<>();
    /*
     * 该hook点是否为可传播的，存在某些单一hook点为漏洞
     */
    private boolean requireHttp = true;
    /**
     * 是否是bean hook点
     */
    private boolean isBeanHook = false;
    /**
     * 某些类通过其接口hook类被添加为hook点，originClassName为其接口类名
     */
    private String originClassName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getEnter() {
        return enter;
    }

    public int getExit() {
        return exit;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public void setExit(int exit) {
        this.exit = exit;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(PolicyTypeEnum type) {
        this.type = type;
    }

    public PolicyTypeEnum getType() {
        return type;
    }

    public boolean getInter() {
        return inter;
    }

    public void setInter(boolean inter) {
        this.inter = inter;
    }

    public boolean isHooked() {
        return isHooked;
    }

    public void setHooked(boolean hooked) {
        isHooked = hooked;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "Policy{ type=" + type +
                ",className='" + className + '\'' +
                ", method='" + method + '\'' +
                ", desc='" + desc + '\'' + "}";
    }

    public String getOriginClassName() {
        return originClassName;
    }

    public void setOriginClassName(String originClassName) {
        this.originClassName = originClassName;
    }

    public Set<String> getExclude() {
        return exclude;
    }

    public void setExclude(Set<String> exclude) {
        this.exclude = exclude;
    }

    public boolean isRequireHttp() {
        return requireHttp;
    }

    public void setRequireHttp(boolean requireHttp) {
        this.requireHttp = requireHttp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return className.equals(policy.className) && method.equals(policy.method) && desc.equals(policy.desc);
    }

    public boolean isBeanHook() {
        return isBeanHook;
    }

    public void setBeanHook(boolean beanHook) {
        isBeanHook = beanHook;
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, method, desc);
    }
}
