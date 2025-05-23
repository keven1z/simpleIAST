package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.consts.PolicyType;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * @author keven1z
 * @since 2023/01/15
 * Hook分发初始化类
 */
public class HandlerHookClassResolverInitializer {

    /**
     * 分发处理类map
     */
    private final Map<PolicyType, HandlerHookClassResolver> resolverMap = new ConcurrentHashMap<>();

    private HandlerHookClassResolverInitializer() {
        bind();
    }

    private static class Inner {
        public static final HandlerHookClassResolverInitializer INSTANCE = new HandlerHookClassResolverInitializer();
    }

    public static HandlerHookClassResolverInitializer getInstance() {
        return Inner.INSTANCE;
    }

    /**
     * 绑定不同类型的分发类
     */
    private void bind() {
        resolverMap.put(PolicyType.SOURCE, new SourceClassResolver());
        resolverMap.put(PolicyType.PROPAGATION, new PropagationClassResolver());
        resolverMap.put(PolicyType.SINK, new SinkClassResolver());
        resolverMap.put(PolicyType.SANITIZER, new SanitizerClassResolver());

    }

    /**
     * 解析污点数据，根据污点类型调用相应的解析器进行处理
     *
     * @param returnValue 方法的返回值
     * @param thisObject 当前对象
     * @param parameters 方法的参数
     * @param className 方法的类名
     * @param method 方法的名称
     * @param desc 方法的描述符
     * @param type 污点类型
     * @param policyName 污点策略名称
     * @param from 污点来源
     * @param to 污点去向
     */
    public void resolve(Object returnValue, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, String from, String to) {
        /*
         * 如果污点图为空，并且不为污染源节点，则不处理
         */
        if (TAINT_GRAPH_THREAD_LOCAL.get() == null) {
            return;
        }
        if (TAINT_GRAPH_THREAD_LOCAL.get().isEmpty() && !PolicyType.SOURCE.name().equals(type)) {
            return;

        }

        /*
         * 由于存在大量的jar包初始化的调用，需要进行hook点过滤
         */
        if (isHookFiltered(thisObject, returnValue)) {
            return;
        }

        HandlerHookClassResolver resolver = resolverMap.get(PolicyType.valueOf(type.toUpperCase()));
        if (resolver == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.RESOLVE_ERROR, "Not found resolver,policy type is " + type);
            }
            return;
        }

        try {
            resolver.resolve(returnValue, thisObject, parameters, className, method, desc, policyName, from, to);
        } catch (Exception e) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.RESOLVE_ERROR, "Resolver error", e);
            }
        }
    }

    /**
     * 过滤部分hook操作
     *
     * @param thisObject  hook对象
     * @param returnValue hook对象返回值
     */
    private boolean isHookFiltered(Object thisObject, Object returnValue) {
        if (returnValue instanceof String) {
            String returnString = (String) returnValue;
            return returnString.startsWith("jar");

        } else if (thisObject instanceof URL) {
            URL url = (URL) thisObject;
            String protocol = url.getProtocol();
            return "jar".equals(protocol) || "file".equals(protocol) || "jrt".equals(protocol);

        } else if (returnValue instanceof StringBuilder) {
            StringBuilder returnString = (StringBuilder) returnValue;
            return returnString.toString().startsWith("jar") || returnString.toString().startsWith("file");
        }
        return false;
    }
}
