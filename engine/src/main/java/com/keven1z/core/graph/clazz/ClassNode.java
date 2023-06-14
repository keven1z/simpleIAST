package com.keven1z.core.graph.clazz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 完整的类节点信息：
 *
 *
 *
 * @author keven1z
 */
public class ClassNode {

    /**
     * 顶点信息
     */
    private final ClassData classData;

    /**
     * 以此顶点为起点的边的集合，是一个列表，列表的每一项是一条边
     *
     * （1）使用集合，避免重复
     */
    private final Set<Edge> edgeSet;

    /**
     * 初始化一個節點
     * @param vertex 頂點
     */
    public ClassNode(ClassData vertex) {
        this.classData = vertex;
        this.edgeSet = new HashSet<>();
    }

    /**
     * 新增一条边
     * @param edge 边
     */
    public void add(final Edge edge) {
        edgeSet.add(edge);
    }

    /**
     * 获取目标边
     * @param to 目标边
     * @return 边
     * @since 0.0.2
     */
    public Edge get(final ClassData to) {
        for(Edge edge : edgeSet) {
            ClassData dest = edge.getTo();

            if(dest.equals(to)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * 删除
     * @param to 目标边
     * @since 0.0.2
     */
    public void remove(final ClassData to) {
        Iterator<Edge> edgeIterable = edgeSet.iterator();

        while (edgeIterable.hasNext()) {
            Edge next = edgeIterable.next();

            if(to.equals(next.getTo())) {
                edgeIterable.remove();
                return;
            }
        }

    }

    /**
     * 获取节点信息
     * @return 节点信息
     */
    public ClassData getVertex() {
        return classData;
    }

    public Set<Edge> getEdgeSet() {
        return edgeSet;
    }

    public Set<ClassData> getToNode(){
        HashSet<ClassData> classData = new HashSet<>();
        for(Edge edge:this.getEdgeSet()){
                ClassData from = edge.getFrom();
                if(from.equals(this.classData)){
                    classData.add(edge.getTo());
                }
        }
        return classData;
    }

    @Override
    public String toString() {
        return this.getVertex().toString();
    }

}
