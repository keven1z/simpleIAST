package com.keven1z.core.hook.taint;

import com.keven1z.core.hook.taint.resolvers.HandlerHookClassResolverInitializer;


public class TaintSpyHandler {
    HandlerHookClassResolverInitializer classResolverInitializer = HandlerHookClassResolverInitializer.getInstance();

    private static class Inner {
        private static final TaintSpyHandler handler = new TaintSpyHandler();
    }

    public static TaintSpyHandler getInstance() {
        return Inner.handler;
    }

    public void doHandle(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc,String type) {
        classResolverInitializer.resolve(returnObject, thisObject, parameters, className, method, desc, type);
    }
}
