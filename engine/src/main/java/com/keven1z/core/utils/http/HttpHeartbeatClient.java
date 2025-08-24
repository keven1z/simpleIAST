package com.keven1z.core.utils.http;

import com.keven1z.core.consts.Api;
import com.keven1z.core.error.http.HeartbeatSendException;
import com.keven1z.core.model.server.HeartbeatMessage;
import com.keven1z.core.utils.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import java.io.IOException;

public class HttpHeartbeatClient extends BaseHttpClient {

    public HttpHeartbeatClient(String baseUrl) {
        super(baseUrl);
    }

    public boolean sendMessage(HeartbeatMessage message) throws HeartbeatSendException {
        HttpPost request = null;
        HttpResponse httpResponse = null;
        try {
            request = new HttpPost(baseUrl + Api.HEARTBEAT_URL);
            request.setEntity(new StringEntity(JsonUtils.toJsonString(message), ContentType.APPLICATION_JSON));
            httpResponse = fetchRequest(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            boolean success = statusCode >= 200 && statusCode < 300;
            if (!success) {
                logger.debug("Heartbeat send returned non-2xx status: " + statusCode);
            }
            return success;
        } catch (IOException e) {
            logger.error("Heartbeat send error", e);
            throw new HeartbeatSendException("Heartbeat send error", e);
        } finally {
            closeResources(httpResponse, request);
        }
    }
}
