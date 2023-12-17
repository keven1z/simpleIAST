package com.keven1z.core.utils;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.policy.SanitizerTypeEnum;

import java.util.*;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

public class TaintUtils {
    /**
     * 查找所有source点
     */
    public static List<TaintNode> findSourceInTaintNodes(LinkedList<TaintNode> taintNodes) {
        List<TaintNode> sourceNodes = new ArrayList<>();
        for (TaintNode taintNode : taintNodes) {
            if (PolicyTypeEnum.SOURCE.name().equals(taintNode.getTaintData().getStage())) {
                sourceNodes.add(taintNode);
            } else {
                /*
                 * 由于taintNodes按顺序排序的，若判断到非source节点直接返回
                 */
                return sourceNodes;
            }
        }
        return sourceNodes;
    }

    private static final int MAX_VALUE_LENGTH = 100;

    /**
     * 格式化值，值的长度大于 MAX_VALUE_LENGTH，省略中间部分
     *
     * @param value 待格式化的值
     */
    public static String format(String value) {
        if (value.length() <= MAX_VALUE_LENGTH) {
            return value;
        }
        value = value.substring(0, 40) + "..." + value.substring(value.length() - 40);
        return value;
    }

    /**
     * 计算污染源的最终值
     */
    public static List<String> calculateSourceValue(LinkedList<TaintData> taintDataLinkedList) {
        List<TaintData> sourceList = getSourceList(taintDataLinkedList);
        ArrayList<String> returnSourceList = new ArrayList<>();
        if (sourceList.isEmpty()) {
            return returnSourceList;
        }
        //如果为单个源
        if (sourceList.size() == 1) {
            TaintData taintData = sourceList.get(0);
            String source = handlerSource(taintData, taintDataLinkedList);
            if (source != null) {
                returnSourceList.add(source);
            }
        } else {
            for (TaintData taintData : sourceList) {
                String source = handlerSource(taintData, taintDataLinkedList);
                if (source != null) {
                    returnSourceList.add(source);
                }
            }
        }
        return returnSourceList;
    }

    /**
     * @return 获取当前污染链中所有的污染源
     */
    public static List<TaintData> getSourceList(LinkedList<TaintData> taintDataLinkedList) {
        ArrayList<TaintData> sourceDataList = new ArrayList<>();
        for (TaintData taintData : taintDataLinkedList) {
            if (PolicyTypeEnum.SOURCE.name().equals(taintData.getStage())) {
                sourceDataList.add(taintData);
            }
        }
        return sourceDataList;
    }

    /**
     * 返回污染源数据，返回null不进行源计入
     *
     * @param taintData 污染源
     */
    private static String handlerSource(TaintData taintData, LinkedList<TaintData> taintDataLinkedList) {
        String taintValueType = taintData.getToType();
        switch (taintValueType) {
            case "java.lang.String":
                return taintData.getToValue();
            case "java.io.InputStream":
                return null;
//            case "java.util.ArrayList":
//                for (TaintData data : taintDataLinkedList) {
//                    if (PolicyTypeEnum.PROPAGATION.name().equals(data.getStage())) {
//                        if (data.getFromObjectHashCode() == taintData.getToObjectHashCode()) {
//                            String className = data.getClassName();
//                            String method = data.getMethod();
//                            if (className.contains("List") && method.equals("get")) {
//                                return data.getToValue();
//                            }
//                        }
//                    }
//                }
//            case "java.util.Map":
//                for (TaintData data : taintDataLinkedList) {
//                    if (PolicyTypeEnum.PROPAGATION.name().equals(data.getStage())) {
//                        if (data.getFromObjectHashCode() == taintData.getToObjectHashCode()) {
//                            String className = data.getClassName();
//                            String method = data.getMethod();
//                            if (className.contains("Map") && method.equals("get")) {
//                                return data.getToValue();
//                            }
//                        }
//                    }
//                }
            default:
                return null;
        }
    }

    /**
     * @param taintDataList     污染链
     * @param sanitizerTypeEnum 过滤类型
     * @param occurrenceNum     过滤类型出现次数
     *                          判定污染链中是否包含指定过滤类型
     */
    public static boolean isContainSanitizer(List<TaintData> taintDataList, SanitizerTypeEnum sanitizerTypeEnum, int occurrenceNum) {
        for (TaintData taintData : taintDataList) {
            List<TaintData> sanitizerNodes = taintData.getSanitizerNodes();
            if (PolicyTypeEnum.SANITIZER.name().equals(taintData.getStage())) {
                if (sanitizerTypeEnum.name().equals(taintData.getName())) {
                    occurrenceNum--;
                }
            }

            if (sanitizerNodes == null || sanitizerNodes.isEmpty()) {
                continue;
            }
            for (TaintData sanitizerNode : sanitizerNodes) {
                if (sanitizerTypeEnum.name().equals(sanitizerNode.getName())) {
                    occurrenceNum--;
                }
            }

            if (occurrenceNum <= 0) {
                return true;
            }

        }
        return false;

    }

    /**
     * 添加命中的污点类
     *
     * @param isRecordStack 是否记录调用栈
     */
    public static void buildTaint(Object returnObject, TaintData taintData, Object toObject, boolean isRecordStack) {
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        if (taintGraph == null) {
            return;
        }

        if (taintData == null) {
            return;
        }

        taintData.setToObjectHashCode(System.identityHashCode(toObject));
        if (toObject != null) {
            taintData.setToValue(toObject.toString());
            taintData.setToType(toObject.getClass().getTypeName());
        }

        if (isRecordStack) {
            taintData.setStackList(StackUtils.getStackTraceArray(true, true));
        }
        taintData.setReturnObjectString(returnObject == null ? null : returnObject.toString());
        taintData.setReturnObjectType(returnObject == null ? null : returnObject.getClass().getName());

        taintGraph.addNode(taintData);
    }
}
