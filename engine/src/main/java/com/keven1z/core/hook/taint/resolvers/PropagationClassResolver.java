package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.consts.CleaningMode;
import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.model.taint.PathNode;
import com.keven1z.core.consts.HookType;
import com.keven1z.core.model.taint.TaintPropagation;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.MethodHookConfig;
import com.keven1z.core.utils.PolicyUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.keven1z.core.consts.CleaningScope.ALL;
import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污点传播阶段的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class PropagationClassResolver implements HandlerHookClassResolver {
    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public void resolve(Object returnObject,
                        Object thisObject,
                        Object[] parameters,
                        String className,
                        String method,
                        String desc) {
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
        for (FlowObject flowObject : fromPositionObjects) {
            Object fromObject = flowObject.getPathObject();
            Set<PathNode> parentNodes = PolicyUtils.searchParentNodes(fromObject, taintGraph);
            if (parentNodes.isEmpty()) {
                continue;
            }
            Object toObject = PolicyUtils.getToPositionObject(taintTracking.getTrackingDirection().getTo(), parameters, returnObject, thisObject);
            if (toObject == null) {
                return;
            }
            TaintPropagation taintData = new TaintPropagation.Builder()
                    .className(className)
                    .method(method)
                    .desc(desc)
                    .returnObject(returnObject)
                    .thisObject(thisObject)
                    .parameters(parameters)
                    .stage(HookType.PROPAGATION)
                    .addFlowPath(new TaintData.FlowPath(fromObject, toObject))
                    .build();
            Map<String, String> cleaningEffect = taintTracking.getCleaningEffect();
            if (cleaningEffect != null && !cleaningEffect.isEmpty()) {
                for(Map.Entry<String,String> kv : cleaningEffect.entrySet()) {
                    String key = kv.getKey();
                    String value = kv.getValue();
                    CleaningMode cleaningMode = CleaningMode.fromString(value);
                    // 如果设置全部清除，则直接删除该污点路径，不再继续传播
                    if (ALL.equals(key) && cleaningMode == CleaningMode.FULL) {
                        return;
                    }
                    taintData.addCleaningEffects(key, cleaningMode);
                }
            }

            for (PathNode parentNode : parentNodes) {
                PathNode pathNode = taintGraph.addNode(taintData);
                taintGraph.addEdge(parentNode, pathNode, flowObject.getPathFlag());
            }
        }
    }
}
