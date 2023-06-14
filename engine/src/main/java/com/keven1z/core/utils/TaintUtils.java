package com.keven1z.core.utils;

import com.keven1z.core.graph.taint.TaintData;
import com.keven1z.core.graph.taint.TaintNode;
import com.keven1z.core.policy.PolicyTypeEnum;

import java.util.*;

public class TaintUtils {
    /**
     * 查找所有source点
     */
    public static List<TaintNode> findSourceInTaintNodes(LinkedList<TaintNode> taintNodes) {
        List<TaintNode> sourceNodes = new ArrayList<>();
        for (TaintNode taintNode : taintNodes) {
            if (PolicyTypeEnum.SOURCE.equals(taintNode.getTaintData().getType())) {
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
            returnSourceList.add(handlerSource(taintData, taintDataLinkedList));
        } else {
            for (TaintData taintData : sourceList) {
                returnSourceList.add(handlerSource(taintData, taintDataLinkedList));
            }
        }
        return returnSourceList;
    }

    public static List<TaintData> getSourceList(LinkedList<TaintData> taintDataLinkedList) {
        ArrayList<TaintData> sourceDataList = new ArrayList<>();
        for (TaintData taintData : taintDataLinkedList) {
            if (PolicyTypeEnum.SOURCE.equals(taintData.getType())) {
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
        String taintValueType = taintData.getTaintValueType();
        switch (taintValueType) {
            case "java.lang.String":
                return taintData.getToValue();
            case "java.io.InputStream":
                return null;
            case "java.util.Map":
                for (TaintData data : taintDataLinkedList) {
                    if (PolicyTypeEnum.PROPAGATION.equals(data.getType())) {
                        if (data.getFromObjectHashCode() == taintData.getToObjectHashCode()) {
                            String className = taintData.getClassName();
                            String method = taintData.getMethod();
                            if (className.contains("Map") || method.equals("get")) {
                                return data.getReturnValue();
                            }
                        }
                    }
                }
                return null;
            default:
                return null;
        }
    }

}
