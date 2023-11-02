/*
 * Copyright 2017-2021 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keven1z.core.log;


import com.keven1z.core.Config;
import org.apache.log4j.*;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.varia.NullAppender;

import java.io.File;

/**
 * Created by lxk on 17-4-10.
 * 用于导出日志配置
 */
public class LogConfig {

    /**
     * 初始化log4j的logger
     * 创建rasp.log、alarm.log、policy_alarm.log和plugin.log 的appender
     * 为appender增加限速
     */
    public static void ConfigFileAppender() throws Exception {
        initLog4jLogger();
        fileAppenderAddBurstFilter();
    }

    /**
     * 管理syslog
     */
//    public static void syslogManager() {
//        DynamicConfigAppender.removeSyslogAppender();
//        if (Config.getConfig().getSyslogSwitch()) {
//            String syslogUrl = Config.getConfig().getSyslogUrl();
//            try {
//                URI url = new URI(syslogUrl);
//                String syslogAddress = url.getHost();
//                int syslogPort = url.getPort();
//                if (syslogAddress != null && !syslogAddress.trim().isEmpty() && syslogPort >= 0 && syslogPort <= 65535) {
//                    DynamicConfigAppender.createSyslogAppender(syslogAddress, syslogPort);
//                } else {
//                    LogTool.warn(ErrorType.CONFIG_ERROR, "syslog url: " + syslogUrl + " is invalid");
//                }
//            } catch (Throwable t) {
//                LogTool.warn(ErrorType.CONFIG_ERROR, "syslog url: " +
//                        syslogUrl + " is invalid: " + t.getMessage(), t);
//            }
//        }
//    }

    /**
     * 为fileAppender添加限速filter
     */
    public static void fileAppenderAddBurstFilter() {
        for (AppenderMappedLogger type : AppenderMappedLogger.values()) {
            if (type.ordinal() <= 3) {
                if ("root".equals(type.getLogger())) {
                    Appender appender = Logger.getRootLogger().getAppender(type.getAppender());
                    if (appender instanceof FileAppender) {
                        appender.clearFilters();
                        appender.addFilter(createBurstFilter());
                    }
                } else {
                    Logger logger = Logger.getLogger(type.getLogger());
                    Appender appender = logger.getAppender(type.getAppender());
                    if (appender instanceof FileAppender) {
                        appender.clearFilters();
                        appender.addFilter(createBurstFilter());
                    }
                }
            }
        }
    }

    public static void initLog4jLogger() {
        String log4jBaseDir = getLog4jPath(false, null);
        for (AppenderMappedLogger type : AppenderMappedLogger.values()) {
            if ("root".equals(type.getLogger())) {
                if (log4jBaseDir.isEmpty()) {
                    Logger.getRootLogger().addAppender(createNullAppender(type.getAppender()));
                } else {
                    File file = new File(log4jBaseDir);
                    if (!file.exists() && file.isDirectory()) {
                        if (!file.mkdirs()) {
                            System.err.println("[SimpleIAST] Log Dir:" + log4jBaseDir + " create failed");
                        }
                    }
                    System.out.println("[SimpleIAST] Log Dir:" + log4jBaseDir);

                    String path = log4jBaseDir + type.getTargetPath();
                    BasicConfigurator.configure(createFileAppender(type.getAppender(), path));

                }

            } else {
                Logger logger = Logger.getLogger(type.getLogger());
                if (log4jBaseDir.isEmpty()) {
                    logger.addAppender(createNullAppender(type.getAppender()));
                } else {
                    File file = new File(log4jBaseDir);
                    if (!file.exists() && file.isDirectory()) {
                        if (!file.mkdirs()) {
                            System.err.println("[SimpleIAST] Log dir:" + log4jBaseDir + " create failed");
                        }
                    }
                    logger.addAppender(createFileAppender(type.getAppender(), log4jBaseDir + type.getTargetPath()));
                }

                logger.setAdditivity(false);
            }
            Logger.getRootLogger().setLevel(Level.INFO);
        }
        setLogMaxBackup();
        //初始化时是否开启log4j的debug的功能
        enableDebug();
    }

    /**
     * 创建NullAppender
     */
    public static NullAppender createNullAppender(String appenderName) {
        NullAppender appender = new NullAppender();
        appender.setName(appenderName);
        return appender;
    }

    /**
     * 创建fileAppender
     */
    public static IASTDailyRollingFileAppender createFileAppender(String appender, String targetPath) {
        IASTDailyRollingFileAppender fileAppender = new IASTDailyRollingFileAppender();
        fileAppender.setName(appender);
        fileAppender.setErrorHandler(new OnlyOnceErrorHandler());
        fileAppender.setFile(targetPath);
        fileAppender.setAppend(true);
        fileAppender.setDatePattern("'.'yyyy-MM-dd");
        fileAppender.setEncoding("UTF-8");
        PatternLayout layout = new PatternLayout();
        if ("IAST".equals(appender)) {
            layout.setConversionPattern("%d %-5p [%t][%c] %m%n");
        } else {
            layout.setConversionPattern("%m%n");
        }
        fileAppender.setLayout(layout);
        fileAppender.activateOptions();
        return fileAppender;
    }

    /**
     * 为fileAppender设置最大日志备份天数
     */
    public static void setLogMaxBackup() {
        for (AppenderMappedLogger type : AppenderMappedLogger.values()) {
            if (type.ordinal() <= 3) {
                if ("root".equals(type.getLogger())) {
                    Appender appender = Logger.getRootLogger().getAppender(type.getAppender());
                    if (appender instanceof FileAppender) {
                        IASTDailyRollingFileAppender fileAppender = (IASTDailyRollingFileAppender) appender;
                        fileAppender.setMaxBackupIndex(Config.MAX_LOG_BACKUP_DAYS);
                        fileAppenderRollFiles(fileAppender);
                    }
                } else {
                    Logger logger = Logger.getLogger(type.getLogger());
                    Appender appender = logger.getAppender(type.getAppender());
                    if (appender instanceof FileAppender) {
                        IASTDailyRollingFileAppender fileAppender = (IASTDailyRollingFileAppender) appender;
                        fileAppender.setMaxBackupIndex(Config.MAX_LOG_BACKUP_DAYS);
                        fileAppenderRollFiles(fileAppender);
                    }
                }
            }
        }
    }

    //手动触发日志文件rotate
    private static void fileAppenderRollFiles(IASTDailyRollingFileAppender fileAppender) {
        try {
            String fileName = fileAppender.getFile();
            fileAppender.rollFiles(new File(fileName));
        } catch (Exception e) {
            LogLog.warn("the appender " + fileAppender.getName() + " roll failed");
        }
    }

    /**
     * 获取log4j的日志自定义路径
     */
    private static String getLog4jPath(boolean isCloud, String path) {
        if (isCloud) {
            return path;
        } else {
            return System.getProperty("user.home") + File.separator + "logs" + File.separator + "simpleIAST";
        }
    }

    /**
     * log4j debug开关
     */
    public static void enableDebug() {
        if (LogTool.isDebugEnabled()) {
            LogLog.setInternalDebugging(true);
        } else {
            LogLog.setInternalDebugging(false);
        }
    }

    /**
     * 创建日志限速filter
     */
    public static BurstFilter createBurstFilter() {
        BurstFilter filter = new BurstFilter();
        int logMaxBurst = Config.MAX_LOG_BURST_NUMBER;
        filter.setMaxBurst(logMaxBurst);
        filter.setRefillAmount(logMaxBurst);
        filter.setRefillInterval(60);
        return filter;
    }

}
