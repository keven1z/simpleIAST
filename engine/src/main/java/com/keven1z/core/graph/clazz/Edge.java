package com.keven1z.core.graph.clazz;

import java.util.Objects;

/**
 * 边的信息
 * @author binbin.hou
 * @since 0.0.2
 */
public class Edge {

    /**
     * 开始节点
     */
    private ClassData from;

    /**
     * 结束节点
     */
    private ClassData to;



    public Edge(ClassData from, ClassData to) {
        this.from = from;
        this.to = to;
    }

    public ClassData getFrom() {
        return from;
    }

    public void setFrom(ClassData from) {
        this.from = from;
    }

    public ClassData getTo() {
        return to;
    }

    public void setTo(ClassData to) {
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
        Edge edge = (Edge) o;
        return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
