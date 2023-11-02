package com.keven1z.core.pojo;

import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.hook.http.response.HttpServletResponse;

import java.util.Map;

/**
 * @author keven1z
 * @date 2023/10/22
 */
public class HttpMessage {
    private String url;
    private String method;
    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;

    /**
     * Http报文
     */
    private String requestBody;
    private String responseBody;

    public HttpMessage(String url, String method, Map<String, String> requestHeaders, Map<String, String> responseHeaders, String requestBody, String responseBody) {
        this.url = url;
        this.method = method;
        this.requestHeaders = requestHeaders;
        this.responseHeaders = responseHeaders;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void clear() {
        if (getRequestHeaders() != null) {
            getRequestHeaders().clear();
        }
        if (getResponseBody() != null) {
            getResponseHeaders().clear();
        }
    }

    public static HttpMessage transform(AbstractRequest request, HttpServletResponse response) {
        if (request == null || response == null) {
            return null;
        }
        return new HttpMessage(request.getRequestURLString(), request.getMethod(), request.getHeaders(), response.getHeaders(), request.getStringBody(), response.getResponseBody());
    }
}
