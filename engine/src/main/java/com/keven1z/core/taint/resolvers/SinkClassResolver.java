package com.keven1z.core.taint.resolvers;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.StackUtils;

import java.util.List;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污染汇聚点的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class SinkClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName) {

        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.policyContainer.getSink());
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the sink policy,className:" + className + ",method:" + method + ",desc:" + desc);
            }
            return;
        }

        String from = policy.getFrom();
        List<Object> formList = PolicyUtils.getPositionObject(from, parameters, returnObject, thisObject);
        if (formList.isEmpty()) {
            return;
        }

        Object fromObject = formList.get(0);
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        TaintNode node = PolicyUtils.searchFromNode(fromObject, taintGraph);
        if (node == null) {
            return;
        }

        TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SINK);
        taintData.setReturnObjectString(returnObject == null ? null : returnObject.toString());
        taintData.setReturnObjectType(returnObject == null ? null : returnObject.getClass().getName());
        taintData.setVulnType(policyName);
        taintData.setFromValue(fromObject.toString());
        taintData.setStackList(StackUtils.getStackTraceArray(true, true));
        taintData.setTaintValueType(fromObject.getClass().getTypeName());
        taintGraph.addNode(taintData);
        taintGraph.addEdge(node.getTaintData(), taintData);
    }
}
