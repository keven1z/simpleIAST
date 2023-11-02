package com.keven1z.core.policy;

public enum SanitizerTypeEnum {
    pattern,//正则匹配
    escape,//编码
    replace,//替换
    compare,//比较
    parse,//转换
    feature,//特性，如xxe设置安全特性
}
