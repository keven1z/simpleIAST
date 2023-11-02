package com.keven1z.core.taint;

import com.keven1z.core.taint.resolvers.HandlerHookClassResolverInitializer;


public class TaintSpyHandler {
    HandlerHookClassResolverInitializer classResolverInitializer = HandlerHookClassResolverInitializer.getInstance();

    private static class Inner {
        private static final TaintSpyHandler handler = new TaintSpyHandler();
    }

    public static TaintSpyHandler getInstance() {
        return Inner.handler;
    }

    public void doHandle(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName) {
        classResolverInitializer.resolve(returnObject, thisObject, parameters, className, method, desc, type, policyName);
    }
}
