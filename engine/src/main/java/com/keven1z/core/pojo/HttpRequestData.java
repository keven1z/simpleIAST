package com.keven1z.core.pojo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class HttpRequestData {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String requestBody;

    public HttpRequestData(String url, String method, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        if (requestBody != null) {
            this.requestBody = new String(Base64.getEncoder().encode(requestBody.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        }
    }
}
