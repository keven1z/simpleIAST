package com.keven1z.core.policy;

import java.util.List;
import java.util.Map;

/**
 * hook方法
 * @author keven1z
 * @date 2025/6/8
 */
public class MethodHookConfig {
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法描述
     */
    private String desc;
    /**
     * 是否启用
     */
    private boolean state;
    /**
     * 钩子位置
     */
    private HookPosition hookPositions;
    /**
     * 污点追踪
     */
    private TaintTracking taintTracking;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public HookPosition getHookPositions() {
        return hookPositions;
    }

    public void setHookPositions(HookPosition hookPositions) {
        this.hookPositions = hookPositions;
    }

    public TaintTracking getTaintTracking() {
        return taintTracking;
    }

    public void setTaintTracking(TaintTracking taintTracking) {
        this.taintTracking = taintTracking;
    }

    @Override
    public String toString() {
        return this.name + this.desc;
    }

    /**
     * Hook位置class
     * @author keven1z
     */
    public static class HookPosition {
        private boolean entry;
        private boolean exit;

        public boolean isEntry() {
            return entry;
        }

        public void setEntry(boolean entry) {
            this.entry = entry;
        }

        public boolean isExit() {
            return exit;
        }

        public void setExit(boolean exit) {
            this.exit = exit;
        }
    }

    public static class TaintTracking {
        private boolean source;
        private boolean propagator;
        private boolean sink;
        private boolean http;
        private List<String> vulnerabilityTypes;
        private Map<String, String> cleaningEffect;
        private TrackingDirection trackingDirection;
        private String httpStage;

        /**
         * 污染源类型(仅污染源)
         */
        private String sourceType;

        public boolean isSource() {
            return source;
        }

        public void setSource(boolean source) {
            this.source = source;
        }

        public boolean isPropagator() {
            return propagator;
        }

        public void setPropagator(boolean propagator) {
            this.propagator = propagator;
        }

        public boolean isSink() {
            return sink;
        }

        public void setSink(boolean sink) {
            this.sink = sink;
        }

        public boolean isHttp() {
            return http;
        }

        public void setHttp(boolean http) {
            this.http = http;
        }
        public List<String> getVulnerabilityTypes() {
            return vulnerabilityTypes;
        }

        public void setVulnerabilityTypes(List<String> vulnerabilityTypes) {
            this.vulnerabilityTypes = vulnerabilityTypes;
        }

        public Map<String, String> getCleaningEffect() {
            return cleaningEffect;
        }

        public void setCleaningEffect(Map<String, String> cleaningEffect) {
            this.cleaningEffect = cleaningEffect;
        }

        public TrackingDirection getTrackingDirection() {
            return trackingDirection;
        }

        public void setTrackingDirection(TrackingDirection trackingDirection) {
            this.trackingDirection = trackingDirection;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getHttpStage() {
            return httpStage;
        }

        public void setHttpStage(String httpStage) {
            this.httpStage = httpStage;
        }
    }
    public static class TrackingDirection {
        private String from;
        private String to;
        public TrackingDirection(String from, String to) {
            this.from = from;
            this.to = to;
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
    }
}

