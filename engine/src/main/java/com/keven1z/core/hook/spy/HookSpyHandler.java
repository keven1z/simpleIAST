package com.keven1z.core.hook.spy;

import com.keven1z.core.graph.taint.TaintGraph;
import com.keven1z.core.graph.taint.TaintNode;
import com.keven1z.core.hook.resolvers.HandlerHookClassResolverInitializer;
import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.vulnerability.report.ReportMessage;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.keven1z.core.hook.spy.HookSpy.clear;
import static com.keven1z.core.hook.spy.HookThreadLocal.*;

public class HookSpyHandler {
    HandlerHookClassResolverInitializer classResolverInitializer = HandlerHookClassResolverInitializer.getInstance();

//    private static final DetectAndReportThreadPoolClient reportClient = DetectAndReportThreadPoolClient.getInstance();

    private static class Inner {
        private static final HookSpyHandler handler = new HookSpyHandler();
    }

    public static HookSpyHandler getInstance() {
        return Inner.handler;
    }

    public void doHandle(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isEnter) {
        classResolverInitializer.resolve(returnObject, thisObject, parameters, className, method, desc, type, policyName, isEnter);

        if (!isRequestEnd.get()) {
            return;
        }
        try {
            if (!isSuspectedTaint.get()) {
                if (LogTool.isDebugEnabled()) {
                    Logger.getLogger(getClass()).info("Not found vulnerability，url:" + REQUEST_THREAD_LOCAL.get().getRequestURL());
                }
                return;
            }

            TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
            List<TaintNode> sinkNodes = taintGraph.getSinkNode();
            if (sinkNodes.isEmpty()) {
                return;
            }

            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(getClass()).info("The vulnerability is found,url:" + REQUEST_THREAD_LOCAL.get().getRequestURL() + ",TaintGraph size:" + taintGraph.getNodeSize() + ",vulnerability type:" + sinkNodes.get(0).getTaintData().getVulnType());
            }

            AbstractRequest request = REQUEST_THREAD_LOCAL.get();
            String agentId = ApplicationModel.getAgentId();
            ReportMessage reportMessage = new ReportMessage(agentId, request.getRequestURL().toString(), request.getMethod(), taintGraph);
            reportMessage.setHttpMessage(Base64.getEncoder().encode(request.getRequestDetail()));
//            reportMessage.setHeaders(request.getHeaders());
            boolean isOffer = REPORT_QUEUE.offer(reportMessage);
            if (!isOffer) {
                taintGraph.clear();
            }

        } finally {
            clear();
        }
    }
}
