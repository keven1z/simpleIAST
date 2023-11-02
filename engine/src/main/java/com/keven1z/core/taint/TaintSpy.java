package com.keven1z.core.taint;

import com.keven1z.core.EngineController;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.policy.PolicyContainer;
import org.apache.log4j.Logger;

import java.lang.spy.SimpleIASTSpy;

import static com.keven1z.core.Config.MAX_REPORT_QUEUE_SIZE;
import static com.keven1z.core.consts.VulnConst.WEAK_PASSWORD_IN_SQL;
import static com.keven1z.core.hook.HookThreadLocal.*;

public class TaintSpy implements SimpleIASTSpy {
    public static PolicyContainer policyContainer;
    private final TaintSpyHandler spyHandler = TaintSpyHandler.getInstance();
    private static final Logger LOGGER = Logger.getLogger(TaintSpy.class);

    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName) {
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

            //如果没有流量，不进行hook
            if (REQUEST_THREAD_LOCAL.get() == null && !WEAK_PASSWORD_IN_SQL.equals(policyName)) {
                return;
            }
            if (isRequestEnd.get()) {
                return;
            }
            /*
             * 如果上报线程满了，不进行hook
             */
            if (REPORT_QUEUE.size() >= MAX_REPORT_QUEUE_SIZE) {
                throw new RuntimeException("上报队列已满,目前队列大小：" + REPORT_QUEUE.size());
            }


            if (policyContainer == null) {
                policyContainer = EngineController.context.getPolicyContainer();
            }
            spyHandler.doHandle(returnObject, thisObject, parameters, className, method, desc, type, policyName);
        } catch (Exception e) {
            TAINT_GRAPH_THREAD_LOCAL.get().clear();
            clear();
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to taint", e);
        } finally {
            enableHookLock.set(false);
        }
    }

    @Override
    public void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc,String type, String policyName,boolean isRequireHttp) {

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

    public static void clear() {
        TAINT_GRAPH_THREAD_LOCAL.get().clear();
        TAINT_GRAPH_THREAD_LOCAL.remove();
        isRequestEnd.set(false);
        REQUEST_THREAD_LOCAL.remove();
        INVOKE_ID.set(INVOKE_ID_INIT_VALUE);
        SINGLE_FINDING_THREADLOCAL.remove();
    }
}
