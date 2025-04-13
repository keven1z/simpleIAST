package com.keven1z.core.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

/**
 * 操作系统信息工具类
 * @author keven1z
 * @since 2025/4/12
 */
public class OSUtils {
    public static double getSystemCpuLoad() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();

        // 获取 CPU 使用率
        double[] load = new double[]{processor.getSystemCpuLoad(1000)}; // 采样间隔 1 秒
        return  load[0] * 100;
    }
    public static double getSystemMemory() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        // 1. 物理内存信息
        long totalMemory = memory.getTotal();            // 总物理内存（字节）
        long availableMemory = memory.getAvailable();    // 可用物理内存（字节）
        long usedMemory = totalMemory - availableMemory; // 已用物理内存
        return  (usedMemory * 100.0) / totalMemory;
    }
}
