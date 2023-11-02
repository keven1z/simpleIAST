package com.keven1z.core.hook.http;

import com.keven1z.core.Config;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.hook.http.body.HttpBodyOutputStream;
import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.hook.http.request.CoyoteRequest;
import com.keven1z.core.hook.http.request.HttpServletRequest;
import com.keven1z.core.hook.http.response.HttpServletResponse;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.vulnerability.FlowProcessingStation;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.lang.spy.SimpleIASTSpy;
import java.util.ArrayList;
import java.util.Objects;

import static com.keven1z.core.hook.HookThreadLocal.*;
import static com.keven1z.core.hook.HookThreadLocal.REQUEST_THREAD_LOCAL;
import static com.keven1z.core.taint.TaintSpy.clear;

public class HttpSpy implements SimpleIASTSpy {
    Logger logger = Logger.getLogger(HttpSpy.class);


    @Override
    public void $_requestStarted(Object requestObject, Object responseObject) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (REQUEST_THREAD_LOCAL.get() != null) {
                return;
            }
            String standardStart = ApplicationModel.getApplicationInfo().get("StandardStart");
            AbstractRequest abstractRequest;
            if (Objects.equals(standardStart, "false")) {
                abstractRequest = new HttpServletRequest(requestObject);
            } else {
                abstractRequest = new CoyoteRequest(requestObject);
            }

            if (filterHttp(abstractRequest.getRequestURLString())) {
                return;
            }

            REQUEST_TIME_CONSUMED.set(System.currentTimeMillis());
            HttpContext httpContext = new HttpContext();
            httpContext.setRequest(abstractRequest);
            httpContext.setResponse(new HttpServletResponse(responseObject));
            REQUEST_THREAD_LOCAL.set(httpContext);
            TAINT_GRAPH_THREAD_LOCAL.set(new TaintGraph());
            SINGLE_FINDING_THREADLOCAL.set(new ArrayList<>());
            if (LogTool.isDebugEnabled()) {
                logger.info("[" + REQUEST_THREAD_LOCAL.get().getRequest().getRequestId() + "] Request Enter,URL:" + abstractRequest.getRequestURLString());
            }
        } catch (Exception e) {
            LogTool.error(ErrorType.REQUEST_START_ERROR, "", e);
        } finally {
            enableHookLock.set(false);
        }

    }

    @Override
    public void $_requestEnded(Object requestObject, Object responseObject) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (isRequestEnd.get() || REQUEST_THREAD_LOCAL.get() == null) {
                return;
            }
            if (!REQUEST_THREAD_LOCAL.get().isHttpExit(requestObject, responseObject)) {
                return;
            }
            System.out.println("[SimpleIAST] 请求消耗时间:" + (System.currentTimeMillis() - REQUEST_TIME_CONSUMED.get()) + "ms");
            REQUEST_TIME_CONSUMED.remove();
            if (LogTool.isDebugEnabled()) {
                logger.info("[" + REQUEST_THREAD_LOCAL.get().getRequest().getRequestId() + "] Request exit,URL:" + REQUEST_THREAD_LOCAL.get().getRequest().getRequestURLString());
            }
            isRequestEnd.set(true);
            FlowProcessingStation.getInstance().doProcess();

        } finally {
            enableHookLock.set(false);
            if (isRequestEnd.get()) {
                clear();
            }
        }
    }

    @Override
    public void $_setRequestBody(Object body) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        if (REQUEST_THREAD_LOCAL.get() == null) {
            return;
        }
        try {
            HttpContext httpContext = REQUEST_THREAD_LOCAL.get();
            if (httpContext.getRequest().getInputStream() == null) {
                httpContext.getRequest().setInputStream(body);
            }
        } catch (Exception e) {
            logger.error("Failed to Get Request Body", e);
        } finally {
            enableHookLock.set(false);
        }

    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            ByteArrayOutputStream bodyOutputStream = getBodyOutputStreamAndSetLength(length, inputStream);
            if (bodyOutputStream != null) {
                bodyOutputStream.write(bytes, off, len);
            }
        } catch (Exception e) {
            logger.warn("Failed to read request body", e);
        } finally {
            enableHookLock.set(false);

        }
    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            ByteArrayOutputStream bodyOutputStream = getBodyOutputStreamAndSetLength(length, inputStream);
            if (bodyOutputStream != null) {
                bodyOutputStream.write(bytes);
            }
        } catch (Exception e) {
            logger.warn("Failed to read request body", e);
        } finally {
            enableHookLock.set(false);
        }
    }

    @Override
    public void $_onReadInvoked(Integer b, Object inputStream) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            ByteArrayOutputStream bodyOutputStream = getBodyOutputStreamAndSetLength(1, inputStream);
            if (bodyOutputStream != null) {
                bodyOutputStream.write(b);
            }
        } catch (Exception e) {
            logger.warn("Failed to read request body", e);
        } finally {
            enableHookLock.set(false);
        }

    }

    private ByteArrayOutputStream getBodyOutputStreamAndSetLength(Integer b, Object inputStream) {
        if (REQUEST_THREAD_LOCAL.get() == null) {
            return null;
        }
        HttpContext httpContext = REQUEST_THREAD_LOCAL.get();
        int inputStreamId = httpContext.getRequest().getInputStreamId();
        if (inputStreamId != System.identityHashCode(inputStream)) {
            return null;
        }
        AbstractRequest request = httpContext.getRequest();
        if (request.getBodyOutputStream() == null) {
            request.setBodyOutputStream(new HttpBodyOutputStream(Config.getConfig().MAX_BODY_SIZE));
        }
        request.setBodyLength(b);
        return request.getBodyOutputStream();
    }

    private final String[] FILTER_HTTP_PREFIX = new String[]{".mvc", ".ico", ".js", ".css", ".jpg", ".gif", "png", ".htm", ".html", ".woff", ".woff2", ".maps", ".map", ".xml"};

    /**
     * 过滤静态页面
     */
    private boolean filterHttp(String url) {
        if (url == null) {
            return true;
        }
        for (String prefix : FILTER_HTTP_PREFIX) {
            if (url.endsWith(prefix)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName) {

    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {

    }
}
