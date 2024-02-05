package com.keven1z.core.pojo;

import com.keven1z.core.model.graph.TaintGraph;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FindingReportBo {
    private String agentId;
    private HttpRequestData requestData;
    private HttpResponseData responseData;
    private TaintGraph taintGraph;
    private List<FindingData> singleFindingDataList = new ArrayList<>();

    public FindingReportBo(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public HttpRequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(HttpRequestData requestData) {
        this.requestData = requestData;
    }

    public HttpResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(HttpResponseData responseData) {
        this.responseData = responseData;
    }

    public List<FindingData> getSingleFindingDataList() {
        return singleFindingDataList;
    }

    public void setSingleFindingDataList(List<FindingData> singleFindingDataList) {
        this.singleFindingDataList = singleFindingDataList;
    }

    public TaintGraph getTaintGraph() {
        return taintGraph;
    }

    public void setTaintGraph(TaintGraph taintGraph) {
        this.taintGraph = taintGraph;
    }

    public void clear() {
        if (taintGraph != null) {
            taintGraph.clear();
        }
        if (singleFindingDataList != null) {
            singleFindingDataList.clear();
        }
    }
}
