package com.keven1z.core.hook;

import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.hook.http.HttpContext;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.pojo.SingleFindingData;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.keven1z.core.Config.MAX_REPORT_QUEUE_SIZE;

public class HookThreadLocal {
    /**
     * hook锁，防止在hook过程中调用方法递归hook，导致栈溢出
     * 注意：当在初始化hook时，若在doSpy方法中存在class初始化，则该class并不会被{@link com.keven1z.core.hook.transforms.HookTransformer}中transform方法捕获，导致栈溢出或者hook点的丢失
     */
    public static final ThreadLocal<Boolean> enableHookLock = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    /**
     * 当前请求上报漏洞list
     */
    public static final ThreadLocal<List<SingleFindingData>> SINGLE_FINDING_THREADLOCAL = new ThreadLocal<>();
    /**
     * 请求是否结束
     */
    public static final ThreadLocal<Boolean> isRequestEnd = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * 当前线程的污点传播图数据
     */
    public static final ThreadLocal<TaintGraph> TAINT_GRAPH_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 当前线程请求流量
     */
    public static final ThreadLocal<HttpContext> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static final int INVOKE_ID_INIT_VALUE = 1;

    public static final AtomicInteger INVOKE_ID = new AtomicInteger(INVOKE_ID_INIT_VALUE);

    public static final LinkedBlockingQueue<ReportData> REPORT_QUEUE = new LinkedBlockingQueue<>(MAX_REPORT_QUEUE_SIZE);
    /**
     * 请求消耗的时间计算
     */
    public static final ThreadLocal<Long> REQUEST_TIME_CONSUMED = new ThreadLocal<>();

}
