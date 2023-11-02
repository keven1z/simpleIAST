package com.keven1z.core.hook.normal;



import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.pojo.SingleFindingData;
import com.keven1z.core.vulnerability.NormalDetector;
import com.keven1z.core.vulnerability.detectors.WeakPasswordInSqlDetector;
import org.apache.log4j.Logger;
import java.lang.spy.SimpleIASTSpy;
import java.util.ArrayList;
import java.util.List;

import static com.keven1z.core.hook.HookThreadLocal.*;


public class SingleSpy implements SimpleIASTSpy {
    Logger logger = Logger.getLogger(SingleSpy.class);
    public static final List<NormalDetector> normalDetectorComposite = new ArrayList<>();

    static {
        //非污点跟踪漏洞
        normalDetectorComposite.add(new WeakPasswordInSqlDetector());
    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            doDetect(returnObject, thisObject, parameters, className, method, desc, type, policyName, isRequireHttp);
        } catch (Exception e) {
            logger.warn("Failed to handle " + className, e);
        } finally {
            enableHookLock.set(false);
        }
    }

    private void doDetect(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {

        for (NormalDetector normalDetector : normalDetectorComposite) {
            if (!normalDetector.getType().equals(policyName)) {
                continue;
            }
            boolean isDetected = normalDetector.detect(returnObject, thisObject, parameters);
            if (!isDetected) {
                continue;
            }
            Object reportDesc = normalDetector.getReportDesc();
            SingleFindingData singleFindingData = new SingleFindingData(className, method, desc, reportDesc);
            singleFindingData.setVulnerableType(policyName);
            //如果该漏洞不依赖HTTP流量则直接上报
            if (!isRequireHttp) {
                ReportData reportMessage = new ReportData(ApplicationModel.getAgentId());
                reportMessage.addFindingDataList(singleFindingData);
                REPORT_QUEUE.offer(reportMessage);
            } else if (REQUEST_THREAD_LOCAL.get() != null) {
                SINGLE_FINDING_THREADLOCAL.get().add(singleFindingData);
            }
        }
    }

    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName) {

    }

    @Override
    public void $_requestStarted(Object requestObject, Object responseObject) {

    }

    @Override
    public void $_requestEnded(Object requestObject, Object responseObject) {

    }

    @Override
    public void $_setRequestBody(Object body) {

    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) {

    }

    @Override
    public void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) {

    }

    @Override
    public void $_onReadInvoked(Integer b, Object inputStream) {

    }

}
