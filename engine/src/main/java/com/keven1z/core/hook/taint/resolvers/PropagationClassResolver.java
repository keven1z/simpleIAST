package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.model.taint.PathNode;
import com.keven1z.core.consts.HookType;
import com.keven1z.core.model.taint.TaintPropagation;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.MethodHookConfig;
import com.keven1z.core.utils.PolicyUtils;

import java.util.List;
import java.util.Set;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污点传播阶段的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class PropagationClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject,
                        Object thisObject,
                        Object[] parameters,
                        String className,
                        String method,
                        String desc) throws Exception{
        MethodHookConfig methodHookConfig = IastHookManager.getManager().getHookMethod(className, method, desc);
        MethodHookConfig.TaintTracking taintTracking = methodHookConfig.getTaintTracking();
        if (taintTracking == null) {
            return;
        }
        String from = taintTracking.getTrackingDirection().getFrom();
        List<FlowObject> fromPositionObjects = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromPositionObjects.isEmpty()) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        TaintData taintData = null;
        for (FlowObject flowObject : fromPositionObjects) {
            Object fromObject = flowObject.getPathObject();
            Set<PathNode> parentNodes = PolicyUtils.searchParentNodes(fromObject, taintGraph);
            if (parentNodes == null || parentNodes.isEmpty()) {
                continue;
            }
            Object toObject = PolicyUtils.getToPositionObject(taintTracking.getTrackingDirection().getTo(), parameters, returnObject, thisObject);
            if (toObject == null) {
                return;
            }
            for (PathNode parentNode : parentNodes) {
                if (taintData == null) {
                    taintData = new TaintPropagation.Builder()
                            .className(className)
                            .method(method)
                            .desc(desc)
                            .returnObject(returnObject)
                            .thisObject(thisObject)
                            .parameters(parameters)
                            .stage(HookType.PROPAGATION)
                            .addFlowPath(new TaintData.FlowPath(fromObject, toObject))
                            .build();
                }
                PathNode pathNode = taintGraph.addNode(taintData);
                taintGraph.addEdge(parentNode, pathNode, flowObject.getPathFlag());
            }
        }
    }
}
