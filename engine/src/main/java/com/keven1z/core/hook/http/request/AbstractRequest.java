/*
 * Copyright 2017-2021 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keven1z.core.hook.http.request;

import com.keven1z.core.Config;
import com.keven1z.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;

import static com.keven1z.core.consts.HTTPConst.POST;

/**
 * 为不同服务器的不同请求hook点做出的统一格式抽象类
 */
public abstract class AbstractRequest {
    public static final Class[] EMPTY_CLASS = new Class[]{};
    protected static final Class[] STRING_CLASS = new Class[]{String.class};


    protected WeakReference<Object> request;
    protected WeakReference<Object> inputStream = null;
    protected int inputStreamId = -1;
    private int bodyLength = 0;
    protected Object charReader = null;
    protected ByteArrayOutputStream bodyOutputStream = null;
    protected CharArrayWriter bodyWriter = null;
    protected int maxBodySize = 4096;
    protected String requestId;
    protected boolean canGetParameter = false;
    /**
     * 请求完整的报文
     */
    private byte[] requestDetail;
    protected HashMap<String, String[]> formItemCache = null;
//    protected LinkedList<RequestFileItem> fileParamCache = null;

    /**
     * constructor
     *
     * @see AbstractRequest#AbstractRequest(Object) 默认请求实体为null
     */
    public AbstractRequest() {
        this(null);
    }

    /**
     * constructor
     *
     * @param request 请求实体
     */
    public AbstractRequest(Object request) {
        this.request = new WeakReference<>(request);
        this.requestId = UUID.randomUUID().toString().replace("-", "");
//        this.maxBodySize = Config.getConfig().getBodyMaxBytes();
    }

    /**
     * constructor
     *
     * @param request 请求实体
     */
    public AbstractRequest(Object request, String requestId) {
        this.request = new WeakReference<>(request);
        this.requestId = requestId;
        this.maxBodySize = Config.getConfig().MAX_BODY_SIZE;
    }

    /**
     * constructor 测试时使用的构造函数
     */
    public AbstractRequest(int request) {
    }

    /**
     * 返回是否当前请求能够获取参数内容
     *
     * @return 是否能够获取参数内容
     */
    public boolean isCanGetParameter() {
        return canGetParameter;
    }

    /**
     * 设置是否能够获取参数
     *
     * @param canGetParameter 是否能够获取参数内容
     */
    public void setCanGetParameter(boolean canGetParameter) {
        this.canGetParameter = canGetParameter;
    }

    /**
     * 设置请求实体，该请求实体在不同的环境中可能是不同的类型
     *
     * @param request 请求实体
     */
    public void setRequest(Object request) {
        this.request = new WeakReference<>(request);
    }

    /**
     * 获取请求实体
     *
     * @return 请求实体
     */
    public Object getRequest() {
        if (this.request == null) {
            return null;
        }
        return this.request.get();
    }

    /**
     * 获取请求Id
     *
     * @return 请求Id
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * 获取本服务器地址
     *
     * @return 服务器地址
     */
    public abstract String getLocalAddr();

    public abstract String getServerPort();

    /**
     * 获取请求方法
     *
     * @return 请求方法
     */
    public abstract String getMethod();

    /**
     * 获取请求协议
     *
     * @return 请求协议
     */
    public abstract String getProtocol();

    /**
     * 获取验证类型
     *
     * @return 验证类型
     */
    public abstract String getAuthType();

    /**
     * 获取请求路径
     *
     * @return 请求路径
     */
    public abstract String getContextPath();

    /**
     * 获取访问客户端的地址
     *
     * @return 客户端地址
     */
    public abstract String getRemoteAddr();

    /**
     * 获取请求的uri
     *
     * @return 请求uri
     */
    public abstract String getRequestURI();

    /**
     * 获取请求的url
     *
     * @return 请求的url
     */
    public abstract StringBuffer getRequestURL();

    public String getRequestURLString() {
        Object ret = getRequestURL();
        return ret != null ? ret.toString() : null;
    }

    public abstract String getScheme();

    /**
     * 获取服务器名称
     *
     * @return 服务器名称
     */
    public abstract String getServerName();

    /**
     * 根据请求的参数名称，获取请求参数的值
     *
     * @param key 请求参数名称
     * @return 请求参数的值
     */
    public abstract String getParameter(String key);

    /**
     * 获取所有请求参数名称
     *
     * @return 请求参数名称的枚举集合
     */
    public abstract Enumeration<String> getParameterNames();

    /**
     * 获取请求参数的map键值对集合
     * key为参数名称，value为参数值
     *
     * @return 请求参数的map集合
     */
    public abstract Map<String, String[]> getParameterMap();

    /**
     * 根据请求头的名称获取请求头的值
     *
     * @param key 请求头的名称
     * @return 请求头的值
     */
    public abstract String getHeader(String key);

    /**
     * 获取所有请求头的名称
     *
     * @return 请求头名称的枚举集合
     */
    public abstract Enumeration<String> getHeaderNames();

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = getHeader(key);
                headers.put(key.toLowerCase(), value);
            }
        }
        return headers;
    }

    /**
     * 获取请求的url中的 Query String 参数部分
     *
     * @return 请求的 Query String
     */
    public abstract String getQueryString();

    /**
     * 获取服务器的上下文参数map集合
     * key为参数名字，value为参数的值
     *
     * @return 服务器上下文参数的map集合
     */
    public abstract Map<String, String> getServerContext();

    /**
     * 获取app部署根路径
     *
     * @return app部署根路径
     */
    public abstract String getAppBasePath();

    /**
     * 获取请求的contentType
     *
     * @return contentType
     */
    public abstract String getContentType();

    /**
     * 获取自定义的clientip
     *
     * @return 自定义的clientip
     */
    public abstract String getClientIp();

    /**
     * 返回HTTP request body
     *
     * @return request body, can be null
     */
    public byte[] getBody() {
        return bodyOutputStream != null ? bodyOutputStream.toByteArray() : null;
    }

    public byte[] getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(byte[] requestDetail) {
        this.requestDetail = requestDetail;
    }

    /**
     * 返回HTTP request body
     *
     * @return request body, can be null
     */
    public String getStringBody() {
        if (bodyOutputStream != null) {
            byte[] body = bodyOutputStream.toByteArray();
            String encoding = getCharacterEncoding();
            if (!StringUtils.isEmpty(encoding)) {
                try {
                    return new String(body, 0, getBodyLength(), encoding);
                } catch (UnsupportedEncodingException e) {
                    return new String(body, 0, getBodyLength());
                }
            } else {
                return new String(body);
            }
        } else if (bodyWriter != null) {
            return bodyWriter.toString();
        } else if (POST.equals(getMethod())) {
            return getParameterString().toString();
        }
        return null;
    }

    public StringBuilder getParameterString() {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> parameterNames = this.getParameterNames();
        if (parameterNames == null) {
            return sb;
        }
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameter = this.getParameter(parameterName);
            sb.append(parameterName).append("=").append(parameter).append("&");
        }
        return sb;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = this.bodyLength + bodyLength;
    }

    /**
     * 返回HTTP request body stream
     *
     * @return request body, can be null
     */

    public ByteArrayOutputStream getBodyOutputStream() {
        return bodyOutputStream;
    }

    public void setBodyOutputStream(ByteArrayOutputStream bodyOutputStream) {
        this.bodyOutputStream = bodyOutputStream;
    }

    /**
     * 返回input stream
     *
     * @return input stream
     */
    public Object getInputStream() {
        return inputStream;
    }

    /**
     * 设置input stream
     *
     * @param inputStream input stream
     */
    public void setInputStream(Object inputStream) {
        this.inputStream = new WeakReference<>(inputStream);
        this.inputStreamId = System.identityHashCode(inputStream);
    }

    public int getInputStreamId() {
        return inputStreamId;
    }

    /**
     * 设置字符输入流
     *
     * @param charReader 字符输入流
     */
    public void setCharReader(Object charReader) {
        this.charReader = charReader;
    }

    /**
     * 添加 HTTP request body
     *
     * @param b 要添加的字节
     */
    public void appendByteBody(int b) {
        if (bodyOutputStream == null) {
            bodyOutputStream = new ByteArrayOutputStream();
        }

        if (bodyOutputStream.size() < maxBodySize) {
            bodyOutputStream.write(b);
        }
    }

    /**
     * 添加 HTTP request body
     *
     * @param bytes  字节数组
     * @param offset 要添加的起始偏移量
     * @param len    要添加的长度
     */
    public void appendBody(byte[] bytes, int offset, int len) {
        if (bodyOutputStream == null) {
            bodyOutputStream = new ByteArrayOutputStream();
        }

        len = Math.min(len, maxBodySize - bodyOutputStream.size());
        if (len > 0) {
            bodyOutputStream.write(bytes, offset, len);
        }
    }

    /**
     * 添加 HTTP request body
     *
     * @param cbuf   字符数组
     * @param offset 要添加的起始偏移量
     * @param len    要添加的长度
     */
    public void appendBody(char[] cbuf, int offset, int len) {
        if (bodyWriter == null) {
            bodyWriter = new CharArrayWriter();
        }

        len = Math.min(len, maxBodySize / 2 - bodyWriter.size());
        if (len > 0) {
            bodyWriter.write(cbuf, offset, len);
        }
    }

    /**
     * 添加 HTTP request body
     *
     * @param b 要添加的字符
     */
    public void appendCharBody(int b) {
        if (bodyWriter == null) {
            bodyWriter = new CharArrayWriter();
        }

        if (bodyWriter.size() < (maxBodySize / 2)) {
            bodyWriter.write(b);
        }
    }

    /**
     * 返回body的编码类型
     *
     * @return CharacterEncoding
     */
    public abstract String getCharacterEncoding();

    protected boolean setCharacterEncodingFromConfig() {
        try {
            String paramEncoding = "Utf-8";
            if (!StringUtils.isEmpty(paramEncoding)) {
                ReflectionUtils.invokeMethod(request, "setCharacterEncoding", STRING_CLASS, paramEncoding);
                return true;
            }
        } catch (Exception e) {
//            LogTool.warn(ErrorType.RUNTIME_ERROR, "set character encoding failed: " + e.getMessage(), e);
        }
        return false;
    }

    public HashMap<String, String[]> getFormItemCache() {
        return formItemCache;
    }

    public void setFormItemCache(HashMap<String, String[]> cache) {
        formItemCache = cache;
    }

    public void clear() throws IOException {
        if (bodyOutputStream != null) {
            bodyOutputStream.close();
        }
        bodyOutputStream = null;
    }
}
