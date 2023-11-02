package com.keven1z.core;

import com.keven1z.core.consts.CommonConst;

public class Config {
    private Config() {
        logDebugProperties = System.getProperty(CommonConst.ENV_DEBUG_LOG);
    }

    public static Config getConfig() {
        return Inner.CONFIG;
    }
    public final int MAX_BODY_SIZE = 4096;
    /**
     * 是否输出debug日志
     */
    private final String logDebugProperties;

    private static class Inner {
        private static final Config CONFIG = new Config();
    }

    /**
     * 策略文件路径
     */
    public final static String POLICY_FILE_PATH = "policy.json";
    /**
     * hook黑名单路径
     */
    public final static String BLACK_LIST_FILE_PATH = "blacklist.txt";
    public final static String WEAK_PASSWORD_FILE_PATH = "weak_password.txt";

    /**
     * 服务端地址
     */
    public static final String IAST_SERVER = "http://127.0.0.1:8989";
    /**
     * 监控线程最大工作数量
     */
    public static final int CORE_POOL_SIZE = 5;
    /**
     * 监控线程池最大数量
     */
    public static final int MAXIMUM_POOL_SIZE = 6;
    /**
     * 是否dump hook的class
     */
    public static final boolean IS_DUMP_CLASS = true;
    /**
     * 最大上报队列大小
     */
    public static final int MAX_REPORT_QUEUE_SIZE = 5;
    /**
     * 日志最大备份天数
     */
    public static final int MAX_LOG_BACKUP_DAYS = 7;
    /**
     * 日志每分钟上传的条数
     */
    public static final int MAX_LOG_BURST_NUMBER = 100;
    /**
     * 最大请求报文长度
     */
    public static final int MAX_REQUEST_MESSAGE_LENGTH = 8192;
    /**
     * 漏洞报告调用栈深度
     */
    public static final int MAX_STACK_DEPTH = 5;

    public boolean isPrintDebugLog() {
//        if (logDebugProperties == null) {
//            return false;
//        }
//        return logDebugProperties.equals(String.valueOf(CommonConst.ON));
        return true;
    }
}
