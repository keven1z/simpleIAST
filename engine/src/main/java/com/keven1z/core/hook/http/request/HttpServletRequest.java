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


import com.keven1z.core.utils.ReflectionUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.keven1z.core.consts.HTTPConst.GET;

/**
 * Created by zhuming01 on 6/15/17.
 * All rights reserved
 * javax.servlet.http.HttpServletRequest类请求的统一格式接口
 */
public final class HttpServletRequest extends AbstractRequest {
    private static final Map<String, String[]> EMPTY_PARAM = new HashMap<String, String[]>();
    private static final Pattern PATTERN = Pattern.compile("\\d+(\\.\\d+)*");

    /**
     * 请求实体
     *
     * @param request 类型为javax.servlet.http.HttpServletRequest的请求实体
     */
    public HttpServletRequest(Object request) {
        super(request);
    }

    /**
     * 请求实体
     *
     * @param request 类型为javax.servlet.http.HttpServletRequest的请求实体
     */
    public HttpServletRequest(Object request, String requestId) {
        super(request, requestId);
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

    @Override
    public String getServerPort() {
        return ReflectionUtils.invokeMethod(getRequest(), "getServerPort", EMPTY_CLASS).toString();
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
        return ReflectionUtils.invokeStringMethod(getRequest(), "getAuthType", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getContentType()
     */
    @Override
    public String getContentType() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getContentType", EMPTY_CLASS);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getContextPath()
     */
    @Override
    public String getContextPath() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getContextPath", EMPTY_CLASS);
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

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getRequestURL()
     */
    @Override
    public StringBuffer getRequestURL() {

        Object ret = ReflectionUtils.invokeMethod(getRequest(), "getRequestURL", EMPTY_CLASS);
        if (ret != null) {
            StringBuffer sb = (StringBuffer) ret;
            if (GET.equals(getMethod())) {
                sb.append("?").append(getParameterString());
            }
            return sb;
        }

        return null;
    }

    @Override
    public String getScheme() {
        return ReflectionUtils.invokeStringMethod(getRequest(), "getScheme", EMPTY_CLASS);
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
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameter(String)
     */
    @Override
    public String getParameter(String key) {
        if (!canGetParameter) {
            if (!setCharacterEncodingFromConfig()) {
                return null;
            }
        }
        return ReflectionUtils.invokeStringMethod(getRequest(), "getParameter", STRING_CLASS, key);
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameterNames()
     */
    @Override
    public Enumeration<String> getParameterNames() {
        if (!canGetParameter) {
            if (!setCharacterEncodingFromConfig()) {
                return null;
            }
        }
        Object ret = ReflectionUtils.invokeMethod(getRequest(), "getParameterNames", EMPTY_CLASS);
        return ret != null ? (Enumeration) ret : null;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getParameterMap()
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> normalMap = null;
        if (canGetParameter || setCharacterEncodingFromConfig()) {
            normalMap = (Map<String, String[]>) ReflectionUtils.invokeMethod(getRequest(), "getParameterMap", EMPTY_CLASS);
        }
        return getMergeMap(normalMap, formItemCache);
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
//        return ApplicationModel.getApplicationInfo();
        return new HashMap<String, String>();
    }

    /**
     * 根据服务器信息获取服务器类型
     *
     * @param serverInfo 服务器信息
     * @return 服务器类型
     */
    public static String extractType(String serverInfo) {
        if (serverInfo == null) {
            return null;
        }
        serverInfo = serverInfo.toLowerCase();
        if (serverInfo.contains("tomcat")) return "Tomcat";
        if (serverInfo.contains("jboss")) return "JBoss";
        if (serverInfo.contains("jetty")) return "Jetty";
        return serverInfo;
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getAppBasePath()
     */
    @Override
    public String getAppBasePath() {
//        try {
//            String realPath;
//            if ("weblogic".equals(ApplicationModel.getServerName())) {
//                realPath = getRealPathForWeblogic();
//            } else {
//                realPath = Reflection.invokeStringMethod(request, "getRealPath", new Class[]{String.class}, "/");
//            }
//            return realPath == null ? "" : realPath;
//        } catch (Exception e) {
//            return "";
//        }
        return "";
    }

    /**
     * (none-javadoc)
     *
     * @see AbstractRequest#getCharacterEncoding()
     */
    @Override
    public String getCharacterEncoding() {
        String encoding = ReflectionUtils.invokeStringMethod(getRequest(), "getCharacterEncoding", EMPTY_CLASS);
        return encoding == null ? "utf-8" : encoding;
    }

    @Override
    public String getClientIp() {
        String clientIp = "127.0.0.1";
        String realIp = getHeader(clientIp);
        return realIp != null ? realIp : "";
    }


    private Map<String, String[]> getMergeMap(Map<String, String[]> map1, Map<String, String[]> map2) {
        if (map1 == null && map2 == null) {
            return null;
        }
        Map<String, String[]> result = new HashMap<String, String[]>();
        if (map1 != null && !map1.isEmpty()) {
            mergeMap(map1, result);
        }
        if (map2 != null && !map2.isEmpty()) {
            mergeMap(map2, result);
        }
        return result;
    }

    private void mergeMap(Map<String, String[]> src, Map<String, String[]> dst) {
        try {
            for (Map.Entry<String, String[]> entry : src.entrySet()) {
                if (dst.containsKey(entry.getKey())) {
                    dst.put(entry.getKey(), mergeArray(dst.get(entry.getKey()), entry.getValue()));
                } else {
                    dst.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable t) {
//            LogTool.traceHookWarn("failed to merge parameter: " + t.getMessage(), t);
        }
    }

    private String[] mergeArray(String[] s1, String[] s2) {
        int str1Length = s1.length;
        int str2length = s2.length;
        s1 = Arrays.copyOf(s1, str1Length + str2length);
        System.arraycopy(s2, 0, s1, str1Length, str2length);
        return s1;
    }

    private String getRealPathForWeblogic() {
        Object servletContext = ReflectionUtils.invokeMethod(getRequest(), "getContext", new Class[]{});
        URL url = (URL) ReflectionUtils.invokeMethod(servletContext, "getResource", new Class[]{String.class}, "/");
        if (url != null) {
            return url.getPath();
        }
        return null;
    }
}
