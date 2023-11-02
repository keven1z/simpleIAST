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

import java.util.ArrayList;
import java.util.List;

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
        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.policyContainer.getSanitizers());
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
        Object fromObject = fromList.get(0);
        TaintNode fromTaintNode = PolicyUtils.searchFromNode(fromObject, taintGraph);
        if (fromTaintNode == null) {
            return;
        }
        TaintData fromTaintData = fromTaintNode.getTaintData();
        fromTaintData.setSanitizer(true);
        //提取过滤条件值
        String conditions = null;
        List<Object> positionObjects = PolicyUtils.getPositionObject(policy.getConditions(), parameters, returnObject, thisObject);
        if (!positionObjects.isEmpty()) {
            List<String> listString = new ArrayList<>();
            for (Object object : positionObjects) {
                listString.add(String.valueOf(object));
            }
            conditions = String.join(",", listString);
        }
        TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SANITIZER);
        taintData.setConditions(conditions);
        taintData.setName(policyName);
        taintData.setReturnValue(returnObject == null ? null : returnObject.toString());

        List<Object> toList = PolicyUtils.getPositionObject(policy.getTo(), parameters, returnObject, thisObject);
        if (!toList.isEmpty()) {
            taintData.setToObjectHashCode(System.identityHashCode(toList.get(0)));
        }

        taintData.setFromValue(fromObject.toString());
        taintGraph.addNode(taintData);
        taintGraph.addEdge(fromTaintData, taintData);
    }
}
