package com.keven1z.core.model.graph;

import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.PolicyTypeEnum;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * hook信息图结构
 *
 * @author keven1z
 * @date 2021/11/18
 */
public class TaintGraph {

    /**
     * 节点链表
     */
    private final List<TaintNode> nodeSet;
    private final List<TaintNode> sinkNode;

    /**
     * 污点缓存
     */
    private final Set<Integer> taintCache;
    private final Logger taintLogger = Logger.getLogger("taint.info");


    /**
     * 初始化有向图
     */
    public TaintGraph() {
//        this.nodeSet = new CopyOnWriteArraySet<>(new HashSet<>(2048));
        this.nodeSet = new ArrayList<>(1024);
        this.sinkNode = new ArrayList<>(10);
        this.taintCache = new HashSet<>(1024);
    }

    public TaintNode addNode(TaintData taintData) {
        TaintNode node = new TaintNode(taintData);
        addNode(node);
        this.taintCache.add(taintData.getToObjectHashCode());
        if (LogTool.isDebugEnabled()) {
            taintLogger.info("Add taint:" + taintData);
        }
        return node;
    }

    public boolean isTaint(int systemCode) {
        return taintCache.contains(systemCode);
    }

    public void addNode(TaintNode node) {
        if (PolicyTypeEnum.SINK.equals(node.getTaintData().getType())) {
            this.sinkNode.add(node);
        }
        // 直接加入到集合中
        this.nodeSet.add(node);
    }

    public int getNodeSize() {
        return this.nodeSet.size();
    }

    public boolean removeVertex(TaintData v) {
        //1. 移除一个顶点
        //2. 所有和这个顶点关联的边也要被移除
        nodeSet.removeIf(taintNode -> v.equals(taintNode.getTaintData()));

        return true;
    }

    public void addEdge(TaintEdge taintEdge) {
        //1. 新增一条边，直接遍历列表。
        // 如果存在这条的起始节点，则将这条边加入。
        // 如果不存在，则直接报错即可。

        for (TaintNode taintNode : nodeSet) {
            TaintData from = taintEdge.getFrom();
            TaintData originFrom = taintNode.getTaintData();

            // 起始节点在开头
            if (from.equals(originFrom)) {
                taintNode.getEdgeSet().add(taintEdge);
            }
        }
    }

    public boolean removeEdge(TaintEdge taintEdge) {
        // 直接从列表中对应的节点，移除即可
        TaintNode node = getNode(taintEdge);
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
    private TaintNode getNode(final TaintEdge taintEdge) {
        for (TaintNode node : nodeSet) {
            final TaintData from = taintEdge.getFrom();

            if (node.getTaintData().equals(from)) {
                return node;
            }
        }

        return null;
    }


    /**
     * 获取className当前class节点
     *
     * @param className 类名
     */
    public TaintNode getNode(String className) {
        for (TaintNode node : nodeSet) {
            TaintData vertex = node.getTaintData();
            if (vertex != null) {
                if (vertex.getClassName().equals(className)) return node;
            }
        }
        return null;
    }

    /**
     * 获取className当前class頂點
     *
     * @param className 类名
     */
    public TaintData getVertex(String className) {

        TaintNode nodeByClassName = getNode(className);
        if (nodeByClassName == null) return null;
        return nodeByClassName.getTaintData();
    }


    /**
     * 获取以 to为结束节点的所有边的set集合
     *
     * @param to 结束节点
     */
    public Set<TaintEdge> getToEdges(TaintData to) {
        HashSet<TaintEdge> taintEdges = new HashSet<>();
        for (TaintEdge taintEdge : this.getEdges()) {
            TaintData dest = taintEdge.getTo();
            if (dest.equals(to)) {
                taintEdges.add(taintEdge);
            }
        }
        return taintEdges;
    }

    /**
     * 获取以 from 为开始节点的所有边的set集合
     *
     * @param from 开始节点
     */
    public HashSet<TaintEdge> getFromEdges(TaintData from) {
        HashSet<TaintEdge> taintEdges = new HashSet<>();
        for (TaintEdge taintEdge : this.getEdges()) {
            TaintData dest = taintEdge.getFrom();
            if (dest.equals(from)) {
                taintEdges.add(taintEdge);
            }
        }
        return taintEdges;
    }


    public Set<TaintEdge> getEdges() {
        HashSet<TaintEdge> taintEdges = new HashSet<>();
        for (TaintNode node : this.nodeSet) {
            Set<TaintEdge> taintEdgeSet = node.getEdgeSet();
            taintEdges.addAll(taintEdgeSet);
        }
        return taintEdges;
    }

    private void clearEdges() {
        for (TaintNode node : this.nodeSet) {
            node.clear();
        }
    }

    /**
     * 清除图
     */
    public void clear() {
        this.clearEdges();
        this.sinkNode.clear();
        this.nodeSet.clear();
        this.taintCache.clear();
    }

    public void addEdge(TaintData from, TaintData to) {
        if (from == null || to == null) return;
        TaintEdge classInfoTaintEdge = new TaintEdge(from, to);
        this.addEdge(classInfoTaintEdge);
    }

    public List<TaintNode> getAllNode() {
        return nodeSet;
    }

    public boolean isEmpty() {
        return this.nodeSet.isEmpty();
    }

    /**
     * 广度遍历
     *
     * @return 返回遍历过的taintData集合
     */
    public LinkedList<TaintData> bfs(TaintNode start) {
        /*
         * 存储遍历的taintData
         */
        LinkedList<TaintData> taintDataList = new LinkedList<>();

        LinkedBlockingQueue<TaintData> queue = new LinkedBlockingQueue<>(this.getNodeSize());
        TaintData taintData = start.getTaintData();
        queue.add(taintData);
        taintDataList.add(taintData);

        while (!queue.isEmpty()) {
            TaintData poll = queue.poll();
            Set<TaintEdge> toEdges = this.getToEdges(poll);
            for (TaintEdge edge : toEdges) {
                TaintData from = edge.getFrom();
                taintDataList.add(from);
                queue.add(from);
            }
        }
        //由于广度遍历由sink到source的，需要倒转顺序
        Collections.reverse(taintDataList);
        addSanitizer(taintDataList);
        return taintDataList;
    }

    /**
     * 添加过滤处理到污点传播阶段
     */
    private void addSanitizer(LinkedList<TaintData> taintDataList) {
        for (TaintData taintData : taintDataList) {
            if (!taintData.isSanitizer()) {
                continue;
            }
            List<TaintData> sanitizerList = this.getSanitizerList(taintData, taintDataList);
            taintData.addSanitizer(sanitizerList);
        }
    }

    /**
     * 提取过滤方法
     */
    private List<TaintData> getSanitizerList(TaintData taintData, LinkedList<TaintData> taintDataList) {
        List<TaintData> linkedList = new LinkedList<>();
        LinkedBlockingQueue<TaintData> queue = new LinkedBlockingQueue<>(this.getNodeSize());
        queue.add(taintData);
        while (!queue.isEmpty()) {
            HashSet<TaintEdge> fromEdges = this.getFromEdges(queue.poll());
            for (TaintEdge edge : fromEdges) {
                TaintData to = edge.getTo();
                //如果污点的去向为SANITIZER并且没有包含在污点传播流程中
                if (PolicyTypeEnum.SANITIZER.equals(to.getType()) && !taintDataList.contains(to)) {
                    queue.add(to);
                    linkedList.add(to);
                }
            }
        }
        return linkedList;
    }

    public LinkedList<TaintData> getAllTaint() {
        List<TaintNode> allNode = this.getAllNode();
        LinkedList<TaintData> list = new LinkedList<>();
        for (TaintNode node : allNode) {
            list.add(node.getTaintData());
        }
        return list;
    }

    public List<TaintNode> getSinkNodes() {
        return sinkNode;
    }

}
