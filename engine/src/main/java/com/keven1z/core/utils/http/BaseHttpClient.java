package com.keven1z.core.utils.http;

import com.keven1z.core.EngineController;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * HTTP客户端基类（抽象公共逻辑）
 */
public abstract class BaseHttpClient implements Closeable {
    protected final Logger logger = Logger.getLogger(getClass());

    protected volatile CloseableHttpClient httpClient;
    protected String baseUrl;

    protected BaseHttpClient(String baseUrl) {
        this.baseUrl = validateBaseUrl(baseUrl);
        httpClient = getOrCreateHttpClient();
    }

    private CloseableHttpClient getOrCreateHttpClient() {
        if (httpClient == null) {
            synchronized (BaseHttpClient.class) {
                if (httpClient == null) {
                    httpClient = buildHttpClient();
                }
            }
        }
        return httpClient;
    }

    private String validateBaseUrl(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }
        if (!url.startsWith("http")) {
            throw new IllegalArgumentException("Invalid protocol in base URL: " + url);
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    protected static CloseableHttpClient buildHttpClient() {
        return HttpClientFactory.createPooledClient();
    }

    protected String fetchRequestResponseAsString(HttpRequestBase request) throws IOException {
        addCommonHeaders(request);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            checkResponseStatus(response);
            return parseResponseContent(response);
        }
    }

    protected CloseableHttpResponse fetchRequest(HttpRequestBase request) throws IOException {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        addCommonHeaders(request);
        return httpClient.execute(request);
    }

    private void checkResponseStatus(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            String errorMsg = String.format("HTTP请求失败，状态码：%d，URL：%s",
                    statusCode, response.getLocale());
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }
    }

    private String parseResponseContent(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) return null;

        try (InputStream is = entity.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            EntityUtils.consume(entity);
            return content.toString();
        }
    }

    protected void addCommonHeaders(HttpRequest request) {
        request.addHeader("User-Agent", "IAST-Agent/1.0");
        request.addHeader("Accept", "application/json");
        String token = EngineController.context.getToken();
        if (token != null) {
            request.addHeader("Authorization", String.format("Bearer %s", token));
        }
    }

    @Override
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    protected void closeResources(HttpResponse httpResponse, HttpRequestBase request) {
        try {
            if (httpResponse != null && httpResponse.getEntity() != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        } catch (Exception e) {
            logger.debug("Error consuming response entity: " + e.getMessage());
        }

        if (request != null) {
            try {
                request.releaseConnection();
            } catch (Exception e) {
                logger.debug("Error releasing connection: " + e.getMessage());
            }
        }
    }
}