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
public class GlassfishDetector extends ServerDetector {
    private static final String SERVER_FLAG_CLASS = " com/sun/enterprise/glassfish/bootstrap/ASMain".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.GLASSFISH.name();
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
            Class<?> clazz = classLoader.loadClass("com.sun.appserv.server.util.Version");
            if (clazz != null) {
                version = (String) ReflectionUtils.invokeMethod(null, clazz, "getVersion", new Class[]{});
            }
            return true;
        } catch (Exception e) {
            LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Load glassfish server info failed", e);
        } finally {
            ApplicationModel.setServerInfo(getServerName(), version);
        }
        return false;
    }
}