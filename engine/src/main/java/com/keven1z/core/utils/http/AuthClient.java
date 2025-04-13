package com.keven1z.core.utils.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.keven1z.core.consts.Api;
import com.keven1z.core.error.RegistrationException;
import com.keven1z.core.pojo.AgentDTO;
import com.keven1z.core.pojo.AuthenticationDTO;
import com.keven1z.core.pojo.ResponseDTO;
import com.keven1z.core.utils.JsonUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * 认证管理客户端
 */
public class AuthClient extends BaseHttpClient {

    public AuthClient(String baseUrl) {
        super(baseUrl);
    }

    public AuthenticationDTO register(AgentDTO agentDto) throws RegistrationException {
        try {
            String payload = JsonUtils.toJsonString(agentDto);
            HttpPost request = buildPostRequest(Api.AGENT_REGISTER_URL, payload);
            String response = executeRequest(request);
            return parseAuthResponse(response);
        } catch (IOException e) {
            throw new RegistrationException("注册请求失败", e);
        }
    }

    public boolean deregister(String agentId) {
        try {
            String url = baseUrl + Api.AGENT_DEREGISTER_URL + "?agentId=" + agentId;
            HttpGet request = new HttpGet(url);
            executeRequest(request);
            return true;
        } catch (IOException e) {
            LOGGER.warn("注销请求失败", e);
            return false;
        }
    }

    private HttpPost buildPostRequest(String path, String payload) {
        HttpPost request = new HttpPost(baseUrl + path);
        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        addCommonHeaders(request);
        return request;
    }

    private AuthenticationDTO parseAuthResponse(String json) throws RegistrationException {
        try {
            ResponseDTO<AuthenticationDTO> response = JsonUtils.parseObject(json,
                    new TypeReference<ResponseDTO<AuthenticationDTO>>() {});

            // 处理失败响应
            if (!response.isFlag()) {
                String errorMsg = String.format("Registration failed. Reason: %s", response.getMessage());
                LOGGER.warn(errorMsg);
                throw new RegistrationException(errorMsg);
            }
            AuthenticationDTO authData = response.getData();
            // 校验响应数据类型
            if (authData == null || authData.getAgentId() == null || authData.getToken() == null) {
                String errorMsg = "Incomplete authentication data. AgentId or Token is missing.";
                LOGGER.error(errorMsg);
                throw new RegistrationException(errorMsg);
            }
            return authData;
        } catch (JsonProcessingException e) {
            throw new RegistrationException("响应解析失败", e);
        }
    }
}

