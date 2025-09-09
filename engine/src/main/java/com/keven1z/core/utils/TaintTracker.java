package com.keven1z.core.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author keven1z
 * @date 2025/9/5
 */
public class TaintTracker {

    // 每个请求单独维护一个 Set
    private static final ThreadLocal<Set<Integer>> taintHashSet =
            ThreadLocal.withInitial(HashSet::new);

    /** 记录污点 hash */
    public static void addTaint(int hash) {
        taintHashSet.get().add(hash);
    }

    /** 检查是否已经存在 */
    public static boolean containsTaint(int hash) {
        return taintHashSet.get().contains(hash);
    }

    /** 获取当前请求的所有污点 hash */
    public static Set<Integer> getTaints() {
        return taintHashSet.get();
    }

    /** 请求结束时清理 */
    public static void clear() {
        taintHashSet.remove();
    }
}
