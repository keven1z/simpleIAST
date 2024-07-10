package com.keven1z.core.utils;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.policy.SanitizerTypeEnum;

import java.util.*;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

public class TaintUtils {

    private static final int MAX_VALUE_LENGTH = 200;

    /**
     * 格式化值，值的长度大于 MAX_VALUE_LENGTH，省略中间部分
     *
     * @param value 待格式化的值
     */
    public static String format(String value) {
        if (value.length() <= MAX_VALUE_LENGTH) {
            return value;
        }
        value = value.substring(0, 100) + "..." + value.substring(value.length() - 100);
        return value;
    }

    /**
     * 计算污染源的最终值
     */
    public static List<Object> calculateSourceValue(LinkedList<TaintData> taintDataLinkedList) {
        List<TaintData> sourceList = getSourceList(taintDataLinkedList);
        ArrayList<Object> returnSourceList = new ArrayList<>();
        if (sourceList.isEmpty()) {
            return returnSourceList;
        }

        for (TaintData taintData : sourceList) {
            Object source = calculateSingleSource(taintData, taintDataLinkedList);
            if (source != null) {
                returnSourceList.add(source);
            }
        }
        return returnSourceList;
    }

    /**
     * 计算单个污染源
     */
    public static Object calculateSingleSource(TaintData source, LinkedList<TaintData> taintDataLinkedList) {
        List<Integer> sourceHashCodes = source.getToObjectHashCode();
        Object sourceObject = source.getReturnObject();
        for (TaintData taint : taintDataLinkedList) {
            if (taint.isSource()) {
                continue;
            }
            Object fromObject = taint.getFromObject();
            if (sourceHashCodes.contains(System.identityHashCode(fromObject))) {
                sourceObject = fromObject;
            }

        }
        return sourceObject;
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
     * 判断给定的TaintDataList中是否包含指定类型的Sanitizer，且数量达到或超过requiredOccurrenceNum。
     *
     * @param taintDataList 包含TaintData对象的列表
     * @param sanitizerTypeEnum 需要匹配的Sanitizer类型枚举
     * @param requiredOccurrenceNum 需要的Sanitizer数量
     * @return 如果在taintDataList中找到指定类型的Sanitizer数量达到或超过requiredOccurrenceNum，则返回true；否则返回false
     */
    public static boolean containSanitizer(List<TaintData> taintDataList, SanitizerTypeEnum sanitizerTypeEnum, int requiredOccurrenceNum) {
        int occurrenceNum = requiredOccurrenceNum;

        for (TaintData taintData : taintDataList) {
            if (PolicyTypeEnum.SANITIZER.name().equals(taintData.getStage()) && sanitizerTypeEnum.name().equals(taintData.getName())) {
                occurrenceNum--;
            }

            List<TaintData> sanitizerNodes = taintData.getSanitizerNodes();
            if (sanitizerNodes != null) {
                for (TaintData sanitizerNode : sanitizerNodes) {
                    if (sanitizerTypeEnum.name().equals(sanitizerNode.getName())) {
                        occurrenceNum--;
                    }
                }
            }

            if (occurrenceNum <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的TaintDataList中是否连续包含指定类型的Sanitizer，且数量达到或超过requiredOccurrenceNum。
     *
     * @param taintDataList 包含TaintData对象的列表
     * @param sanitizerTypeEnum 需要匹配的Sanitizer类型枚举
     * @param requiredOccurrenceNum 需要的Sanitizer连续出现的次数
     * @return 如果在taintDataList中存在连续出现的sanitizerTypeEnum类型的Sanitizer数量达到或超过requiredOccurrenceNum，则返回true；否则返回false
     */
    public static boolean containSanitizerInContinuousCode(List<TaintData> taintDataList, SanitizerTypeEnum sanitizerTypeEnum, int requiredOccurrenceNum) {
        for (TaintData taintData : taintDataList) {
            List<TaintData> sanitizerNodes = taintData.getSanitizerNodes();
            if (sanitizerNodes != null) {
                int currentOccurrenceNum = requiredOccurrenceNum;
                for (TaintData sanitizerNode : sanitizerNodes) {
                    if (sanitizerTypeEnum.name().equals(sanitizerNode.getName())) {
                        currentOccurrenceNum--;
                    }
                    if (currentOccurrenceNum <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 添加命中的污点类
     *
     * @param isRecordStack 是否记录调用栈
     */
    public static void buildTaint(Object returnObject, TaintData taintData, boolean isRecordStack) {
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        if (taintGraph == null) {
            return;
        }

        if (taintData == null) {
            return;
        }

        if (isRecordStack) {
            taintData.setStackList(StackUtils.getStackTraceArray(true, true));
        }
        taintData.setReturnObject(returnObject);

        taintGraph.addNode(taintData);
    }
}
