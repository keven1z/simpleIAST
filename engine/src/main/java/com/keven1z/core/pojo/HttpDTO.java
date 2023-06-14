package com.keven1z.core.pojo;

import java.util.Map;

public class HttpDTO {
    private String url;
    private String method;
    private Map<String,String> headers;
    private byte[] httpMessage;

    public HttpDTO(String url, String method, Map<String, String> headers) {
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

    public byte[] getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(byte[] httpMessage) {
        this.httpMessage = httpMessage;
    }
}
