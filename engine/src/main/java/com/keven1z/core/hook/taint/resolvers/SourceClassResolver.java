package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.consts.SourceType;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.model.taint.TaintSource;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.hook.taint.resolvers.source.SourceHandler;
import com.keven1z.core.hook.taint.resolvers.source.SourceHandlerFactory;
import com.keven1z.core.consts.HookType;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.MethodHookConfig;
import com.keven1z.core.utils.*;

import java.util.*;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污染源的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class SourceClassResolver implements HandlerHookClassResolver {
    private static final String[] BLACK_SPRINGFRAMEWORK_RETURN_OBJECT = new String[]{"org.springframework.web", "SecurityContextHolderAwareRequestWrapper"};

    @Override
    public void resolve(Object returnObject,
                        Object thisObject,
                        Object[] parameters,
                        String className,
                        String method,
                        String desc) throws Exception{
        if (!isReturnObjectFiltered(returnObject)) {
            return;
        }

//        FlowObject sourceFromPositionObject = PolicyUtils.getSourceAndSinkFromPositionObject(from, parameters, returnObject, thisObject);
//        if (sourceFromPositionObject == null) {
//            return;
//        }
        MethodHookConfig methodHookConfig = IastHookManager.getManager().getHookMethod(className, method, desc);
        if (methodHookConfig == null) {
            return;
        }
        SourceType sourceType = SourceType.fromName(methodHookConfig.getTaintTracking().getSourceType());
        SourceHandler handler = SourceHandlerFactory.getHandler(sourceType);
        if (handler == null) {
            return;
        }

        List<TaintData.FlowPath> flowPaths;
        flowPaths = handler.handle(thisObject,
                parameters,returnObject);
        if (flowPaths.isEmpty()) {
            return;
        }
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        //去除重复的污染源
        for (TaintData.FlowPath flowPath : flowPaths) {
            if (taintGraph.isTaint(flowPath.getHashcode())) {
                return;
            }
        }

        TaintSource taintSource = new TaintSource.Builder().className(className)
                .method(method)
                .desc(desc)
                .thisObject(thisObject)
                .returnObject(returnObject)
                .parameters(parameters)
                .stackList(StackUtils.getStackTraceArray(true, true))
                .flowPaths(flowPaths)
                .sourceType(sourceType)
                .stage(HookType.SOURCE)
                .build();
        if (taintGraph == null) {
            return;
        }
        taintGraph.addNode(taintSource);
    }


//    private String getSourceFromName(FlowObject flowObject) {
//        if ("org.springframework.web.method.HandlerMethod$HandlerMethodParameter".equals(flowObject.getClass().getName())) {
//            return ReflectionUtils.invokeStringMethod(flowObject, "getParameterName", new Class[]{});
//        } else {
//            return flowObject.toString();
//        }
//    }
//
//    private void resolveBeanHook(String className, String method, String desc, Object returnObject, FlowObject sourceFromPositionObject) {
//        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
//        PathNode parentNode = PolicyUtils.searchParentNode(sourceFromPositionObject.getPathObject(), taintGraph);
//        if (parentNode == null) {
//            return;
//        }
//        TaintData taintData = new TaintSource.Builder()
//                .className(className)
//                .method(method)
//                .desc(desc)
//                .build()
//                ;
//        taintGraph.addEdge(parentNode.getTaintData(), taintData, sourceFromPositionObject.getPathFlag());
//
////        searchAndFillSourceFromReturnObject(returnObject, taintData);
//        taintData.setToObject(returnObject);
//        TaintUtils.buildTaint(returnObject, taintData, PolicyType.SOURCE, true);
//    }
//

//

//

//

    /**
     * @param returnObject 返回污染源
     * @return 判断源是否可以视为污染源，false表示无法作为污染源，true表示可以作为污染源
     */
    private boolean isReturnObjectFiltered(Object returnObject) {
        if (returnObject == null || returnObject.equals("")) {
            return false;
        } else if (returnObject instanceof Collection) {
            if (((Collection<?>) returnObject).isEmpty()) {
                return false;
            }
        } else if (returnObject instanceof Map) {
            if (((Map<?, ?>) returnObject).isEmpty()) {
                return false;
            }
        }
        /*
          过滤HandlerMethodArgumentResolverComposite初始包装request对象
         */
        for (String blackReturnObject : BLACK_SPRINGFRAMEWORK_RETURN_OBJECT) {
            if (returnObject.toString().startsWith(blackReturnObject)) {
                return false;
            }
        }
        return true;
    }
}
