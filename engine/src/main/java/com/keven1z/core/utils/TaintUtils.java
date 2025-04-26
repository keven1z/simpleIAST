package com.keven1z.core.utils;

import com.keven1z.core.model.taint.TaintData;

import com.keven1z.core.consts.SanitizerType;
import com.keven1z.core.model.taint.TaintSink;
import com.keven1z.core.model.taint.TaintSource;
import java.util.*;

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
    public static List<TaintData.FlowPath> getSourceObject(LinkedList<TaintData> taintDataLinkedList) {
        ArrayList<TaintData.FlowPath> sourceObjects = new ArrayList<>();
        for (TaintData taintData : taintDataLinkedList) {
            if (taintData instanceof TaintSource) {
                sourceObjects.add(taintData.getFlowPaths().get(0));
            }
        }
        return sourceObjects;
    }

    /**
     * 计算单个污染源
     */
    public static Object calculateSingleSource(TaintData source, LinkedList<TaintData> taintDataLinkedList) {
//        List<Integer> sourceHashCodes = source.getToObjectHashCodes();
        Object sourceObject = source.getReturnObject();
//        for (TaintData taint : taintDataLinkedList) {
//            if (PolicyType.SOURCE.equals(taint.getStage())) {
//                continue;
//            }
//            Object fromObject = taint.getFromTaint();
//            if (sourceHashCodes.contains(System.identityHashCode(fromObject))) {
//                sourceObject = fromObject;
//            }
//
//        }
        return sourceObject;
    }

    /**
     * 判断给定的TaintDataList中是否包含指定类型的Sanitizer，且数量达到或超过requiredOccurrenceNum。
     *
     * @param taintDataList         包含TaintData对象的列表
     * @param sanitizerType         需要匹配的Sanitizer类型枚举
     * @param requiredOccurrenceNum 需要的Sanitizer数量
     * @return 如果在taintDataList中找到指定类型的Sanitizer数量达到或超过requiredOccurrenceNum，则返回true；否则返回false
     */
    public static boolean containSanitizer(List<TaintData> taintDataList, SanitizerType sanitizerType, int requiredOccurrenceNum) {
//        int occurrenceNum = requiredOccurrenceNum;
//
//        for (TaintData taintData : taintDataList) {
//            if (PolicyType.SANITIZER.name().equals(taintData.getStage()) && sanitizerType.name().equals(taintData.getName())) {
//                occurrenceNum--;
//            }
//
//            List<TaintData> sanitizerNodes = taintData.getSanitizerNodes();
//            if (sanitizerNodes != null) {
//                for (TaintData sanitizerNode : sanitizerNodes) {
//                    if (sanitizerType.name().equals(sanitizerNode.getName())) {
//                        occurrenceNum--;
//                    }
//                }
//            }
//
//            if (occurrenceNum <= 0) {
//                return true;
//            }
//        }
        return false;
    }

    /**
     * 判断给定的TaintDataList中是否连续包含指定类型的Sanitizer，且数量达到或超过requiredOccurrenceNum。
     *
     * @param taintDataList         包含TaintData对象的列表
     * @param sanitizerType         需要匹配的Sanitizer类型枚举
     * @param requiredOccurrenceNum 需要的Sanitizer连续出现的次数
     * @return 如果在taintDataList中存在连续出现的sanitizerTypeEnum类型的Sanitizer数量达到或超过requiredOccurrenceNum，则返回true；否则返回false
     */
    public static boolean containSanitizerInContinuousCode(List<TaintData> taintDataList, SanitizerType sanitizerType, int requiredOccurrenceNum) {
        for (TaintData taintData : taintDataList) {
//            List<TaintData> sanitizerNodes = taintData.getSanitizerNodes();
//            if (sanitizerNodes != null) {
//                int currentOccurrenceNum = requiredOccurrenceNum;
//                for (TaintData sanitizerNode : sanitizerNodes) {
//                    if (sanitizerType.name().equals(sanitizerNode.getName())) {
//                        currentOccurrenceNum--;
//                    }
//                    if (currentOccurrenceNum <= 0) {
//                        return true;
//                    }
//                }
//            }
        }
        return false;
    }
    public static Object getSinkFromObject(TaintData taintData) {
        if (!(taintData instanceof TaintSink)) {
            return null;
        }
        TaintSink taintSink = (TaintSink) taintData;
        List<TaintData.FlowPath> flowPaths = taintSink.getFlowPaths();
        return flowPaths.get(0).getFromObject();
    }
}
