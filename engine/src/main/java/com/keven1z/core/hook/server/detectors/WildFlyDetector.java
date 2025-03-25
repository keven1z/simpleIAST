package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

/**
 * @author keven1z
 * @since 2024/09/22
 */
public class WildFlyDetector extends ServerDetector {
    private static final String SERVER_FLAG_CLASS = " org/jboss/as/server/Main".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.WILDFLY.name();
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
            Class<?> clazz = classLoader.loadClass("org.jboss.as.version.Version");
            if (clazz != null) {
                Field field = clazz.getDeclaredField("AS_VERSION");
                version = (String)field.get(null);
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