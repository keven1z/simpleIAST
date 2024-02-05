package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.pojo.FindingData;
import com.keven1z.core.pojo.FindingReportBo;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.pojo.TaintFindingData;
import com.keven1z.core.vulnerability.Detector;
import com.keven1z.core.vulnerability.FlowProcessingStation;
import com.keven1z.core.vulnerability.report.ReportBuilder;
import com.keven1z.core.vulnerability.report.ReportPrinter;
import com.keven1z.core.vulnerability.report.ReportSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.keven1z.core.hook.HookThreadLocal.*;

public class FindingReportMonitor extends Monitor {


    @Override
    public String getThreadName() {
        return "SimpleIAST-Finding-Report-Thread";
    }

    @Override
    public boolean isForServer() {
        return false;
    }

    @Override
    public void doRun() {
        FindingReportBo findingReportBo = null;
        try {
            findingReportBo = FINDING_REPORT_QUEUE.take();
            List<FindingData> findingDataList = check(findingReportBo);
            if (findingDataList.isEmpty()) {
                if (LogTool.isDebugEnabled()){
                    logger.warn("Failed to report finding");
                }
                return;
            }
            buildAndReport(findingReportBo, findingDataList);
            //降低漏洞处理频率，减少cpu 持续消耗
            Thread.sleep(1000);
        } catch (Exception e) {
            LogTool.error(ErrorType.REPORT_ERROR, "Failed to report finding", e);
        } finally {
            if (findingReportBo != null) {
                findingReportBo.clear();
            }
        }
    }

    private List<FindingData> check(FindingReportBo findingReportBo) {
        TaintGraph taintGraph = findingReportBo.getTaintGraph();
        List<FindingData> findingDataList = new ArrayList<>();

        if (taintGraph == null) {
            if (LogTool.isDebugEnabled()) {
                logger.warn("TaintGraph is null");
            }
            return findingDataList;
        }
        List<TaintNode> taintFindings = taintGraph.getSinkNodes();
        if (taintFindings.isEmpty()) {
            if (LogTool.isDebugEnabled()) {
                logger.warn("Not found sink node in taintGraph");
            }
            return findingDataList;
        }
        FlowProcessingStation station = FlowProcessingStation.getInstance();
        for (TaintNode sinkNode : taintFindings) {
            String vulnType = sinkNode.getTaintData().getVulnType();
            Detector detector = station.getDetector(vulnType);
            if (detector == null) {
                LogTool.warn(ErrorType.DETECT_VULNERABILITY_ERROR, "Failed to get detector for SinkNode vulnType,vulnType is " + vulnType);
                continue;
            }

            LinkedList<TaintData> flowLinks = taintGraph.bfs(sinkNode);
            if (detector.detect(flowLinks, findingReportBo.getRequestData(), findingReportBo.getResponseData())) {
                TaintFindingData findingData = new TaintFindingData();
                findingData.setFlowData(flowLinks);
                findingData.setVulnerableType(vulnType);
                //设置漏洞等级
                findingData.setLevel(detector.getLevel());
                findingDataList.add(findingData);
            }
        }
        return findingDataList;
    }

    private void buildAndReport(FindingReportBo findingReportBo, List<FindingData> findingDataList) throws JsonProcessingException {
        ReportData reportData = new ReportData(ApplicationModel.getAgentId());
        reportData.setRequestData(findingReportBo.getRequestData());
        reportData.setResponseData(findingReportBo.getResponseData());
        reportData.setFindingDataList(findingDataList);
        String reportJson = ReportBuilder.build(reportData);
        //debug模式 默认打印漏洞信息
        if (IASTContext.getContext().isOfflineEnabled()) {
            ReportPrinter.print(reportJson);
        } else {
            ReportSender.send(reportJson);
        }
    }
}
