package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.ReflectionUtils;
import java.security.ProtectionDomain;

/**
 * @author keven1z
 * @since 2024/09/22
 */
public class WebLogicDetector extends ServerDetector {
    private static final String SERVER_FLAG_CLASS = " weblogic/Server".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.WEBLOGIC.name();
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
            Class<?> clazz = classLoader.loadClass("weblogic.version");
            if (clazz != null) {
                version = (String) ReflectionUtils.invokeMethod(null, clazz, "getVersions", new Class[]{});
            }
            return true;
        } catch (Exception e) {
            LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Load weblogic server info failed", e);
        } finally {
            ApplicationModel.setServerInfo(getServerName(), version);
        }
        return false;
    }
}