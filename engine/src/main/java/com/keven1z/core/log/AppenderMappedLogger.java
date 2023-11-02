package com.keven1z.core.log;


import java.io.File;

public enum AppenderMappedLogger {
    ROOT("root", "IAST", File.separator + "iast.log"),
    POLICY_ALARM("com.keven1z.core.policy", "policy", File.separator +"policy.log"),
    TAINT("taint.info", "taint", File.separator+"taint.log"),
    HOOK("hook.info", "hook", File.separator+"hook.log");

    private final String logger;
    private final String appender;
    private final String targetPath;


    AppenderMappedLogger(String logger, String appender, String targetPath) {
        this.logger = logger;
        this.appender = appender;
        this.targetPath = targetPath;
    }

    public String getLogger() {
        return logger;
    }

    public String getAppender() {
        return appender;
    }

    public String getTargetPath() {
        return targetPath;
    }
}
