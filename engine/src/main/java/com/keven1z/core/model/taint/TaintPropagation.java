package com.keven1z.core.model.taint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
@JsonDeserialize(builder = TaintPropagation.Builder.class)
public class TaintPropagation extends TaintData {

    // 私有构造函数，通过 Builder 创建对象
    private TaintPropagation(Builder builder) {
        super(builder);  // 调用父类 TaintData 的构造函数
    }

    // Builder 类，帮助构建 PropagationInfo 对象
    public static class Builder extends TaintData.Builder<Builder> {
        public Builder() {
            super();
        }

        // 构建并返回 PropagationInfo 对象
        public TaintPropagation build() {
            return new TaintPropagation(this);
        }
    }

    // FlowPath 类定义，表示数据流路径

}

