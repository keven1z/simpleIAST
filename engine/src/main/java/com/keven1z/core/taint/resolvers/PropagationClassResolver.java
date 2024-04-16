package com.keven1z.core.taint.resolvers;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.TaintUtils;

import java.util.Map;
import java.util.Set;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污点传播阶段的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class PropagationClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, String from, String to) {
        Map<String, Object> fromMap = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromMap == null || fromMap.isEmpty()) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        TaintData taintData = null;
        for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
            Object fromObject = entry.getValue();
            Set<TaintNode> parentNodes = PolicyUtils.searchParentNodes(fromObject, taintGraph);
            if (parentNodes == null || parentNodes.isEmpty()) {
                continue;
            }
            for (TaintNode parentNode : parentNodes) {
                if (taintData == null) {
                    taintData = new TaintData(className, method, desc, PolicyTypeEnum.PROPAGATION);
                }
                taintData.setFromValue(fromObject.toString());
                taintGraph.addEdge(parentNode.getTaintData(), taintData, entry.getKey());
            }
        }
        if (taintData != null) {
            Object toObject = PolicyUtils.getToPositionObject(to, parameters, returnObject, thisObject);
            if (toObject == null) {
                return;
            }
            taintData.setToObject(toObject);
            TaintUtils.buildTaint(returnObject, taintData, false);
        }

    }
}
