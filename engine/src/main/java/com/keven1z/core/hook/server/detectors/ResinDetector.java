package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import java.security.ProtectionDomain;

/**
 * @author keven1z
 * @date 2024/09/22
 */
public class ResinDetector extends ServerDetector {
    private static final String SERVER_FLAG_CLASS = " com/caucho/server/resin/Resin".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.Resin.name();
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
            Class<?> versionClass = classLoader.loadClass("com.caucho.Version");
            if (versionClass != null) {
                version = (String) versionClass.getField("VERSION").get(null);
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