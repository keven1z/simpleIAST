package com.keven1z.core.taint.resolvers;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
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
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, String from, String to) {
        Map<String, Object> fromMap = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromMap ==null || fromMap.isEmpty()) {
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
                taintData.setThisObject(thisObject);
            }
            taintGraph.addEdge(parentNode.getTaintData(), taintData, entry.getKey());
        }
        if (taintData != null) {
            TaintUtils.buildTaint(returnObject, taintData, null, true);
        }
    }
}
