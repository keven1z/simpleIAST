package com.keven1z.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.keven1z.core.consts.Api;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.pojo.ResponseDTO;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpClientUtils {
    private static final CloseableHttpClient client = createHttpClient();
    private static final int SOCKET_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;
    private static final int REQUEST_TIMEOUT = 3000;

    private static String doGet(String url) throws IOException {

        CloseableHttpResponse response = get(url);
        InputStream inputStream = null;
        String responseString = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();

                responseString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                EntityUtils.consume(entity);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (response != null) {
                response.close();
            }
        }
        return responseString;
    }

    private static CloseableHttpResponse get(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = client.execute(request);
        return response;
    }

    private static String post(String url, String payload) throws IOException {
        HttpResponse response = postNormal(url, payload);
        HttpEntity entity = response.getEntity();
        String responseString = null;
        if (entity != null) {
            InputStream inputStream = entity.getContent();
            try {
                responseString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } finally {
                inputStream.close();
            }
        }
        EntityUtils.consume(entity);
        return responseString;
    }

    private static CloseableHttpResponse postGzipBody(String url, String payload) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new GzipCompressingEntity(new StringEntity(payload, ContentType.APPLICATION_JSON)));
        return client.execute(request);
    }

    private static CloseableHttpResponse postNormal(String url, String payload) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        return client.execute(request);
    }

    private static CloseableHttpClient createHttpClient() {
        HttpClientBuilder hcb = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier());
        hcb.setDefaultRequestConfig(createRequestConfig());
        return hcb.build();
    }

    private static RequestConfig createRequestConfig() {
        return RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
    }

    /**
     * 发送漏洞报告，漏洞内容采用gzip压缩
     *
     * @param payload 漏洞内容
     */
    public static boolean sendReport(String payload) throws IOException {
        CloseableHttpResponse response = null;
        try {
            response = postGzipBody(Api.SEND_REPORT_URL, payload);

            return response.getStatusLine().getStatusCode() == 200;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 向服务器注册agent
     *
     * @param information 注册信息
     */
    public static boolean register(String information) throws IOException {
        String response = post(Api.AGENT_REGISTER_URL, information);
        ResponseDTO<String> responseDTO = JsonUtils.toObject(response, ResponseDTO.class);
        if (responseDTO.isFlag()) {
            ApplicationModel.setAgentId(responseDTO.getData());
        }
        return responseDTO.isFlag();
    }

    /**
     * 向服务器解绑agent
     */
    public static boolean deregister()  {
        CloseableHttpResponse response = null;
        try {
            response = get(Api.AGENT_DEREGISTER_URL + "?agentId=" + ApplicationModel.getAgentId());
            return response.getStatusLine().getStatusCode() == 200;

        }catch (Exception e){
            return false;
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public static String getInstruction() throws IOException {
        try {
            return doGet(Api.INSTRUCTION_GET_URL + "?agentId=" + ApplicationModel.getAgentId());
        } catch (Exception e) {
            if (LogTool.isDebugEnabled()) {
                LogTool.error(ErrorType.REQUEST_ERROR, "Failed to get instruction", e);
            }
        }
        return null;
    }

    public static void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ignore) {
            }
        }
    }
}

