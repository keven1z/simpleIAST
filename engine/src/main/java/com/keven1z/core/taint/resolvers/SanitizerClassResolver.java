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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 无害处理阶段的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class SanitizerClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName) {
        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.getInstance().getPolicyContainer().getSanitizers());
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the policy,className:" + className + ",method:" + method + ",desc:" + desc);
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
        Object toObject = PolicyUtils.getToPositionObject(policy.getTo(), parameters, returnObject, thisObject);

        for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
            Object fromObject = entry.getValue();
            TaintNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
            if (parentNode == null) {
                continue;
            }
            TaintData fromTaintData = parentNode.getTaintData();
            fromTaintData.setSanitizer(true);

            if (taintData == null) {
                taintData = new TaintData(className, method, desc, PolicyTypeEnum.SANITIZER);
                taintData.setName(policyName);
                taintData.setConditions(getConditionString(policy.getConditions(), parameters, returnObject, thisObject));
                if (toObject != null) {
                    taintData.setToObjectHashCode(System.identityHashCode(toObject));
                }
            }
            taintGraph.addEdge(fromTaintData, taintData, entry.getKey());
        }

        if (taintData != null) {
            TaintUtils.buildTaint(returnObject, taintData, toObject, true);
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
