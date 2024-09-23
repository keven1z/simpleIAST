package com.keven1z.core.utils;

import com.keven1z.core.log.LogTool;
import org.apache.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * transformer守护者
 * <p>
 * 用来保护transformer的操作所产生的事件不被响应
 * </p>
 */
public class TransformerProtector {

    private final Logger logger = Logger.getLogger(getClass());

    private final ThreadLocal<AtomicInteger> isInProtectingThreadLocal = ThreadLocal.withInitial(() -> new AtomicInteger(0));

    /**
     * 进入守护区域
     *
     * @return 守护区域当前引用计数
     */
    public int enterProtecting() {
        final int referenceCount = isInProtectingThreadLocal.get().getAndIncrement();
        if (LogTool.isDebugEnabled()) {
            logger.debug("thread:" + Thread.currentThread() + " enter protect:" + referenceCount);
        }
        return referenceCount;
    }

    /**
     * 离开守护区域
     *
     * @return 守护区域当前引用计数
     */
    public int exitProtecting() {
        final int referenceCount = isInProtectingThreadLocal.get().decrementAndGet();
        // assert referenceCount >= 0;
        if (referenceCount == 0) {
            isInProtectingThreadLocal.remove();
            if (LogTool.isDebugEnabled()) {
                logger.debug("thread:" + Thread.currentThread() + " exit protect:" + referenceCount + " with clean");
            }
        } else if (referenceCount > 0) {
            if (LogTool.isDebugEnabled()) {
                logger.debug("thread:" + Thread.currentThread() + " exit protect:" + referenceCount);
            }
        } else {
            logger.warn("thread:" + Thread.currentThread() + " exit protect:" + referenceCount + "with error!");
        }
        return referenceCount;
    }

    /**
     * 判断当前是否处于守护区域中
     *
     * @return TRUE:在守护区域中；FALSE：非守护区域中
     */
    public boolean isInProtecting() {
        final boolean res = isInProtectingThreadLocal.get().get() > 0;
        if (!res) {
            isInProtectingThreadLocal.remove();
        }
        return res;
    }

    /**
     * Sandbox守护者单例
     */
    public static final TransformerProtector instance = new TransformerProtector();

}
