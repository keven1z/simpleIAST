package com.keven1z.core.hook.http.handlers;


import com.keven1z.core.Config;
import com.keven1z.core.graph.taint.TaintGraph;
import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.hook.http.request.CoyoteRequest;
import com.keven1z.core.hook.http.request.HttpServletRequest;
import com.keven1z.core.hook.spy.HookThreadLocal;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.ReflectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import static com.keven1z.core.hook.spy.HookThreadLocal.*;

/**
 * @author keven1z
 * @date 2023/01/15
 */
public class TomcatHttpHandler implements HttpRequestHandler {

    private static boolean filterHttp(String url) {
        for (String prefix : FILTER_HTTP_PREFIX) {
            if (url.endsWith(prefix)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void handler(Object thisObject, String method, String desc, Object[] parameters, boolean isEnter) {
        if ("service".equals(method)) {
            handlerRR(parameters, isEnter);
        } else if ("endRequest".equals(method)) {
            if (REQUEST_THREAD_LOCAL.get() != null) {
                this.OnRequestExit(thisObject);
            }
        }
    }

    /**
     * 处理request和response
     */
    private void handlerRR(Object[] parameters, boolean isEnter) {
        if (isEnter) {
            if (REQUEST_THREAD_LOCAL.get() == null) {
                this.OnRequestEnter(parameters[0], parameters[1]);
            }
        }
    }

    /**
     * @param request  请求对象
     * @param response 响应对象
     */
    private void OnRequestEnter(Object request, Object response) {
        String standardStart = ApplicationModel.getApplicationInfo().get("StandardStart");
        AbstractRequest abstractRequest;
        if (Objects.equals(standardStart, "false")) {
            abstractRequest = new HttpServletRequest(request);
        } else {
            abstractRequest = new CoyoteRequest(request);
        }
        if (filterHttp(abstractRequest.getRequestURLString())) {
            return;
        }
        REQUEST_THREAD_LOCAL.set(abstractRequest);
        TAINT_GRAPH_THREAD_LOCAL.set(new TaintGraph());
        if (LogTool.isDebugEnabled()) {
            logger.info("Request enter,url:" + abstractRequest.getRequestURLString());
        }
    }

    private void OnRequestExit(Object thisObject) {
        if (LogTool.isDebugEnabled()) {
            logger.info("Request exit");
        }
        isRequestEnd.set(true);
        //如果没有漏洞不获取报文
        if (!isSuspectedTaint.get()) {
            return;
        }
        try {
            Object inputBuffer = ReflectionUtils.getField(thisObject, "inputBuffer");
            Object byteBuffer = ReflectionUtils.invokeMethod(inputBuffer, "getByteBuffer", AbstractRequest.EMPTY_CLASS);
            byte[] bytes = (byte[]) ReflectionUtils.invokeMethod(byteBuffer, "array", AbstractRequest.EMPTY_CLASS);
            int limit = (Integer) ReflectionUtils.invokeMethod(byteBuffer, "limit", AbstractRequest.EMPTY_CLASS);
            limit = Math.min(limit, Config.MAX_REQUEST_MESSAGE_LENGTH);
            bytes = Arrays.copyOfRange(bytes, 0, limit);
            REQUEST_THREAD_LOCAL.get().setRequestDetail(bytes);
        } catch (Exception e) {
            logger.error("Failed to Get Request Body", e);
        }
    }
}
