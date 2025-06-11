package com.keven1z.core.hook.taint;

import com.keven1z.core.consts.HookType;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.TransformerProtector;
import org.apache.log4j.Logger;
import java.lang.spy.SimpleIASTSpy;
import static com.keven1z.core.model.Config.MAX_REPORT_QUEUE_SIZE;
import static com.keven1z.core.hook.HookThreadLocal.*;

public class TaintSpy implements SimpleIASTSpy {
    private final TaintSpyHandler spyHandler = TaintSpyHandler.getInstance();
    private static final Logger logger = Logger.getLogger(TaintSpy.class);

    private TaintSpy() {
    }

    public static TaintSpy getInstance() {
        return Inner.taintSpy;
    }

    private static class Inner {
        public static final TaintSpy taintSpy = new TaintSpy();
    }

    /**
     * 对方法返回值、当前对象、方法参数进行taint标记
     *
     * @param returnObject 被污染的对象，即方法的返回值
     * @param thisObject 当前对象
     * @param parameters 方法参数
     * @param className 被hook的类名
     * @param method 被hook的方法名
     * @param desc 方法描述符
     * @param type taint类型
     * @param from 污点来源
     * @param to 污点去向
     */
    @Override
    public void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type) {
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
            if (REQUEST_THREAD_LOCAL.get() == null) {
                return;
            }
            if (isRequestEnded.get()) {
                return;
            }
            /*
             * 如果上报线程满了，不进行hook
             */
            if (REPORT_QUEUE.size() >= MAX_REPORT_QUEUE_SIZE) {
                if (LogTool.isDebugEnabled()){
                    logger.warn("上报队列已满,目前队列大小：" + REPORT_QUEUE.size());
                }
                return;
            }

            if (TAINT_GRAPH_THREAD_LOCAL.get() == null) {
                return;
            }
            /*
             * 如果污点图为空，并且不为污染源节点，则不处理
             */
            if (TAINT_GRAPH_THREAD_LOCAL.get().isEmpty() && !HookType.SOURCE.name().equals(type)) {
                return;
            }
            spyHandler.doHandle(returnObject, thisObject, parameters, className, method, desc, type);
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
        isRequestEnded.set(false);
        REQUEST_THREAD_LOCAL.remove();
        INVOKE_ID.set(INVOKE_ID_INIT_VALUE);
        SINGLE_FINDING_THREADLOCAL.remove();
    }
}
