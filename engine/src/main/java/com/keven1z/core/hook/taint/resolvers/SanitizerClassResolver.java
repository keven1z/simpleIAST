package com.keven1z.core.hook.taint.resolvers;

import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.utils.PolicyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 无害处理阶段的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class SanitizerClassResolver implements HandlerHookClassResolver {
    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc) {
//        List<FlowObject> fromPositionObjects = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
//        if (fromPositionObjects.isEmpty()) {
//            return;
//        }
//
//        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
//        TaintData taintData = null;
//
//        for (FlowObject flowObject : fromPositionObjects) {
//            Object fromObject = flowObject.getPathObject();
//            //如果包含在非污点缓存中，直接跳过
//            int identityHashCode = System.identityHashCode(fromObject);
//            if (SANITIZER_RESOLVER_CACHE.get().contains(identityHashCode)) {
//                continue;
//            }
//            PathNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
//            if (parentNode == null) {
//                SANITIZER_RESOLVER_CACHE.get().add(identityHashCode);
//                continue;
//            }
//            TaintData fromTaintData = parentNode.getTaintData();
////            fromTaintData.setHasSanitizer(true);
//
//            if (taintData == null) {
//                taintData = new TaintData(className, method, desc, PolicyType.SANITIZER);
//                taintData.setName(policyName);
////                taintData.setConditions(getConditionString(policy.getConditions(), parameters, returnObject, thisObject));
//            }
//            PathNode pathNode = taintGraph.addNode(taintData, PolicyType.SANITIZER);
//            taintGraph.addEdge(parentNode, pathNode, flowObject.getPathFlag());
//            Object toObject = PolicyUtils.getToPositionObject(to, parameters, returnObject, thisObject);
//            if (toObject != null) {
//                taintData.setToObject(toObject);
//            }
//            taintData.setFromObject(fromObject);
//            TaintUtils.buildTaint(returnObject, taintData, PolicyType.SANITIZER,true);
//        }
    }


    private String getConditionString(String conditionString, Object[] parameters, Object returnObject, Object thisObject) {
        List<FlowObject> fromPositionObjects = PolicyUtils.getFromPositionObject(conditionString, parameters, returnObject, thisObject);
        if (fromPositionObjects.isEmpty()) {
            return null;
        }

        List<String> listString = new ArrayList<>();
        for (FlowObject flowObject : fromPositionObjects) {
            listString.add(String.valueOf(flowObject.getPathObject()));
        }
        return String.join(",", listString);
    }
}
