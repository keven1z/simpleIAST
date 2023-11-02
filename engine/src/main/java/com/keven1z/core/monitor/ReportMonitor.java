package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.EngineController;
import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.vulnerability.report.ReportBuilder;
import com.keven1z.core.vulnerability.report.ReportPrinter;
import com.keven1z.core.vulnerability.report.ReportSender;

import static com.keven1z.core.hook.HookThreadLocal.REPORT_QUEUE;

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
        ReportData reportData;
        try {
            reportData = REPORT_QUEUE.take();
            report(reportData);
            //降低漏洞处理频率，减少cpu 持续消耗
            Thread.sleep(1000);
        } catch (Exception e){

        }
    }
    private  void report(ReportData reportData) throws JsonProcessingException {
        String reportJson = ReportBuilder.build(reportData);
        String mode = EngineController.context.getMode();
        //debug模式 默认打印漏洞信息
        if (IASTContext.getContext().isOfflineEnabled()) {
            ReportPrinter.print(reportJson);
        } else if (CommonConst.MODE_NORMAL.equals(mode)) {
            ReportSender.send(reportJson);
        }
    }
}
