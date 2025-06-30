package com.keven1z.core.model.taint;

import java.util.ArrayList;
import java.util.List;

public class TaintSanitation extends TaintData {
    private final List<String> conditions;

    // 私有构造函数，通过 Builder 创建对象
    private TaintSanitation(Builder builder) {
        super(builder);  // 调用父类 TaintData 的构造函数
        this.conditions = builder.conditions;
    }


    // 获取 conditions
    public List<String> getConditions() {
        return conditions;
    }

    // Builder 类，帮助构建 SanitationInfo 对象
    public static class Builder extends TaintData.Builder<Builder> {
        private List<String> conditions = new ArrayList<>();  // 默认空的条件列表

        public Builder() {
            super();
        }

        // 设置 conditions
        public Builder conditions(List<String> conditions) {
            this.conditions = conditions;
            return this;
        }

        // 添加单个条件
        public Builder addCondition(String condition) {
            this.conditions.add(condition);
            return this;
        }

        // 构建并返回 SanitationInfo 对象
        public TaintSanitation build() {
            return new TaintSanitation(this);
        }
    }
}

