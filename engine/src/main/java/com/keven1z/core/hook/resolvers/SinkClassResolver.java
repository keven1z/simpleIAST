package com.keven1z.core.hook.resolvers;

import com.keven1z.core.graph.taint.TaintData;
import com.keven1z.core.graph.taint.TaintGraph;
import com.keven1z.core.graph.taint.TaintNode;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.PolicyUtils;
import java.util.List;
import static com.keven1z.core.hook.spy.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;
import static com.keven1z.core.hook.spy.HookThreadLocal.isSuspectedTaint;

/**
 * 污染汇聚点的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class SinkClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, Policy policy, boolean isEnter) {
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        if (taintGraph.isEmpty()) {
            return;
        }
        String from = policy.getFrom();
        List<Object> formList = PolicyUtils.getPositionObject(from, parameters, returnObject, thisObject);
        if (formList.isEmpty()) {
            return;
        }

        Object fromObject = formList.get(0);
        TaintNode node = PolicyUtils.searchFromNode(fromObject, taintGraph);
        if (node == null){
            return;
        }
        TaintData taintData = new TaintData(className, method, desc);
        taintData.setType(PolicyTypeEnum.SINK);
        taintData.setVulnType(policyName);
        taintData.setFromValue(fromObject.toString());
        taintData.setTaintValueType(fromObject.getClass().getTypeName());
        taintGraph.addNode(taintData);
        taintGraph.addEdge(node.getTaintData(), taintData);
        isSuspectedTaint.set(true);
    }
}
