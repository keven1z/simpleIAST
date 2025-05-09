package com.keven1z.core.model.taint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keven1z.core.consts.VulnerabilityType;
@JsonDeserialize(builder = TaintSink.Builder.class)
public class TaintSink extends TaintData {
    private VulnerabilityType vulnerabilityType;

    // 私有构造函数，通过 Builder 创建对象
    private TaintSink(Builder builder) {
        super(builder);  // 调用父类 TaintData 的构造函数
        this.vulnerabilityType = builder.vulnerabilityType;
    }

    // 获取漏洞类型
    public VulnerabilityType getVulnerabilityType() {
        return vulnerabilityType;
    }

    // 设置漏洞类型
    public void setVulnerabilityType(VulnerabilityType vulnerabilityType) {
        this.vulnerabilityType = vulnerabilityType;
    }


    // Builder 类，帮助构建 SinkInfo 对象
    public static class Builder extends TaintData.Builder<Builder> {
        private VulnerabilityType vulnerabilityType;

        public Builder() {
            super();
        }

        // 设置漏洞类型
        public Builder vulnerabilityType(VulnerabilityType vulnerabilityType) {
            this.vulnerabilityType = vulnerabilityType;
            return this;
        }

        // 构建并返回 SinkInfo 对象
        public TaintSink build() {
            return new TaintSink(this);
        }
    }

}

