package com.keven1z.core.utils.http;

import com.keven1z.core.consts.Api;
import com.keven1z.core.error.http.ReportSendException;
import org.apache.http.HttpResponse;
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

    public boolean sendVulnerabilityReport(String report) throws ReportSendException {
        HttpPost request = null;
        HttpResponse response = null;
        try {
            request = buildCompressedRequest(report);
            response = fetchRequest(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                if (logger.isDebugEnabled()){
                    logger.debug("Report sent successfully, status code: " + statusCode);
                }
                return true;
            } else {
                logger.error("Failed to sent Report, status code:" + statusCode);
                return false;
            }
        } catch (IOException e) {
            logger.debug("Report send IO error: "+e.getMessage(), e);
            throw new ReportSendException("Report send error", e);
        } finally {
            closeResources(response, request);
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
