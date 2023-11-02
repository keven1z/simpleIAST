package com.keven1z.core.hook.http;

import com.keven1z.core.hook.http.request.AbstractRequest;
import com.keven1z.core.hook.http.response.HttpServletResponse;

public class HttpContext {
    private AbstractRequest request;
    private int requestHashCode;
    private int responseHashCode;
    private HttpServletResponse response;

    /**
     * 由于存在多个不同的http hook点，需要判断response是否为同一个
     */
    public boolean isHttpExit(Object request, Object response) {
        if (getResponse() == null) {
            return false;
        }
        return System.identityHashCode(response) == responseHashCode && System.identityHashCode(request) == requestHashCode;
    }

    public AbstractRequest getRequest() {
        return request;
    }

    public void setRequest(AbstractRequest request) {
        this.requestHashCode = System.identityHashCode(request.getRequest());
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.responseHashCode = System.identityHashCode(response.getResponseObject());
        this.response = response;
    }
}
