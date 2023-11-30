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
import com.keven1z.core.utils.TaintUtils;

import java.util.Map;

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

        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.getInstance().getPolicyContainer().getSink());
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the sink policy,className:" + className + ",method:" + method + ",desc:" + desc);
            }
            return;
        }

        String from = policy.getFrom();
        Map<String, Object> fromMap = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromMap.isEmpty()) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        TaintData taintData = null;
        for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
            Object fromObject = entry.getValue();
            TaintNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
            if (parentNode == null) {
                continue;
            }
            if (taintData == null) {
                taintData = new TaintData(className, method, desc, PolicyTypeEnum.SINK);
                taintData.setFromValue(fromObject.toString());
                taintData.setVulnType(policyName);
            }
            taintGraph.addEdge(parentNode.getTaintData(), taintData, entry.getKey());
        }
        if (taintData != null) {
            TaintUtils.buildTaint(returnObject, taintData, null, true);
        }
    }
}
