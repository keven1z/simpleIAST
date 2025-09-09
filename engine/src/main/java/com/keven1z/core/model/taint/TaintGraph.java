package com.keven1z.core.model.taint;

import com.keven1z.core.consts.HookType;
import com.keven1z.core.utils.TaintTracker;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * hook信息图结构
 *
 * @author keven1z
 * @since 2021/11/18
 */
public class TaintGraph {

    /**
     * 节点链表
     */
    private final List<PathNode> pathNodeList;
    /*
     * 由to到from的路径map
     */
    private final Map<Integer, Set<Integer>> pathMap;

    /**
     * 初始化有向图
     */
    public TaintGraph() {
        this.pathNodeList = new ArrayList<>(1024);
        this.pathMap = new HashMap<>(100);
    }

    public List<PathNode> getSinkNodes() {
        ArrayList<PathNode> sinkNodes = new ArrayList<>(pathNodeList.size());
        for (PathNode node : pathNodeList) {
            if (node.getTaintData().getStage() == HookType.SINK) {
                sinkNodes.add(node);
            }
        }
        return sinkNodes;
    }

    public int getNodeSize() {
        return this.pathNodeList.size();
    }

    /**
     * 添加taint node
     */
    public PathNode addNode(TaintData taintData) {
        PathNode node = new PathNode(taintData);
        addNode(node);
        List<TaintData.FlowPath> flowPaths = taintData.getFlowPaths();
        for (TaintData.FlowPath flowPath : flowPaths) {
            TaintTracker.addTaint(flowPath.getHashcode());
        }
        return node;
    }

    public void addNode(PathNode node) {
        this.pathNodeList.add(node);
    }

    public void addEdge(TaintEdge taintEdge) {
        //1. 新增一条边，直接遍历列表。
        // 如果存在这条的起始节点，则将这条边加入。
        // 如果不存在，则直接报错即可。

        for (PathNode PathNode : pathNodeList) {
            PathNode fromNode = taintEdge.getFrom();

            // 起始节点在开头
            if (fromNode.equals(PathNode)) {
                PathNode.getEdgeSet().add(taintEdge);
                return;
            }
        }
    }

    /**
     * 从给定的TaintData对象中获取其所有from节点。
     *
     * @param to 要获取from节点的to节点对应的TaintData对象
     * @return 包含所有from节点的Set集合，如果to节点不存在于pathMap中，则返回null
     */
    private Set<PathNode> getFromNodes(TaintData to) {
        if (to == null) {
            throw new IllegalArgumentException("TaintData object cannot be null.");
        }

        if (!this.pathMap.containsKey(to.getInvokeId())) {
            return Collections.emptySet();
        }

        Set<Integer> invokeIds = this.pathMap.get(to.getInvokeId());
        Set<PathNode> pathNodes = new LinkedHashSet<>();
        for (Integer invokeId : invokeIds) {
            PathNode node = findNodeByInvokeId(invokeId);
            if (node != null) {
                pathNodes.add(node);
            }
        }
        return pathNodes;
    }

    private PathNode findNodeByInvokeId(Integer invokeId) {
        return this.pathNodeList.stream()
                .filter(node -> invokeId.equals(node.getTaintData().getInvokeId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取以 from 为开始节点的所有边的set集合
     *
     * @param from 开始节点
     */
    public Set<TaintEdge> getFromEdges(TaintData from) {
        HashSet<TaintEdge> taintEdges = new HashSet<>();
        for (TaintEdge taintEdge : this.getEdges()) {
            TaintData dest = taintEdge.getFrom().getTaintData();
            if (dest.equals(from)) {
                taintEdges.add(taintEdge);
            }
        }
        return taintEdges;
    }


    public Set<TaintEdge> getEdges() {
        HashSet<TaintEdge> taintEdges = new HashSet<>();
        for (PathNode node : this.pathNodeList) {
            Set<TaintEdge> taintEdgeSet = node.getEdgeSet();
            taintEdges.addAll(taintEdgeSet);
        }
        return taintEdges;
    }

    private void clearEdges() {
        for (PathNode node : this.pathNodeList) {
            node.clear();
        }
    }

    /**
     * 清除图
     */
    public void clear() {
        this.clearEdges();
        this.pathNodeList.clear();
    }

    public void addEdge(PathNode from, PathNode to, String direction) {
        if (from == null || to == null) return;
        TaintEdge classInfoTaintEdge = new TaintEdge(from, to, direction);
        int toInvokeId = to.getTaintData().getInvokeId();
        Set<Integer> fromInvokeIdSet;
        if (pathMap.containsKey(toInvokeId)) {
            fromInvokeIdSet = pathMap.get(toInvokeId);
        } else {
            fromInvokeIdSet = new LinkedHashSet<>();
        }
        fromInvokeIdSet.add(from.getTaintData().getInvokeId());
        pathMap.put(toInvokeId, fromInvokeIdSet);

        this.addEdge(classInfoTaintEdge);
    }

    public List<PathNode> getAllNode() {
        return pathNodeList;
    }

    public boolean isEmpty() {
        return this.pathNodeList.isEmpty();
    }

    /**
     * 广度遍历
     *
     * @return 返回遍历过的taintData集合
     */
    public List<TaintData> bfs(PathNode start) {
        if (start == null) {
            throw new IllegalArgumentException("Start node cannot be null.");
        }

        LinkedList<TaintData> taintDataList = new LinkedList<>();
        Set<Integer> visitedNodeInvokeIds = new HashSet<>();
        Queue<TaintData> queue = new LinkedList<>();
        TaintData startTaintData = start.getTaintData();
        queue.offer(startTaintData);
        visitedNodeInvokeIds.add(startTaintData.getInvokeId()); // 添加起始节点ID到已访问集合

        while (!queue.isEmpty()) {
            TaintData poll = queue.poll();
            taintDataList.add(poll);
            Set<PathNode> fromNodes = this.getFromNodes(poll);

            for (PathNode fromNode : fromNodes) {
                TaintData fromNodeTaintData = fromNode.getTaintData();
                if (!visitedNodeInvokeIds.contains(fromNodeTaintData.getInvokeId())) {
                    queue.offer(fromNodeTaintData);
                    visitedNodeInvokeIds.add(fromNodeTaintData.getInvokeId());
                }
            }
        }
        addSanitizer(taintDataList);
        // 广度遍历的结果需要按从source到sink的顺序排列，因此倒转列表
        Collections.reverse(taintDataList);
        return taintDataList;
    }

    /**
     * 添加过滤处理到污点传播阶段
     */
    private void addSanitizer(List<TaintData> taintDataList) {
//        for (TaintData taintData : taintDataList) {
//            if (!taintData.isHasSanitizer()) {
//                continue;
//            }
//            List<TaintData> sanitizerList = this.getSanitizerList(taintData, taintDataList);
//            taintData.addSanitizer(sanitizerList);
//        }
    }

    /**
     * 提取过滤方法
     */
    private List<TaintData> getSanitizerList(TaintData taintData, List<TaintData> taintDataList) {
        List<TaintData> sanitizerTaintDataList = new LinkedList<>();
        LinkedBlockingQueue<TaintData> queue = new LinkedBlockingQueue<>(this.getNodeSize());
        queue.add(taintData);
        while (!queue.isEmpty()) {
            Set<TaintEdge> fromEdges = this.getFromEdges(queue.poll());
            for (TaintEdge edge : fromEdges) {
                PathNode toNode = edge.getTo();
                TaintData toTaintData = edge.getTo().getTaintData();
                //如果污点的去向为SANITIZER并且没有包含在污点传播流程中
                if (HookType.SANITIZER.equals(toNode.getTaintData().getStage()) && !taintDataList.contains(toTaintData)) {
                    queue.add(toTaintData);
                    sanitizerTaintDataList.add(toTaintData);
                }
            }
        }
        return sanitizerTaintDataList;
    }

}
