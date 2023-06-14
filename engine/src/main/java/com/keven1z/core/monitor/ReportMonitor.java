package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.vulnerability.DetectAndReportHandler;
import com.keven1z.core.vulnerability.report.ReportMessage;

import static com.keven1z.core.hook.spy.HookThreadLocal.REPORT_QUEUE;

public class ReportMonitor extends Monitor {


    @Override
    public String getThreadName() {
        return "SimpleIAST-Report-Thread";
    }

    @Override
    public boolean isForServer() {
        return false;
    }

    @Override
    public void doRun() throws InterruptedException, JsonProcessingException {
        ReportMessage reportMessage = null;
        try {
            reportMessage = REPORT_QUEUE.take();
            DetectAndReportHandler.doHandle(reportMessage);
            //降低漏洞处理频率，减少cpu 持续消耗
            Thread.sleep(1000);
        } finally {
            if (reportMessage != null) {
                reportMessage.clear();
            }
        }
    }
}
