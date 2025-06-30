package com.keven1z.core.policy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keven1z.core.consts.PolicyType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author keven1z
 * @since 2023/02/22
 */
@JsonDeserialize(builder = HookPolicy.Builder.class)
public class HookPolicy implements Serializable, Comparable<HookPolicy> {

    public static Builder builder() {
        return new Builder();
    }
    public HookPolicy(){
    }
    private HookPolicy(Builder builder) {
        this.name = builder.name;
        this.className = builder.className;
        this.method = builder.method;
        this.desc = builder.desc;
        this.enter = builder.enter;
        this.exit = builder.exit;
        this.state = builder.state;
        this.from = builder.from;
        this.to = builder.to;
        this.isHooked = builder.isHooked;
        this.type = builder.type;
        this.inter = builder.inter;
        this.conditions = builder.conditions;
        this.exclude = builder.exclude;
        this.requireHttp = builder.requireHttp;
        this.originClassName = builder.originClassName;
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
    private PolicyType type;
    private boolean inter;
    private String conditions;
    private Set<String> exclude = new HashSet<>();
    /*
     * 该hook点是否为可传播的，存在某些单一hook点为漏洞
     */
    private boolean requireHttp = true;

    /**
     * 某些类通过其接口hook类被添加为hook点，originClassName为其接口类名
     */
    private  String originClassName;

    public void setType(PolicyType policyType) {
        this.type = policyType;
    }

    public static class Builder {
        @JsonProperty("name")
        private String name;
        @JsonProperty("className")
        private String className;
        @JsonProperty("method")
        private String method;
        @JsonProperty("desc")
        private String desc;
        @JsonProperty("enter")
        private int enter;
        @JsonProperty("exit")
        private int exit;
        @JsonProperty("state")
        private int state;
        @JsonProperty("from")
        private String from;
        @JsonProperty("to")
        private String to;
        @JsonProperty("isHooked")
        private boolean isHooked;
        @JsonProperty("type")
        private PolicyType type;
        @JsonProperty("inter")
        private boolean inter;
        @JsonProperty("conditions")
        private String conditions;
        @JsonProperty("exclude")
        private Set<String> exclude = new HashSet<>();
        @JsonProperty("requireHttp")
        private boolean requireHttp = true;
        @JsonProperty("originClassName")
        private String originClassName;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder enter(int enter) {
            this.enter = enter;
            return this;
        }

        public Builder exit(int exit) {
            this.exit = exit;
            return this;
        }

        public Builder state(int state) {
            this.state = state;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder isHooked(boolean isHooked) {
            this.isHooked = isHooked;
            return this;
        }

        public Builder type(PolicyType type) {
            this.type = type;
            return this;
        }

        public Builder inter(boolean inter) {
            this.inter = inter;
            return this;
        }

        public Builder conditions(String conditions) {
            this.conditions = conditions;
            return this;
        }

        public Builder exclude(Set<String> exclude) {
            this.exclude = exclude;
            return this;
        }

        public Builder requireHttp(boolean requireHttp) {
            this.requireHttp = requireHttp;
            return this;
        }

        public Builder originClassName(String originClassName) {
            this.originClassName = originClassName;
            return this;
        }

        public HookPolicy build() {
            return new HookPolicy(this);
        }
    }

    public void setHooked(boolean hooked) {
        isHooked = hooked;
    }

    public String getName() {
        return name;
    }


    public String getClassName() {
        return className;
    }


    public String getMethod() {
        return method;
    }


    public String getDesc() {
        return desc;
    }


    public int getEnter() {
        return enter;
    }

    public int getExit() {
        return exit;
    }


    public int getState() {
        return state;
    }


    public String getFrom() {
        return from;
    }


    public String getTo() {
        return to;
    }


    public PolicyType getType() {
        return type;
    }

    public boolean getInter() {
        return inter;
    }


    public boolean isHooked() {
        return isHooked;
    }


    public String getConditions() {
        return conditions;
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

    public Set<String> getExclude() {
        return exclude;
    }

    public boolean isRequireHttp() {
        return requireHttp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HookPolicy hookPolicy = (HookPolicy) o;
        return className.equals(hookPolicy.className) && method.equals(hookPolicy.method) && desc.equals(hookPolicy.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, method, desc);
    }

    @Override
    public int compareTo(HookPolicy other) {
        int classNameComparison = this.className.compareTo(other.className);
        if (classNameComparison != 0) {
            return classNameComparison;
        }
        int methodComparison = this.method.compareTo(other.method);
        if (methodComparison != 0) {
            return methodComparison;
        }

        return this.desc.compareTo(other.desc);
    }
}
