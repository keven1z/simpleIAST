package com.keven1z.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.EngineController;
import com.keven1z.core.consts.Api;
import com.keven1z.core.error.RegistrationException;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.server.AgentDTO;
import com.keven1z.core.model.server.AuthenticationDto;
import com.keven1z.core.model.server.ResponseDTO;
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
import org.apache.log4j.Logger;

@Deprecated
public class IASTHttpClient {
    private static final Logger logger = Logger.getLogger(IASTHttpClient.class);

    private final CloseableHttpClient client;
    private String requestHost;
    private static final int SOCKET_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;
    private static final int REQUEST_TIMEOUT = 3000;

    private IASTHttpClient() {
        this.client = createHttpClient();
    }

    public static IASTHttpClient getClient() {
        return Inner.client;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }

    public String getRequestHost() {
        return requestHost;
    }

    private static class Inner {
        private static final IASTHttpClient client = new IASTHttpClient();
    }

    private String doGet(String url, boolean isKeepAlive) throws IOException {

        CloseableHttpResponse response = get(url, isKeepAlive);
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

    private CloseableHttpResponse get(String url, boolean isKeepAlive) throws IOException {
        HttpGet request = new HttpGet(url);
        if (!isKeepAlive) {
            request.addHeader("Connection", "close");
        } else {
            request.addHeader("Connection", "keep-alive");
        }
        String token = EngineController.context.getToken();
        if (token != null) {
            request.addHeader("Authorization", token);
        }
        return client.execute(request);
    }


    private CloseableHttpResponse post(String url, String payload, boolean isGzip) throws IOException {
        HttpPost request = new HttpPost(url);
        String token = EngineController.context.getToken();
        if (token != null) {
            request.addHeader("Authorization", token);
        }

        HttpEntity entity;
        if (isGzip) {
            entity = new GzipCompressingEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        } else {
            entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        }
        request.setEntity(entity);

        return  client.execute(request);
    }


    private CloseableHttpClient createHttpClient() {
        HttpClientBuilder hcb = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier());
        hcb.setDefaultRequestConfig(createRequestConfig());
        return hcb.build();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
    }

    /**
     * 发送漏洞报告，漏洞内容采用gzip压缩
     *
     * @param payload 漏洞内容
     */
    public boolean sendReport(String payload) throws IOException {
        if (requestHost == null) {
            return false;
        }
        CloseableHttpResponse response = null;
        try {
            response = post(getRequestHost() + Api.SEND_REPORT_URL, payload, true);

            return response.getStatusLine().getStatusCode() == 200;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    /**
     * 发送注册请求
     */
    public static void register(AgentDTO agentDTO) throws RegistrationException {
        try {
            String requestBody = JsonUtils.toJsonString(agentDTO);
            String responseBody = IASTHttpClient.getClient().register(requestBody);

            // 解析响应
            ResponseDTO<Object> responseDTO = JsonUtils.toObject(responseBody, ResponseDTO.class);
            // 处理失败响应
            if (!responseDTO.isFlag()) {
                String errorMsg = String.format("Registration failed. Reason: %s", responseDTO.getMessage());
                logger.warn(errorMsg);
                throw new RegistrationException(errorMsg);
            }
            // 校验响应数据类型
            AuthenticationDto authData = JsonUtils.parseObject(responseDTO.getData(), AuthenticationDto.class);
            if (authData == null || authData.getAgentId() == null || authData.getToken() == null) {
                String errorMsg = "Incomplete authentication data. AgentId or Token is missing.";
                logger.error(errorMsg);
                throw new RegistrationException(errorMsg);
            }

            // 更新应用配置
            ApplicationModel.setAgentId(authData.getAgentId());
            EngineController.context.setToken(authData.getToken());

            // 记录成功日志
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Registration successful. AgentID: %s", authData.getAgentId()));
            }
        } catch (JsonProcessingException e) {
            String errorMsg = "JSON serialization/deserialization failed during registration.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常

        } catch (IOException e) {
            String errorMsg = "Network communication error during registration.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常

        } catch (RegistrationException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error during registration process.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常
        }
    }
    /**
     * 向服务器注册agent
     *
     * @param information 注册信息
     */
    private String register(String information) throws IOException {
        if (requestHost == null) {
            return null;
        }
        HttpResponse response = post(getRequestHost() + Api.AGENT_REGISTER_URL, information,false);
        HttpEntity entity = response.getEntity();
        String responseString = null;
        try {
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    responseString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                }
            }
        } finally {
            // 确保资源被释放
            if (entity != null) {
                EntityUtils.consume(entity);  // 释放实体资源
            }
        }
        return responseString;
    }

    /**
     * 向服务器解绑agent
     */
    public boolean deregister() {
        if (requestHost == null) {
            return false;
        }
        CloseableHttpResponse response = null;
        try {
            response = get(getRequestHost() + Api.AGENT_DEREGISTER_URL + "?agentId=" + ApplicationModel.getAgentId(), false);
            return response.getStatusLine().getStatusCode() == 200;

        } catch (Exception e) {
            return false;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public String getInstruction() {
        if (requestHost == null) {
            return null;
        }
        try {
            return doGet(getRequestHost() + Api.POLICY_URL + "?agentId=" + ApplicationModel.getAgentId(), true);
        } catch (Exception e) {
            if (LogTool.isDebugEnabled()) {
                LogTool.error(ErrorType.REQUEST_ERROR, "Failed to get instruction", e);
            }
        }
        return null;
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ignore) {
            }
        }
    }
}

