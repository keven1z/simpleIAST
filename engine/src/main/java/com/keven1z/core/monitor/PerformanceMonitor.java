package com.keven1z.core.monitor;


import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;


public class PerformanceMonitor extends Monitor {
    public static final double MEM_THRESHOLD = 1.0;


    @Override
    public String getThreadName() {
        return "SimpleIAST-PerformanceMonitor-Thread";
    }

    @Override
    public boolean isForServer() {
        return true;
    }

    @Override
    public void doRun() throws Exception {
        if (isMemRatioExceeds()) {
            LogTool.warn(ErrorType.MEMORY_ERROR, "内存超过阈值" );
            System.out.println("[simpleIAST] 内存超过阈值");
            ApplicationModel.stop();
            stopRunning();
        }
        Thread.sleep(2000);

    }

    /**
     * 内存占比是否超出阈值
     */
    private boolean isMemRatioExceeds() {
        OperatingSystemMXBean jsxs = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemorySize = jsxs.getTotalPhysicalMemorySize();
        long usedPhysicalMemorySize = totalPhysicalMemorySize - jsxs.getFreePhysicalMemorySize();
        double ratio = (double) usedPhysicalMemorySize / totalPhysicalMemorySize;
        if (ratio > MEM_THRESHOLD) {
            logger.info("Current use memory:" + usedPhysicalMemorySize + ",total memory:" + totalPhysicalMemorySize);
            return true;
        }
        return false;
    }

}
