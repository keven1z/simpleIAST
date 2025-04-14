package com.keven1z.core.utils.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.keven1z.core.consts.Api;
import com.keven1z.core.error.PolicyException;
import com.keven1z.core.pojo.ResponseDTO;
import com.keven1z.core.policy.ServerPolicy;
import com.keven1z.core.utils.JsonUtils;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * 指令管理客户端
 */
public class PolicyClient extends BaseHttpClient {

    public PolicyClient(String baseUrl) {
        super(baseUrl);
    }

    public ServerPolicy fetchPolicies() throws PolicyException {
        try {
            String url = baseUrl + Api.POLICY_URL;
            HttpGet request = new HttpGet(url);
            String response = executeRequest(request);
            return parsePolicies(response);
        } catch (IOException e) {
            throw new PolicyException("获取策略失败", e);
        }
    }

    private ServerPolicy parsePolicies(String json) throws PolicyException {
        try {
            return JsonUtils.parseObject(json,
                    new TypeReference<ResponseDTO<ServerPolicy>>() {}).getData();
        } catch (JsonProcessingException e) {
            throw new PolicyException("policy解析失败", e);
        }
    }
}
