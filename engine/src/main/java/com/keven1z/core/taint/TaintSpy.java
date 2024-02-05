package com.keven1z.core.taint;

import com.keven1z.core.EngineController;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.utils.TransformerProtector;
import org.apache.log4j.Logger;
import java.lang.spy.SimpleIASTSpy;
import static com.keven1z.core.Config.MAX_REPORT_QUEUE_SIZE;
import static com.keven1z.core.consts.VulnEnum.WEAK_PASSWORD_IN_SQL;
import static com.keven1z.core.hook.HookThreadLocal.*;

public class TaintSpy implements SimpleIASTSpy {
    private PolicyContainer policyContainer;
    private final TaintSpyHandler spyHandler = TaintSpyHandler.getInstance();
    private static final Logger logger = Logger.getLogger(TaintSpy.class);

    private TaintSpy() {
    }

    public static TaintSpy getInstance() {
        return Inner.taintSpy;
    }

    public PolicyContainer getPolicyContainer() {
        return policyContainer;
    }

    private static class Inner {
        public static final TaintSpy taintSpy = new TaintSpy();
    }

    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, String from, String to) {
        if (enableHookLock.get()) {
            return;
        } else {
            enableHookLock.set(true);
        }
        try {
            /*
             * 判断agent是否开启，若关闭不进行hook
             */
            if (!ApplicationModel.isRunning()) {
                return;
            }
            if (TransformerProtector.instance.isInProtecting()) {
                return;
            }
            //如果没有流量，不进行hook
            if (REQUEST_THREAD_LOCAL.get() == null && !WEAK_PASSWORD_IN_SQL.name().equals(policyName)) {
                return;
            }
            if (isRequestEnd.get()) {
                return;
            }
            /*
             * 如果上报线程满了，不进行hook
             */
            if (REPORT_QUEUE.size() >= MAX_REPORT_QUEUE_SIZE) {
                if (LogTool.isDebugEnabled()){
                    logger.warn("上报队列已满,目前队列大小：" + REPORT_QUEUE.size());
                }
            }

            if (policyContainer == null) {
                policyContainer = EngineController.context.getPolicyContainer();
            }
            spyHandler.doHandle(returnObject, thisObject, parameters, className, method, desc, type, policyName, from, to);
        } catch (Exception e) {
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to taint", e);
        } finally {
            enableHookLock.set(false);
        }
    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {

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

    public void clear() {
        TAINT_GRAPH_THREAD_LOCAL.remove();
        SANITIZER_RESOLVER_CACHE.remove();
        isRequestStart.set(false);
        isRequestEnd.set(false);
        REQUEST_THREAD_LOCAL.remove();
        INVOKE_ID.set(INVOKE_ID_INIT_VALUE);
        SINGLE_FINDING_THREADLOCAL.remove();
    }
}
