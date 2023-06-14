package com.keven1z.core.log;

import com.keven1z.core.Config;
import org.apache.log4j.Logger;


/**
 * 参考{@link <a href="https://github.com/baidu/openrasp/blob/master/agent/java/engine/src/main/java/com/baidu/openrasp/messaging/LogTool.java">...</a>}
 */
public class LogTool {

    private static final Logger LOGGER = Logger.getLogger(LogTool.class);

    public static void warn(ErrorType errorType, String message, Throwable t) {
        LOGGER.warn(new ExceptionModel(errorType, message), t);
    }

    public static void error(ErrorType errorType, String message, Throwable t) {
        LOGGER.error(new ExceptionModel(errorType, message), t);
    }

    public static void warn(ErrorType errorType, String message) {
        LOGGER.warn(new ExceptionModel(errorType, message));
    }

    public static void error(ErrorType errorType, String message) {
        LOGGER.error(new ExceptionModel(errorType, message));
    }

    public static boolean isDebugEnabled() {
        return Config.getConfig().isPrintDebugLog();
    }

}
