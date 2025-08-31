package com.keven1z.core.hook.taint;

import com.keven1z.core.consts.HookType;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.taint.PathNode;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.model.taint.TaintPropagation;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.TransformerProtector;
import org.apache.log4j.Logger;

import java.lang.spy.SimpleIASTSpy;
import java.util.Arrays;
import java.util.Set;

import static com.keven1z.core.consts.PolicyConst.O;
import static com.keven1z.core.consts.PolicyConst.P;
import static com.keven1z.core.model.Config.MAX_REPORT_QUEUE_SIZE;
import static com.keven1z.core.hook.HookThreadLocal.*;

public class TaintSpy implements SimpleIASTSpy {
    private final TaintSpyHandler spyHandler = TaintSpyHandler.getInstance();
    private static final Logger logger = Logger.getLogger(TaintSpy.class);

    private TaintSpy() {
    }

    public static TaintSpy getInstance() {
        return Inner.taintSpy;
    }

    private static class Inner {
        public static final TaintSpy taintSpy = new TaintSpy();
    }

    /**
     * 对方法返回值、当前对象、方法参数进行taint标记
     *
     * @param returnObject 被污染的对象，即方法的返回值
     * @param thisObject   当前对象
     * @param parameters   方法参数
     * @param className    被hook的类名
     * @param method       被hook的方法名
     * @param desc         方法描述符
     * @param type         taint类型
     */
    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type) {
        if (Boolean.TRUE.equals(enableHookLock.get())) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (shouldProceedWithTaintTracking()) {
                return;
            }

            /*
             * 如果污点图为空，并且不为污染源节点，则不处理
             */
            if (TAINT_GRAPH_THREAD_LOCAL.get().isEmpty() && !HookType.isSource(type)) {
                return;
            }
            spyHandler.doHandle(returnObject, thisObject, parameters, className, method, desc, type);
        } catch (Exception e) {
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to taint", e);
        } finally {
            enableHookLock.set(false);
        }
    }

    @Override
    public void $_arrayTaint(Object arrayObject, int index, Object arrayValue, String className, String method, String desc) {
        if (Boolean.TRUE.equals(enableHookLock.get())) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (shouldProceedWithTaintTracking()) {
                return;
            }
            if (TAINT_GRAPH_THREAD_LOCAL.get().isEmpty()) {
                return;
            }
            if (arrayObject == null || arrayValue == null) {
                return;
            }
            TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
            Set<PathNode> parentNodes = PolicyUtils.searchParentNodes(arrayValue, taintGraph);
            if (parentNodes.isEmpty()) {
                return;
            }
            TaintPropagation taintData = buildTaintPropagation(String.class.getName(), "[]", Arrays.toString((String[])arrayObject).replace("null",arrayValue.toString()), arrayObject, arrayObject,
                    new TaintData.FlowPath(arrayValue, arrayObject));
            for (PathNode parentNode : parentNodes) {
                PathNode pathNode = taintGraph.addNode(taintData);
                taintGraph.addEdge(parentNode, pathNode, O);
            }
        } finally {
            enableHookLock.set(false);
        }
    }

    @Override
    public void $_userBeanTaint(Object returnValue, Object[] parameters, Object thisObject, String className, String method, String desc) {
        if (Boolean.TRUE.equals(enableHookLock.get())) {
            return;
        }
        enableHookLock.set(true);

        try {
            if (shouldProceedWithTaintTracking() || thisObject == null || returnValue == null) {
                return;
            }

            TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
            if (taintGraph.isEmpty()) {
                return;
            }

            PathNode pathNode;
            if (method.startsWith("set") && parameters != null && parameters.length > 0) {
                Set<PathNode> parentNodes = PolicyUtils.searchParentNodes(parameters[0], taintGraph);
                if (!parentNodes.isEmpty()) {
                    TaintPropagation taintData = buildTaintPropagation(className, method, desc, returnValue, thisObject,
                            new TaintData.FlowPath(parameters[0], thisObject));
                    for (PathNode parentNode : parentNodes) {
                        pathNode = taintGraph.addNode(taintData);
                        taintGraph.addEdge(parentNode, pathNode, P);
                    }
                }
            } else if (method.startsWith("get")) {
                Set<PathNode> parentNodes = PolicyUtils.searchParentNodes(thisObject, taintGraph);
                if (!parentNodes.isEmpty()) {
                    TaintPropagation taintData = buildTaintPropagation(className, method, desc, returnValue, thisObject,
                            new TaintData.FlowPath(thisObject, returnValue));
                    for (PathNode parentNode : parentNodes) {
                        pathNode = taintGraph.addNode(taintData);
                        taintGraph.addEdge(parentNode, pathNode, O);
                    }
                }
            }
        } finally {
            enableHookLock.set(false);
        }
    }

    private TaintPropagation buildTaintPropagation(String className, String method, String desc,
                                                   Object returnObject, Object thisObject, TaintData.FlowPath flowPath) {
        return new TaintPropagation.Builder()
                .className(className)
                .method(method)
                .desc(desc)
                .returnObject(returnObject)
                .thisObject(thisObject)
                .stage(HookType.PROPAGATION)
                .addFlowPath(flowPath)
                .build();
    }

    /**
     * 判断是否应该继续进行污点追踪
     *
     * @return 如果满足继续污点追踪的条件，则返回true；否则返回false
     */
    private boolean shouldProceedWithTaintTracking() {
        /*
         * 判断agent是否开启，若关闭不进行hook
         */
        /*
         * 判断agent是否开启，若关闭不进行hook
         */
        if (!ApplicationModel.isRunning()) {
            return true;
        }
        if (TransformerProtector.instance.isInProtecting()) {
            return true;
        }
        //如果没有流量，不进行hook
        if (REQUEST_THREAD_LOCAL.get() == null) {
            return true;
        }
        if (isRequestEnded.get()) {
            return true;
        }
        /*
         * 如果上报线程满了，不进行hook
         */
        if (REPORT_QUEUE.size() >= MAX_REPORT_QUEUE_SIZE) {
            if (LogTool.isDebugEnabled()) {
                logger.warn("上报队列已满,目前队列大小：" + REPORT_QUEUE.size());
            }
            return true;
        }

        if (TAINT_GRAPH_THREAD_LOCAL.get() == null) {
            logger.warn("Taint graph is null, skip taint tracking");
            return true;
        }

        return false;
    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {

    }

    @Override
    public void $_requestStarted(Object requestObject, Object responseObject) {
    }

    @Override
    public void $_requestEnded(Object requestObject, Object responseObject) {
    }

    @Override
    public void $_setRequestBody(Object body) {
    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) {
    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) {
    }

    @Override
    public void $_onReadInvoked(Integer b, Object inputStream) {
    }

    public void clear() {
        TAINT_GRAPH_THREAD_LOCAL.remove();
        SANITIZER_RESOLVER_CACHE.remove();
        isRequestStart.set(false);
        isRequestEnded.set(false);
        REQUEST_THREAD_LOCAL.remove();
        INVOKE_ID.set(INVOKE_ID_INIT_VALUE);
        SINGLE_FINDING_THREADLOCAL.remove();
    }
}
