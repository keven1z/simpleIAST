package com.keven1z.core.consts;

import java.util.Locale;

/**
 * 污点清理模式
 */
public enum CleaningMode {
    /**
     * 部分影响污染源
     */
    PARTIAL,
    /**
     * 污染源完全影响
     */
    FULL,
    /**
     * 不影响污染源,仅记录,综合判断
     */
    LOG_ONLY;
    public static CleaningMode fromString(String modeName) {
        if (modeName == null) {
            return CleaningMode.LOG_ONLY;
        }
        try {
            return CleaningMode.valueOf(modeName.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return CleaningMode.LOG_ONLY;
        }
    }
}
