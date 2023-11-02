package com.keven1z.core.taint.resolvers;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.utils.PolicyUtils;

import java.util.List;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污点传播阶段的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class PropagationClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName) {
        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.policyContainer.getPropagation());
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the policy,className:" + className + ",method:" + method + ",desc:" + desc);
            }
            return;
        }

        String from = policy.getFrom();
        List<Object> fromList = PolicyUtils.getPositionObject(from, parameters, returnObject, thisObject);
        if (fromList.isEmpty()) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        for (Object fromObject : fromList) {
            TaintNode taintNode = PolicyUtils.searchFromNode(fromObject, taintGraph);
            if (taintNode == null) {
                continue;
            }
            TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.PROPAGATION);

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
