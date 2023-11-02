package com.keven1z.core.pojo;

import java.util.Map;

public class HttpResponseData {

    private Map<String,String> headers;

    public HttpResponseData(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


}
