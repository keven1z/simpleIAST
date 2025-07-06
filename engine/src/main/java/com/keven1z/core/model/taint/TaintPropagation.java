package com.keven1z.core.model.taint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keven1z.core.consts.CleaningMode;

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(builder = TaintPropagation.Builder.class)
public class TaintPropagation extends TaintData {
    private Map<String,CleaningMode> cleaningEffects;
    // 私有构造函数，通过 Builder 创建对象
    private TaintPropagation(Builder builder) {
        super(builder);  // 调用父类 TaintData 的构造函数
    }
    public void addCleaningEffects(String key, CleaningMode cleaningMode) {
        if (cleaningEffects == null){
            cleaningEffects = new HashMap<>(4);
        }
        this.cleaningEffects.put(key, cleaningMode);
    }
    public Map<String, CleaningMode> getCleaningEffects() {
        return cleaningEffects;
    }

    public void setCleaningEffects(Map<String, CleaningMode> cleaningEffects) {
        this.cleaningEffects = cleaningEffects;
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
}

