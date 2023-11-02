package com.keven1z.core.model.graph;

import java.util.Objects;

/**
 * 边的信息
 * @author binbin.hou
 * @since 0.0.2
 */
public class TaintEdge {

    /**
     * 开始节点
     * @since 0.0.2
     */
    private TaintData from;

    /**
     * 结束节点
     * @since 0.0.2
     */
    private TaintData to;



    public TaintEdge(TaintData from, TaintData to) {
        this.from = from;
        this.to = to;
    }

    public TaintData getFrom() {
        return from;
    }

    public void setFrom(TaintData from) {
        this.from = from;
    }

    public TaintData getTo() {
        return to;
    }

    public void setTo(TaintData to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaintEdge taintEdge = (TaintEdge) o;
        return Objects.equals(from, taintEdge.from) &&
                Objects.equals(to, taintEdge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
