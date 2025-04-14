package com.keven1z.core.utils.http;

import com.keven1z.core.consts.Api;
import com.keven1z.core.pojo.HeartbeatMessage;
import com.keven1z.core.utils.JsonUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class HttpHeartbeatClient extends BaseHttpClient {

    public HttpHeartbeatClient(String baseUrl) {
        super(baseUrl);
    }

    public boolean sendMessage(HeartbeatMessage message) {
        try {
            HttpPost request = new HttpPost(baseUrl + Api.HEARTBEAT_URL);
            request.setEntity(new StringEntity(JsonUtils.toJsonString(message), ContentType.APPLICATION_JSON));
            String response = executeRequest(request);
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
