package com.keven1z.core.hook.http;

import com.keven1z.core.model.Config;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.hook.http.body.HttpBodyOutputStream;
import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.hook.http.request.CoyoteRequest;
import com.keven1z.core.hook.http.request.HttpServletRequest;
import com.keven1z.core.hook.http.response.HttpServletResponse;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.hook.taint.TaintSpy;
import com.keven1z.core.vulnerability.DetectorFactory;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.lang.spy.SimpleIASTSpy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static com.keven1z.core.model.Config.MAX_DETECT_SIZE;
import static com.keven1z.core.hook.HookThreadLocal.*;
import static com.keven1z.core.hook.HookThreadLocal.REQUEST_THREAD_LOCAL;

public class HttpSpy implements SimpleIASTSpy {
    Logger logger = Logger.getLogger(HttpSpy.class);


    /**
     * 当请求开始时调用此方法。
     *
     * @param requestObject 请求对象
     * @param responseObject 响应对象
     *
     */
    @Override
    public void $_requestStarted(Object requestObject, Object responseObject) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }

            if (REQUEST_THREAD_LOCAL.get() != null || isRequestStart.get()) {
                return;
            }
            if (DETECT_LIMIT_SET.size() > MAX_DETECT_SIZE) {
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
            DETECT_LIMIT_SET.add(abstractRequest.getRequestId());
            isRequestStart.set(true);
            SANITIZER_RESOLVER_CACHE.set(new HashSet<>());
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

    /**
     * 请求结束时的处理方法
     *
     * @param requestObject 请求对象
     * @param responseObject 响应对象
     */
    @Override
    public void $_requestEnded(Object requestObject, Object responseObject) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }
            HttpContext requestData  = REQUEST_THREAD_LOCAL.get();
            if (isRequestEnded.get() || requestData  == null) {
                return;
            }
            if (!requestData .isHttpExit(requestObject, responseObject)) {
                return;
            }
            System.out.println("[SimpleIAST] 请求消耗时间:" + (System.currentTimeMillis() - REQUEST_TIME_CONSUMED.get()) + "ms");
            REQUEST_TIME_CONSUMED.remove();
            if (LogTool.isDebugEnabled()) {
                logger.info("[" + requestData .getRequest().getRequestId() + "] Request exit,URL:" + REQUEST_THREAD_LOCAL.get().getRequest().getRequestURLString());
            }
            isRequestEnded.set(true);
            DetectorFactory.getInstance().processAndReportFindings();

        } finally {
            enableHookLock.set(false);
            if (isRequestEnded.get()) {
                HttpContext requestData = REQUEST_THREAD_LOCAL.get();
                if (requestData != null) {
                    DETECT_LIMIT_SET.remove(requestData.getRequest().getRequestId());
                }
                TaintSpy.getInstance().clear();
            }
        }
    }

    /**
     * 填充请求体内容
     *
     * @param body 请求体内容
     *
     */
    @Override
    public void $_setRequestBody(Object body) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        if (!ApplicationModel.isRunning()) {
            return;
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

    /**
     * 当读取操作被调用时触发的方法
     *
     * @param length 读取的长度
     * @param inputStream 输入流对象
     * @param bytes 读取的字节数组
     * @param off 数组中的起始偏移量（包括）
     * @param len 要读取的字节数
     *
     */
    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }

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

    /**
     * 在读取操作被调用时触发
     *
     * @param length 读取的字节长度
     * @param inputStream 输入流对象
     * @param bytes 读取的字节数组
     *
     */
    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }

        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }

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

    /**
     * 当读取操作被调用时触发的函数
     *
     * @param b            待读取的字节
     * @param inputStream  输入流
     *
     */
    @Override
    public void $_onReadInvoked(Integer b, Object inputStream) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }
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

    /**
     * 获取请求体输出流并设置长度
     *
     * @param b 请求体长度
     * @param inputStream 输入流对象
     * @return 如果当前线程中的请求上下文存在，并且输入流与请求中的输入流匹配，则返回请求体输出流；否则返回null
     */
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
     *
     * @param url 需要过滤的URL
     * @return 如果URL是静态页面则返回true，否则返回false
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
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type) {

    }

    @Override
    public void $_arrayTaint(Object arrayObject, int index, Object arrayValue, String className, String method, String desc) {

    }

    @Override
    public void $_userBeanTaint(Object thisObject, Object[] parameters, Object returnObject, String className, String method, String desc) {

    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {

    }
}
