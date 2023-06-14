package com.keven1z.core.monitor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static com.keven1z.core.Config.CORE_POOL_SIZE;
import static com.keven1z.core.Config.MAXIMUM_POOL_SIZE;

public class MonitorPoolClient {

    private static final long KEEP_ALIVE_TIME = 1;
    private final ThreadPoolExecutor executor;
    TimeUnit unit = TimeUnit.SECONDS;

    private MonitorPoolClient() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(MAXIMUM_POOL_SIZE - CORE_POOL_SIZE);
        ThreadFactory threadFactory = new TaskTreadFactory();
        RejectedExecutionHandler handler = new IgnoreExecutionHandler();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, unit,
                workQueue, threadFactory, handler);
        executor.allowCoreThreadTimeOut(true);
//        executor.prestartAllCoreThreads();
    }

    public static MonitorPoolClient getClient() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final MonitorPoolClient instance = new MonitorPoolClient();
    }

    static class TaskTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable);
        }
    }

    /**
     * 申请创建任务，若线程池已满，则放弃创建。
     *
     */
    public synchronized void createTask(Runnable runnable) {
//        executor.execute(runnable);
        executor.execute(runnable);
    }
    public synchronized void shutdownNow() {
//        executor.execute(runnable);
        executor.shutdownNow();
    }
    /**
     * 判断线程池是否运行
     *
     * @return true is running,false is not running
     */
    public boolean isRunning() {
        return !executor.isShutdown();
    }

    public int getActiveCount() {
        return executor.getActiveCount();
    }

    /**
     * @return 线程池最大工作数量
     */
    public int getCorePoolSize() {
        return executor.getCorePoolSize();
    }


    /**
     * @return 线程池最大承载数量
     */
    public int getMaximumPoolSize() {
        return executor.getMaximumPoolSize();
    }
    public void close() throws InterruptedException {

    }
    private static class IgnoreExecutionHandler extends ThreadPoolExecutor.AbortPolicy {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //若线程阻塞，拒绝该线程调用，并请求系统调用java回收站，减少内存消耗

        }

    }
}
