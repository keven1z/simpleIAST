package com.keven1z.core.model.taint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keven1z.core.consts.PolicyType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.keven1z.core.hook.HookThreadLocal.INVOKE_ID;

/**
 * @author keven1z
 * @created 2021/6/11
 */
@JsonDeserialize(builder = TaintData.Builder.class)
public class TaintData {
    private final int invokeId;
    private final String className;
    private final String method;
    private final String desc;
    private final String name;
    private final WeakReference<Object> thisObject;
    private final WeakReference<Object> returnObject;
    private final WeakReference<Object[]> parameters;
    private final List<String> stackList;
    private final List<FlowPath> flowPaths;
    private PolicyType stage;
    protected TaintData(Builder<?> builder) {
        this.invokeId = INVOKE_ID.getAndIncrement();
        this.className = builder.className;
        this.method = builder.method;
        this.desc = builder.desc;
        this.name = builder.name;
        this.thisObject = builder.thisObject;
        this.returnObject = builder.returnObject;
        this.parameters = builder.parameters;
        this.stackList = builder.stackList;
        this.flowPaths = builder.flowPaths;
        this.stage = builder.stage;
    }

    public int getInvokeId() {
        return invokeId;
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

    public String getName() {
        return name;
    }
    @JsonIgnore
    public Object getThisObject() {
        if (thisObject == null) {
            return null;
        }
        return thisObject.get();
    }

    public PolicyType getStage() {
        return stage;
    }

    @JsonIgnore
    public Object getReturnObject() {
        if (returnObject == null) {
            return null;
        }
        return returnObject.get();
    }
    @JsonIgnore
    public Object[] getParameters() {
        if (parameters == null) return null;
        return parameters.get();
    }

    public List<String> getStackList() {
        return stackList;
    }

    @Override
    public String toString() {
        return "TaintData{" +
                "invokeId=" + invokeId +
                ", className='" + className + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

    public List<FlowPath> getFlowPaths() {
        return flowPaths;
    }

    public static class FlowPath {
        @JsonProperty("fromObject")
        private final String fromObject;
        @JsonProperty("toObject")
        private final String toObject;
        @JsonIgnore
        private final int hashcode;

        // 构造函数
        public FlowPath(Object fromObject, Object toObject) {
            this.fromObject = fromObject.toString();
            if (toObject != null) {
                this.hashcode = System.identityHashCode(toObject); // 使用值的身份哈希码
                this.toObject = toObject.toString();
            } else {
                this.hashcode = -1;
                this.toObject = null;
            }
        }

        // 获取来源对象
        public Object getFromObject() {
            return fromObject;
        }

        // 获取目标对象
        public Object getToObject() {
            return toObject;
        }

        // 获取哈希码
        public int getHashcode() {
            return hashcode;
        }
    }

    public static class Builder<T extends Builder<T>> {
        @JsonProperty("className")
        private String className;
        @JsonProperty("method")
        private String method;
        @JsonProperty("desc")
        private String desc;
        @JsonProperty("name")
        private String name;
        @JsonIgnore
        private WeakReference<Object> thisObject;
        @JsonIgnore
        private WeakReference<Object> returnObject;
        @JsonIgnore
        private WeakReference<Object[]> parameters;
        @JsonProperty("stackList")
        private List<String> stackList;
        @JsonProperty("flowPaths")
        private List<FlowPath> flowPaths;
        @JsonProperty("stage")
        private PolicyType stage ;
        public Builder() {
        }

        public T className(String className) {
            this.className = className;
            return self();
        }

        public T method(String method) {
            this.method = method;
            return self();
        }

        public T desc(String desc) {
            this.desc = desc;
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T thisObject(Object thisObject) {
            this.thisObject = new WeakReference<>(thisObject);
            return self();
        }

        public T returnObject(Object returnObject) {
            this.returnObject = new WeakReference<>(returnObject);
            return self();
        }

        public T parameters(Object[] parameters) {
            this.parameters = new WeakReference<>(parameters);
            return self();
        }

        public T stackList(List<String> stackList) {
            this.stackList = stackList;
            return self();
        }
        public T stage(PolicyType stage) {
            this.stage = stage;
            return self();
        }

        public T flowPaths(List<FlowPath> flowPaths) {
            this.flowPaths = flowPaths;
            return self();
        }

        // 添加单个 flowPath
        public T addFlowPath(FlowPath flowPath) {
            if (this.flowPaths == null) {
                this.flowPaths = new ArrayList<>();
            }
            this.flowPaths.add(flowPath);
            return self();
        }

        // 返回子类的实例，确保子类 Builder 继续使用
        protected T self() {
            return (T) this;
        }

        public TaintData build() {
            return new TaintData(this);
        }
    }
}


