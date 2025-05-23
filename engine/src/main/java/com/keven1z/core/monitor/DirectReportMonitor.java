package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.model.server.ReportData;
import com.keven1z.core.vulnerability.report.ReportBuilder;
import com.keven1z.core.vulnerability.report.ReportPrinter;
import com.keven1z.core.vulnerability.report.ReportSender;

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
        } catch (Exception ignore) {

        }
    }

    private void report(ReportData reportData) throws JsonProcessingException {
        String reportJson = ReportBuilder.build(reportData);
        //debug模式 默认打印漏洞信息
        if (IASTContext.getContext().isOfflineEnabled()) {
            ReportPrinter.print(reportJson);
        } else {
            ReportSender.send(reportJson);
        }
    }
}
