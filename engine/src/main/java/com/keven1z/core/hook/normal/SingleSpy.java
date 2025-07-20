package com.keven1z.core.hook.normal;


import com.keven1z.core.consts.VulnerabilityType;
import com.keven1z.core.error.taint.DetectorNotFoundException;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.server.ReportData;
import com.keven1z.core.model.finding.SingleFindingData;
import com.keven1z.core.vulnerability.DetectContext;
import com.keven1z.core.vulnerability.Detector;
import com.keven1z.core.vulnerability.DetectorFactory;
import org.apache.log4j.Logger;

import java.lang.spy.SimpleIASTSpy;

import static com.keven1z.core.hook.HookThreadLocal.*;


public class SingleSpy implements SimpleIASTSpy {
    Logger logger = Logger.getLogger(SingleSpy.class);
    private final DetectorFactory detectorFactory = DetectorFactory.getInstance();

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            if (!ApplicationModel.isRunning()) {
                return;
            }
            doDetect(returnObject, thisObject, parameters, className, method, desc,  policyName, isRequireHttp);
        } catch (Exception e) {
            logger.warn("Failed to handle " + className, e);
        } finally {
            enableHookLock.set(false);
        }
    }

    private void doDetect(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc,  String policyName, boolean isRequireHttp) throws DetectorNotFoundException {
        VulnerabilityType vulnerabilityType;
        try {
            vulnerabilityType = VulnerabilityType.valueOf(policyName.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error(String.format("无效漏洞类型: {}", policyName));
            return;
        }
        Detector detector = detectorFactory.getDetector(vulnerabilityType);
        if (detector == null) {
            throw new DetectorNotFoundException(vulnerabilityType.name());
        }
        DetectContext normalContext = new DetectContext.Builder()
                .returnObject(returnObject)
                .thisObject(thisObject)
                .parameters(parameters)
                .build();
        boolean isDetect = detector.detect(normalContext);
        if (!isDetect) {
            if (logger.isDebugEnabled()) {
                logger.warn("Not found vulnerability for " + policyName);
            }
            return;
        }
        Object reportDesc = detector.getReportDesc();
        SingleFindingData singleFindingData = new SingleFindingData(className, method, desc, reportDesc);
        singleFindingData.setVulnerableType(policyName);
        singleFindingData.setLevel(detector.getLevel().getPriority());
        //如果该漏洞不依赖HTTP流量则直接上报
        if (!isRequireHttp) {
            ReportData reportMessage = new ReportData(ApplicationModel.getAgentId());
            reportMessage.addFindingDataList(singleFindingData);
            REPORT_QUEUE.offer(reportMessage);
        } else if (REQUEST_THREAD_LOCAL.get() != null) {
            SINGLE_FINDING_THREADLOCAL.get().add(singleFindingData);
        }
    }

    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type) {

    }

    @Override
    public void $_arrayTaint(Object arrayObject, int index, Object arrayValue, String className, String method, String desc) {

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
