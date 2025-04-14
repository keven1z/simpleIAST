package com.keven1z.core.utils.http;

import com.keven1z.core.consts.Api;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * 漏洞报告客户端
 */
public class ReportClient extends BaseHttpClient {

    public ReportClient(String baseUrl) {
        super(baseUrl);
    }

    public boolean sendVulnerabilityReport(String report) {
        try {
//            String payload = JsonUtils.toJsonString(report);
            HttpPost request = buildCompressedRequest(report);
            executeRequest(request);
            return true;
        } catch (IOException e) {
            LOGGER.error("漏洞报告发送失败", e);
            return false;
        }
    }

    private HttpPost buildCompressedRequest(String payload) {
        HttpPost request = new HttpPost(baseUrl + Api.SEND_REPORT_URL);
        request.setEntity(new GzipCompressingEntity(
                new StringEntity(payload, ContentType.APPLICATION_JSON)));
        request.addHeader("Content-Encoding", "gzip");
        return request;
    }
}
