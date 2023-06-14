package com.keven1z.core.hook.resolvers;

import com.keven1z.core.graph.taint.TaintData;
import com.keven1z.core.graph.taint.TaintGraph;
import com.keven1z.core.graph.taint.TaintNode;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.PolicyUtils;

import java.util.List;

import static com.keven1z.core.hook.spy.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污点传播阶段的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class PropagationClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, Policy policy, boolean isEnter) {
        //如果污点图为空，则不进去传播过程的构建，仅有污染源时，才进行图的构建
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        if (taintGraph.isEmpty()) {
            return;
        }

        String from = policy.getFrom();
        List<Object> fromList = PolicyUtils.getPositionObject(from, parameters, returnObject, thisObject);
        if (fromList.isEmpty()) {
            return;
        }
        for (Object fromObject : fromList) {
            TaintNode taintNode = PolicyUtils.searchFromNode(fromObject, taintGraph);
            if (taintNode == null) {
                continue;
            }
            TaintData taintData = new TaintData(className, method, desc);
            taintData.setType(PolicyTypeEnum.PROPAGATION);

            List<Object> toList = PolicyUtils.getPositionObject(policy.getTo(), parameters, returnObject, thisObject);
            if (toList.isEmpty()) {
                return;
            }
            Object toObject = toList.get(0);
            taintData.setToObjectHashCode(System.identityHashCode(toObject));

            if (toObject instanceof String) {
                taintData.setToValue(toObject.toString());
            }
            taintData.setFromValue(fromObject.toString());
            taintData.setFromObjectHashCode(System.identityHashCode(fromObject));
            taintData.setToValue(toObject.toString());
            taintGraph.addNode(taintData);
            taintGraph.addEdge(taintNode.getTaintData(), taintData);
        }
    }
}
