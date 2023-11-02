package com.keven1z.core.model.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 完整的类节点信息：
 *
 * @author keven1z
 */
public class TaintNode {

    /**
     * 顶点信息
     *
     * @since 0.0.2
     */
    private final TaintData taintData;

    /**
     * 以此顶点为起点的边的集合，是一个列表，列表的每一项是一条边
     * <p>
     * （1）使用集合，避免重复
     */
    private final Set<TaintEdge> taintEdgeSet;

    /**
     * 初始化一個節點
     *
     * @param vertex 頂點
     */
    public TaintNode(TaintData vertex) {
        this.taintData = vertex;
        this.taintEdgeSet = new HashSet<>();
    }

    /**
     * 新增一条边
     *
     * @param taintEdge 边
     */
    public void add(final TaintEdge taintEdge) {
        taintEdgeSet.add(taintEdge);
    }

    /**
     * 获取目标边
     *
     * @param to 目标边
     * @return 边
     * @since 0.0.2
     */
    public TaintEdge get(final TaintData to) {
        for (TaintEdge taintEdge : taintEdgeSet) {
            TaintData dest = taintEdge.getTo();

            if (dest.equals(to)) {
                return taintEdge;
            }
        }
        return null;
    }

    /**
     * 删除
     *
     * @param to 目标边
     * @since 0.0.2
     */
    public void remove(final TaintData to) {
        Iterator<TaintEdge> edgeIterable = taintEdgeSet.iterator();

        while (edgeIterable.hasNext()) {
            TaintEdge next = edgeIterable.next();

            if (to.equals(next.getTo())) {
                edgeIterable.remove();
                return;
            }
        }

    }

    public TaintData getTaintData() {
        return taintData;
    }

    public Set<TaintEdge> getEdgeSet() {
        return taintEdgeSet;
    }

    public Set<TaintData> getToNode() {
        HashSet<TaintData> taintDataSet = new HashSet<>();
        for (TaintEdge taintEdge : this.getEdgeSet()) {
            TaintData from = taintEdge.getFrom();
            if (from.equals(this.taintData)) {
                taintDataSet.add(taintEdge.getTo());
            }
        }
        return taintDataSet;
    }

    @Override
    public String toString() {
        return this.getTaintData().toString();
    }

    /**
     * 判断是否是该节点传出的对象
     *
     * @param fromObject 传入对象
     */
    public boolean isToObject(int fromObjectHashCode) {
        int toObjectHashCode = this.taintData.getToObjectHashCode();
        return fromObjectHashCode == toObjectHashCode;
    }
    public void clear(){
        this.taintEdgeSet.clear();
    }
}
