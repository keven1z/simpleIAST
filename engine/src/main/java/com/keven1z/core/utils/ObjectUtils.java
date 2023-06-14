package com.keven1z.core.utils;

import java.util.List;

/**
 * @author keven1z
 * @date 2023/02/06
 */
public class ObjectUtils {
    /**
     * 判断元素是否在集合中
     *
     * @param element
     * @param list
     */
    public static boolean isInList(String element, List<String> list) {
        if (element == null || list == null || list.isEmpty()) {
            return false;
        }
        for (String l : list) {
            if (element.equals(l)) {
                return true;
            }
        }
        return false;
    }
}
