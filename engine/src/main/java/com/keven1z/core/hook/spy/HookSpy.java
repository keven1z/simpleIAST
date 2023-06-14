package com.keven1z.core.hook.spy;

import java.lang.spy.SimpleIASTSpy;

import com.keven1z.core.EngineController;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.policy.PolicyContainer;
import org.apache.log4j.Logger;

import static com.keven1z.core.Config.MAX_REPORT_QUEUE_SIZE;

import static com.keven1z.core.hook.spy.HookThreadLocal.*;

public class HookSpy implements SimpleIASTSpy {

    private static final Logger logger = Logger.getLogger(HookSpy.class);

    public static PolicyContainer policyContainer;

    private final HookSpyHandler spyHandler = HookSpyHandler.getInstance();

    @Override
    public void doSpy(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isEnter) {
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
            /*
             * 如果上报线程满了，不进行hook
             */
            if (IS_REPORT_QUEUE_FULL.get()) {
                clear();
                return;
            }
            /*
             * 如果存在漏洞，且不为流量hook点，不再进行hook解析处理
             */
            if (isSuspectedTaint.get() && !PolicyTypeEnum.HTTP.toString().equals(type)) {
                return;
            }

            if (REPORT_QUEUE.size() == MAX_REPORT_QUEUE_SIZE) {
                IS_REPORT_QUEUE_FULL.set(true);
                clear();
                return;
            }
            //如果没有流量，不进行hook
            if (REQUEST_THREAD_LOCAL.get() == null && !PolicyTypeEnum.HTTP.toString().equals(type)) {
                return;
            }

            if (policyContainer == null) {
                policyContainer = EngineController.context.getPolicyContainer();
            }
            spyHandler.doHandle(returnObject, thisObject, parameters, className, method, desc, type, policyName, isEnter);
        } catch (Exception e) {
            clear();
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to doSpy", e);

        } finally {
            enableHookLock.set(false);
        }
    }

    public static void clear() {
//        TAINT_GRAPH_THREAD_LOCAL.get().clear();
        TAINT_GRAPH_THREAD_LOCAL.remove();
        isRequestEnd.set(false);
        isSuspectedTaint.set(false);
        REQUEST_THREAD_LOCAL.remove();
        IS_REPORT_QUEUE_FULL.set(false);
        INVOKE_ID.set(INVOKE_ID_INIT_VALUE);
    }
}
