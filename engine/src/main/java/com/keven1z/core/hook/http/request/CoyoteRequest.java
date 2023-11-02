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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.keven1z.core.consts.HTTPConst.GET;

/**
 * Created by zhuming01 on 6/23/17.
 * All rights reserved
 * org.apache.coyote.Request类请求的的格式化接口
 */
public class CoyoteRequest extends AbstractRequest {

    /**
     * 请求实体
     *
     * @param request 类型为org.apache.coyote.Request的请求实体
     */
    public CoyoteRequest(Object request) {
        super(request);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getLocalAddr()
     */
    @Override
    public String getLocalAddr() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getLocalAddr", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getMethod", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getProtocol()
     */
    @Override
    public String getProtocol() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getProtocol", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getAuthType()
     */
    @Override
    public String getAuthType() {
        return ReflectionUtils.invokeStringMethod(request, "getAuthType", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getContextPath()
     */
    @Override
    public String getContextPath() {
        return null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getRemoteAddr()
     */
    @Override
    public String getRemoteAddr() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getRemoteAddr", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getRequestURI()
     */
    @Override
    public String getRequestURI() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getRequestURI", EMPTY_CLASS);
    }

    @Override
    public String getScheme() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getScheme", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getRequestURL()
     */
    @Override
    public StringBuffer getRequestURL() {
        StringBuffer sb = new StringBuffer();

        String scheme = getScheme();
        if (scheme != null) {
            sb.append(scheme).append("://");
        }

        String host = getServerName();
        if (host != null) {
            sb.append(host);
        }

        String port = getServerPort();
        if (port != null) {
            sb.append(":").append(port);
        }

        String uri = getRequestURI();
        if (uri != null) {
            sb.append(uri);
        }
        String method = getMethod();
        if (GET.equals(method)) {
            sb.append("?").append(getParameterString());
        }

        return sb;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getServerName()
     */
    @Override
    public String getServerName() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getServerName", EMPTY_CLASS);
    }

    /**
     * 获取本服务的端口号
     *
     * @return 端口号
     */
    @Override
    public String getServerPort() {
        return ReflectionUtils.invokeMethod(getRequest(), "getServerPort", EMPTY_CLASS).toString();
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameter(String)
     */
    @Override
    public String getParameter(String key) {
        Object parameters = ReflectionUtils.invokeMethod(getRequest(), "getParameters", EMPTY_CLASS);
        if (parameters == null)
            return null;

        return ReflectionUtils.invokeStringMethod(parameters, "getParameter", STRING_CLASS, key);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameterNames()
     */
    @Override
    public Enumeration<String> getParameterNames() {
        Object parameters = ReflectionUtils.invokeMethod(getRequest(), "getParameters", EMPTY_CLASS);
        if (parameters == null)
            return null;

        Object names = ReflectionUtils.invokeMethod(parameters, "getParameterNames", EMPTY_CLASS);
        if (names == null)
            return null;

        return (Enumeration<String>) names;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameterMap()
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Enumeration<String> names = getParameterNames();
        if (names == null) {
            return null;
        }
        Map<String, String[]> paramMap = new HashMap<>();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String[] values = (String[]) ReflectionUtils.invokeMethod(getRequest(), "getParameterValues", STRING_CLASS, key);
            paramMap.put(key, values);
        }
        return paramMap;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getHeader(String)
     */
    @Override
    public String getHeader(String key) {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getHeader", STRING_CLASS, key);
    }


    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getHeaderNames()
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        Object ret = ReflectionUtils.invokeMethod(getRequest(), "getHeaderNames", EMPTY_CLASS);
        return ret != null ? (Enumeration<String>) ret : null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getQueryString()
     */
    @Override
    public String getQueryString() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getQueryString", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getServerContext()
     */
    @Override
    public Map<String, String> getServerContext() {
        return null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getAppBasePath()
     */
    @Override
    public String getAppBasePath() {
        return null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getClientIp()
     */
    @Override
    public String getClientIp() {
//        String clientIp = "127.0.0.1";
        String realIp = getHeader("x-forwarded-for");
        return realIp != null ? realIp : "";
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getContentType()
     */
    @Override
    public String getContentType() {
        return null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getCharacterEncoding()
     */
    @Override
    public String getCharacterEncoding() {
        String encoding = ReflectionUtils.invokeStringMethod(getRequest(), "getCharacterEncoding", new Class[]{});
        return encoding == null ? "utf-8" : encoding;
    }

}
