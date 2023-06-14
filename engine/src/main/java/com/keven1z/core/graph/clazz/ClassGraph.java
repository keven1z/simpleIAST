package com.keven1z.core.graph.clazz;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 应用所有class信息 继承关系的图结构，暂且未使用，
 *
 * @author keven1z
 * @date 2021/11/18
 */
public class ClassGraph {

    /**
     * 节点链表
     */
    private final Set<ClassNode> nodeSet;

    /**
     * 初始化有向图
     */
    public ClassGraph() {
        this.nodeSet = new CopyOnWriteArraySet<>();
    }

    public void addNode(ClassData classData) {
        ClassNode node = getNode(classData);
        if (node != null && classData.getAccess() != -1) {
            node.getVertex().setAccess(classData.getAccess());
        } else {
            node = new ClassNode(classData);
            this.nodeSet.add(node);
        }
        // 直接加入到集合中

    }

    public ClassData addNode(String className) {
        ClassNode node = this.getNode(className);
        if (node != null) {
            return node.getVertex();
        }
        ClassData classData = new ClassData(className);
        classData.setAccess(-1);
        node = new ClassNode(classData);
        // 直接加入到集合中
        addNode(node);
        return classData;
    }

    public void addNode(ClassNode node) {
        // 直接加入到集合中
        this.nodeSet.add(node);
    }

    public int getNodeSize() {
        return this.nodeSet.size();
    }

    public boolean removeVertex(ClassData v) {
        //1. 移除一个顶点
        //2. 所有和这个顶点关联的边也要被移除
        nodeSet.removeIf(classNode -> v.equals(classNode.getVertex()));

        return true;
    }

    public void addEdge(ClassData from, ClassData to) {
        if (from == null || to == null) return;
        Edge classInfoEdge = new Edge(from, to);
        this.addEdge(classInfoEdge);
    }

    public void addEdge(Edge taintEdge) {
        //1. 新增一条边，直接遍历列表。
        // 如果存在这条的起始节点，则将这条边加入。
        // 如果不存在，则直接报错即可。

        for (ClassNode classNode : nodeSet) {
            ClassData from = taintEdge.getFrom();
            ClassData originFrom = classNode.getVertex();

            // 起始节点在开头
            if (from.equals(originFrom)) {
                classNode.getEdgeSet().add(taintEdge);
            }
        }
    }

    public boolean removeEdge(Edge taintEdge) {
        // 直接从列表中对应的节点，移除即可
        ClassNode node = getNode(taintEdge);
        if (null != node) {
            // 移除目标为 to 的边
            node.remove(taintEdge.getTo());
        }

        return true;
    }


    /**
     * 获取图节点
     *
     * @param taintEdge 边
     * @return 图节点
     */
    private ClassNode getNode(final Edge taintEdge) {
        for (ClassNode node : nodeSet) {
            final ClassData from = taintEdge.getFrom();

            if (node.getVertex().equals(from)) {
                return node;
            }
        }

        return null;
    }

    /**
     * 获取对应的图节点
     *
     * @param vertex 顶点
     * @return 图节点
     * @since 0.0.2
     */
    public ClassNode getNode(ClassData vertex) {
        for (ClassNode node : nodeSet) {
            if (node.getVertex().equals(vertex)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 获取className当前class节点
     *
     * @param className
     * @return
     */
    public ClassNode getNode(String className) {
        for (ClassNode node : nodeSet) {
            ClassData vertex = node.getVertex();
            if (vertex != null) {
                if (vertex.getClassName().equals(className)) return node;
            }
        }
        return null;
    }

    /**
     * 获取className当前class頂點
     *
     * @param className
     * @return
     */
    public ClassData getVertex(String className) {

        ClassNode nodeByClassName = getNode(className);
        if (nodeByClassName == null) return null;
        return nodeByClassName.getVertex();
    }


    public HashSet<Edge> getToEdges(ClassData to) {
        HashSet<Edge> edges = new HashSet<>();
        for (Edge taintEdge : this.getEdges()) {
            ClassData dest = taintEdge.getTo();
            if (dest.equals(to)) {
                edges.add(taintEdge);
            }
        }
        return edges;
    }

    public HashSet<Edge> getFromEdges(ClassData from) {
        HashSet<Edge> edges = new HashSet<>();
        for (Edge taintEdge : this.getEdges()) {
            ClassData dest = taintEdge.getFrom();
            if (dest.equals(from)) {
                edges.add(taintEdge);
            }
        }
        return edges;
    }


    public HashSet<Edge> getEdges() {
        HashSet<Edge> edges = new HashSet<>();
        for (ClassNode node : this.nodeSet) {
            Set<Edge> edgeSet = node.getEdgeSet();
            edges.addAll(edgeSet);
        }
        return edges;
    }

    /**
     * 清除图
     */
    public void clear() {
        this.nodeSet.clear();
    }


}
