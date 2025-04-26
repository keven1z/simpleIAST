package com.keven1z.core.model.taint;

import com.keven1z.core.consts.PolicyType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 完整的类节点信息：
 *
 * @author keven1z
 */
public class PathNode {

    /**
     * 顶点信息
     *
     * @since 0.0.2
     */
    private final TaintData taintData;
    private final PolicyType policyType;
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
    public PathNode(TaintData vertex, PolicyType policyType) {
        this.taintData = vertex;
        this.taintEdgeSet = new HashSet<>();
        this.policyType = policyType;
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
            PathNode pathNode = taintEdge.getTo();

            if (pathNode.getTaintData().equals(to)) {
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


    @Override
    public String toString() {
        return this.getTaintData().toString();
    }

    /**
     * 判断是否是该节点传出的对象
     *
     * @param fromObjectHashCode 传入对象
     */
    public boolean isToObject(int fromObjectHashCode) {
        List<TaintData.FlowPath> flowPaths = this.taintData.getFlowPaths();
        for (TaintData.FlowPath flowPath : flowPaths) {
            if(flowPath.getHashcode() == fromObjectHashCode){
                return true;
            }
        }
        return false;
    }

    public void clear() {
        this.taintEdgeSet.clear();
//        this.taintData.clear();
    }

    public PolicyType getPolicyType() {
        return policyType;
    }
}
