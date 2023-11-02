package com.keven1z.core.taint.resolvers;

/**
 * 通过解析器集合解析hook class
 * @author keven1z
 * @date 2023/01/15
 */
public interface HandlerHookClassResolver {

    void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName);
}
