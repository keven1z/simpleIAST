package com.keven1z.core.consts;

public enum SanitizerType {
    pattern,//正则匹配
    escape,//编码
    replace,//替换
    compare,//比较
    parse,//转换
    sub,//切割
    feature,//特性，如xxe设置安全特性
}
