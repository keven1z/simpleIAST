package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.pojo.finding.FindingData;
import com.keven1z.core.pojo.finding.FindingReportBo;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.pojo.finding.TaintFindingData;
import com.keven1z.core.vulnerability.Detector;
import com.keven1z.core.vulnerability.FlowProcessingStation;
import com.keven1z.core.vulnerability.report.ReportBuilder;
import com.keven1z.core.vulnerability.report.ReportPrinter;
import com.keven1z.core.vulnerability.report.ReportSender;

import java.util.*;

import static com.keven1z.core.hook.HookThreadLocal.*;

/**
 * 通过读取流量漏洞上报
 */
public class TrafficReadingReportMonitor extends Monitor {


    @Override
    public String getThreadName() {
        return "SimpleIAST-Traffic-Report-Thread";
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
            buildAndReport(findingReportBo);
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
        Set<String> processedSinkClass = new HashSet<>();
        FlowProcessingStation station = FlowProcessingStation.getInstance();
        for (TaintNode sinkNode : taintFindings) {
            if (isDuplicateSink(sinkNode, processedSinkClass)) {
                continue;
            }
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
            /*
             * 添加已detect过的同一个调用栈上的类
             */
            processedSinkClass.add(sinkNode.getTaintData().getThisObject().getClass().getName());
        }
        processedSinkClass.clear();
        return findingDataList;
    }

    private boolean isDuplicateSink(TaintNode sinkNode, Set<String> processedSinkClass) {
        if (processedSinkClass.isEmpty()) {
            return false;
        }
        List<String> stackList = sinkNode.getTaintData().getStackList();
        for (String stack : stackList) {
            for (String processSinkClass : processedSinkClass) {
                if (stack.contains(processSinkClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void buildAndReport(FindingReportBo findingReportBo) throws JsonProcessingException {
        List<FindingData> findingDataList = check(findingReportBo);
        findingDataList.addAll(findingReportBo.getSingleFindingDataList());
        if (findingDataList.isEmpty()) {
            if (LogTool.isDebugEnabled()) {
                logger.warn("Failed to report finding,no vulnerabilities were detected.");
            }
            return;
        }
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
