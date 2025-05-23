package com.keven1z.core.utils;

import com.keven1z.core.model.Config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author keven1z
 * @since 2023/10/15
 */
public class StackUtils {
    /**
     * 获取栈信息
     *
     * @return 栈信息
     */
    public static String getStackTrace(boolean isFilter) {

        Throwable throwable = new Throwable();
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        StringBuilder retStack = new StringBuilder();
        if (isFilter) {
            stackTraceElements = filter(stackTraceElements, Config.MAX_STACK_DEPTH);
        }
        //此处前几个调用栈都是插件中产生的所以跳过，只显示客户自己的调用栈
        if (stackTraceElements.length >= 3) {
            for (int i = 2; i < stackTraceElements.length; i++) {
                retStack.append(stackTraceElements[i].getClassName()).append("@").append(stackTraceElements[i].getMethodName()).append("(").append(stackTraceElements[i].getLineNumber()).append(")").append("\r\n");
            }
        } else {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                retStack.append(stackTraceElement.getClassName()).append("@").append(stackTraceElement.getMethodName()).append("(").append(stackTraceElement.getLineNumber()).append(")").append("\r\n");
            }
        }

        return retStack.toString();
    }

    /**
     * 获取原始栈
     *
     * @return 原始栈
     */
    public static List<String> getStackTraceArray(boolean isFilter, boolean hasLineNumber) {
        LinkedList<String> stackTrace = new LinkedList<>();
        Throwable throwable = new Throwable();
        StackTraceElement[] stack = throwable.getStackTrace();
        if (stack != null) {
            if (isFilter) {
                stack = filter(stack, Config.MAX_STACK_DEPTH);
            }
            for (StackTraceElement stackTraceElement : stack) {
                if (hasLineNumber) {
                    stackTrace.add(stackTraceElement.toString());
                } else {
                    stackTrace.add(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
                }
            }
        }

        return stackTrace;
    }

    /**
     * hook 点参数获取原始栈
     *
     * @return 原始栈
     */
    public static List<String> getParamStackTraceArray() {
        return getStackTraceArray(true, false);
    }


    //去掉包含rasp的堆栈
    public static StackTraceElement[] filter(StackTraceElement[] trace, int maxStackDepth) {
        int i = 0;
        // 去除插件本身调用栈
        while (i < trace.length && (trace[i].getClassName().startsWith("com.keven1z") || trace[i].getClassName().startsWith("java.lang.spy")
                || trace[i].getClassName().contains("reflect"))) {
            i++;
        }
        return Arrays.copyOfRange(trace, i, Math.min(i + maxStackDepth, trace.length));
    }
}