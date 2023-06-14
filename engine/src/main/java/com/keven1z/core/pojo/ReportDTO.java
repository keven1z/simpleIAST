package com.keven1z.core.pojo;

import com.keven1z.core.graph.taint.TaintData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class ReportDTO {
    private String agentId;
    private HttpDTO http;
    private LinkedList<TaintData> taintLinkList;
    private final String timestamp ;
    private String vulType;
    public ReportDTO(){
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将当前时间格式化为字符串
        this.timestamp = now.format(formatter);
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public HttpDTO getHttp() {
        return http;
    }

    public void setHttp(HttpDTO http) {
        this.http = http;
    }

    public LinkedList<TaintData> getTaintLinkList() {
        return taintLinkList;
    }

    public void setTaintLinkList(LinkedList<TaintData> taintLinkList) {
        this.taintLinkList = taintLinkList;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getVulType() {
        return vulType;
    }

    public void setVulType(String vulType) {
        this.vulType = vulType;
    }
}
