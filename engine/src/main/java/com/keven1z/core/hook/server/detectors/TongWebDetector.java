package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.ReflectionUtils;
import java.security.ProtectionDomain;

/**
 * @author keven1z
 * @date 2024/09/22
 */
public class TongWebDetector extends ServerDetector {
    private static final String SERVER_FLAG_CLASS = " com/tongweb/web/thor/Server".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.TongWeb.name();
    }

    @Override
    public boolean isClassMatched(String className) {
        return className.equals(SERVER_FLAG_CLASS);
    }

    @Override
    public boolean processServerInfo(ClassLoader classLoader, ProtectionDomain domain) {
        String version = "";
        try {
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            Class<?> clazz = classLoader.loadClass("com.tongweb.web.thor.util.ServerInfo");
            if (clazz != null) {
                version = (String) ReflectionUtils.invokeMethod(null, clazz, "getServerNumber", new Class[]{});
            }
            return true;
        } catch (Exception e) {
            LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Load wildfly server info failed", e);
        } finally {
            ApplicationModel.setServerInfo(getServerName(), version);
        }
        return false;
    }
}