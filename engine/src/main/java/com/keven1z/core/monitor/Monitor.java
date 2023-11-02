package com.keven1z.core.monitor;

import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import org.apache.log4j.Logger;

public abstract class Monitor extends Thread {
    Logger logger = Logger.getLogger(getClass());

    public abstract String getThreadName();

    /**
     * 是否为server服务的监控线程
     */
    public abstract boolean isForServer();

    private boolean state = true;

    @Override
    public void run() {
        /*
         * 如果为server服务的进程且为离线模式，停止运行
         */
        if (isForServer()) {
            if (IASTContext.getContext().isOfflineEnabled()) {
                stopRunning();
            }
        }

        while (isRunning()) {
            try {
                this.doRun();
            } catch (Throwable e) {
                stopRunning();
                if (ApplicationModel.isRunning()) {
                    System.err.println("Failed to execute " + getThreadName());
                    logger.error("Failed to execute " + getThreadName(), e);
                }
            }
        }

    }

    public abstract void doRun() throws Exception;

    public boolean isRunning() {
        return state;
    }

    public void stopRunning() {
        this.state = false;
    }

    public void startRunning() {
        this.state = true;
    }
}
