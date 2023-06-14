package com.keven1z.core.hook.http.handlers;

import org.apache.log4j.Logger;

public interface HttpRequestHandler {
    Logger logger = Logger.getLogger(HttpRequestHandler.class);

    String[] FILTER_HTTP_PREFIX = new String[]{".mvc", ".ico", ".js", ".css", ".jpg", ".gif", "png", ".htm", ".html", ".woff", ".woff2", ".maps", ".map", ".xml"};

    void handler(Object thisObject, String method, String desc, Object[] parameters, boolean isEnter);

}
