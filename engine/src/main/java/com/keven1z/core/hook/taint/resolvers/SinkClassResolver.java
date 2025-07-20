package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.consts.VulnerabilityType;
import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.model.taint.PathNode;
import com.keven1z.core.consts.HookType;
import com.keven1z.core.model.taint.TaintSink;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.MethodHookConfig;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.StackUtils;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污染汇聚点的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class SinkClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc) {
        MethodHookConfig methodHookConfig = IastHookManager.getManager().getHookMethod(className, method, desc);
        String from = methodHookConfig.getTaintTracking().getTrackingDirection().getFrom();
        FlowObject flowObject = PolicyUtils.getSourceAndSinkFromPositionObject(from, parameters, returnObject, thisObject);
        if (flowObject == null) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        Object fromObject = flowObject.getPathObject();
        PathNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
        if (parentNode == null) {
            return;
        }
        String vulnerabilityType = methodHookConfig.getTaintTracking().getVulnerabilityTypes().get(0);
        TaintData taintData = new TaintSink.Builder()
                .className(className)
                .method(method)
                .desc(desc)
                .parameters(parameters)
                .thisObject(thisObject)
                .addFlowPath(new TaintData.FlowPath(fromObject, null))
                .stage(HookType.SINK)
                .stackList(StackUtils.getParamStackTraceArray())
                .vulnerabilityType(VulnerabilityType.valueOf(vulnerabilityType.toUpperCase()))
                .build();
        PathNode pathNode = taintGraph.addNode(taintData);
        taintGraph.addEdge(parentNode, pathNode, flowObject.getPathFlag());
    }
}
