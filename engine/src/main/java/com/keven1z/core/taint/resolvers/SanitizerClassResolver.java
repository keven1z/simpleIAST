package com.keven1z.core.taint.resolvers;

import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.consts.PolicyType;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.TaintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.keven1z.core.hook.HookThreadLocal.SANITIZER_RESOLVER_CACHE;
import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 无害处理阶段的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class SanitizerClassResolver implements HandlerHookClassResolver {
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
            //如果包含在非污点缓存中，直接跳过
            int identityHashCode = System.identityHashCode(fromObject);
            if (SANITIZER_RESOLVER_CACHE.get().contains(identityHashCode)) {
                continue;
            }
            TaintNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
            if (parentNode == null) {
                SANITIZER_RESOLVER_CACHE.get().add(identityHashCode);
                continue;
            }
            TaintData fromTaintData = parentNode.getTaintData();
            fromTaintData.setHasSanitizer(true);

            if (taintData == null) {
                taintData = new TaintData(className, method, desc, PolicyType.SANITIZER);
                taintData.setName(policyName);
//                taintData.setConditions(getConditionString(policy.getConditions(), parameters, returnObject, thisObject));
            }
            taintGraph.addEdge(fromTaintData, taintData, entry.getKey());
            Object toObject = PolicyUtils.getToPositionObject(to, parameters, returnObject, thisObject);
            if (toObject != null) {
                taintData.setToObject(toObject);
            }
            taintData.setFromObject(fromObject);
            TaintUtils.buildTaint(returnObject, taintData, true);
        }
    }


    private String getConditionString(String conditionString, Object[] parameters, Object returnObject, Object thisObject) {
        Map<String, Object> fromPositionObject = PolicyUtils.getFromPositionObject(conditionString, parameters, returnObject, thisObject);
        if (fromPositionObject.isEmpty()) {
            return null;
        }

        List<String> listString = new ArrayList<>();
        for (Map.Entry<String, Object> entry : fromPositionObject.entrySet()) {
            listString.add(String.valueOf(entry.getValue()));
        }
        return String.join(",", listString);
    }
}
