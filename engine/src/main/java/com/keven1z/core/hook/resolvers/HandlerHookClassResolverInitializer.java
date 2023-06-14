package com.keven1z.core.hook.resolvers;

import com.keven1z.core.hook.spy.HookSpy;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.PolicyUtils;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keven1z
 * @date 2023/01/15
 * Hook分发初始化类
 */
public class HandlerHookClassResolverInitializer {

    /**
     * 分发处理类map
     */
    private final Map<PolicyTypeEnum, HandlerHookClassResolver> resolverMap = new ConcurrentHashMap<>();

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
        resolverMap.put(PolicyTypeEnum.HTTP, new HTTPClassResolver());
        resolverMap.put(PolicyTypeEnum.SOURCE, new SourceClassResolver());
        resolverMap.put(PolicyTypeEnum.PROPAGATION, new PropagationClassResolver());
        resolverMap.put(PolicyTypeEnum.SINK, new SinkClassResolver());
        resolverMap.put(PolicyTypeEnum.SANITIZER, new SanitizerClassResolver());

    }

    public void resolve(Object returnValue, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isEnter) {
        PolicyContainer policyContainer = HookSpy.policyContainer;
        if (isHookFiltered(thisObject, returnValue)) {
            return;
        }
        HandlerHookClassResolver resolver = resolverMap.get(PolicyTypeEnum.valueOf(type));
        if (resolver == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.RESOLVE_ERROR, "Not found resolver,policy type is " + type);
            }
            return;
        }

        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, policyContainer);
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the policy,className:" + className + ",method:" + method + ",desc:" + desc);
            }
            return;
        }
        try {
            resolver.resolve(returnValue, thisObject, parameters, className, method, desc, policyName, policy, isEnter);

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
