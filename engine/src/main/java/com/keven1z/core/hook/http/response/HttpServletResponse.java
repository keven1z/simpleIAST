package com.keven1z.core.hook.http.response;

import com.keven1z.core.utils.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpServletResponse {
    private final WeakReference<Object> responseObject;
    private Map<String, String> headers;

    public HttpServletResponse(Object responseObject) {
        this.responseObject = new WeakReference<>(responseObject);
    }

    public Object getResponseObject() {
        return responseObject.get();
    }

    public Map<String, String> getHeaders() {
        if (headers != null) {
            return headers;
        }
        Object response = responseObject.get();
        Collection<String> headerNames = (Collection<String>) ReflectionUtils.invokeMethod(response, "getHeaderNames", new Class[]{});
        headers = new HashMap<>();
        if (headerNames == null) {
            return headers;
        }
        Iterator<String> iterator = headerNames.iterator();
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            Collection<String> value = (Collection<String>) ReflectionUtils.invokeMethod(response, "getHeaders", new Class[]{String.class}, headerName);
            if (value != null && !value.isEmpty()) {
                headers.put(headerName, value.iterator().next());
            }
        }
        return headers;
    }

    public String getResponseBody() {
        return "";
    }

}
