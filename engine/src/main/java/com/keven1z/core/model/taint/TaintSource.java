package com.keven1z.core.model.taint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keven1z.core.consts.SourceType;

@JsonDeserialize(builder = TaintSource.Builder.class)
public class TaintSource extends TaintData {
    private final SourceType sourceType;

    private TaintSource(Builder builder) {
        super(builder);
        this.sourceType = builder.sourceType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public static class Builder extends TaintData.Builder<Builder> {
        @JsonProperty("sourceType")
        private SourceType sourceType;

        public Builder(){
            super();
        }

        public Builder sourceType(SourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public TaintSource build() {
            return new TaintSource(this);
        }
    }
}
