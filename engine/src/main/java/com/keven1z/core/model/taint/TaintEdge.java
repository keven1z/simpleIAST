package com.keven1z.core.model.taint;

import java.util.Objects;

/**
 * 边的信息
 * @author binbin.hou
 * @since 0.0.2
 */
public class TaintEdge {

    /**
     * 开始节点
     */
    private PathNode from;

    /**
     * 结束节点
     */
    private PathNode to;
    /**
     * 污点流出的方向
     */
    private String direction;



    public TaintEdge(PathNode from, PathNode to, String direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    public PathNode getFrom() {
        return from;
    }

    public void setFrom(PathNode from) {
        this.from = from;
    }

    public PathNode getTo() {
        return to;
    }

    public void setTo(PathNode to) {
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
