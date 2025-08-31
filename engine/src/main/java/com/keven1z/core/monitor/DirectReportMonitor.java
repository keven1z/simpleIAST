package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.model.server.ReportData;
import com.keven1z.core.vulnerability.report.ReportBuilder;
import com.keven1z.core.vulnerability.report.ReportHandler;

import static com.keven1z.core.hook.HookThreadLocal.REPORT_QUEUE;

/**
 * 直接漏洞上报
 */
public class DirectReportMonitor extends Monitor {


    @Override
    public String getThreadName() {
        return "SimpleIAST-Direct-Report-Thread";
    }

    @Override
    public boolean isForServer() {
        return false;
    }

    @Override
    public void doRun()  {
        ReportData reportData;
        try {
            reportData = REPORT_QUEUE.take();
            report(reportData);
            //降低漏洞处理频率，减少cpu 持续消耗
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("Failed to process report data", e);
        }
    }

    private void report(ReportData reportData) throws JsonProcessingException {
        String reportJson = ReportBuilder.build(reportData);
        //debug模式 默认打印漏洞信息
        if (IASTContext.getContext().isOfflineEnabled()) {
            ReportHandler.print(reportJson);
        } else {
            ReportHandler.send(reportJson);
        }
    }
}
