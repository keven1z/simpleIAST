package com.keven1z.core.consts;

public enum HookType {
    NONE,
    HTTP,
    SOURCE,
    PROPAGATION,
    SINK,
    SANITIZER,
    SINGLE;
    public static boolean isSource(String type) {
        return HookType.valueOf(type.toUpperCase()) == SOURCE;
    }
}
