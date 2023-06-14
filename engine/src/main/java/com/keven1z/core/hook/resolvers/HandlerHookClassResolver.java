package com.keven1z.core.hook.resolvers;

import com.keven1z.core.policy.Policy;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过解析器集合解析hook class
 * @author keven1z
 * @date 2023/01/15
 */
public interface HandlerHookClassResolver {

    void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, Policy policy, boolean isEnter);
}
