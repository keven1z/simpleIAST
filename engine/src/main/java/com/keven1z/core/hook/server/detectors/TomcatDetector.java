package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Tomcat服务器检测
 */
public class TomcatDetector extends ServerDetector {
    private static final String SERVER_INFO = " org.apache.catalina.util.ServerInfo".substring(1);

    @Override
    public String getServerName() {
        return ServerEnum.TOMCAT.name();
    }

    @Override
    public boolean isClassMatched(String className) {
        return className.equals(" org/apache/catalina/Server".substring(1));
    }

    @Override
    public boolean handleServerInfo(ClassLoader classLoader, ProtectionDomain domain) {
        String version = "";
        try {
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            Class<?> clazz = classLoader.loadClass(SERVER_INFO);
            if (!isJboss(classLoader)) {
                version = (String) ReflectionUtils.invokeMethod(null, clazz, "getServerNumber", new Class[]{});
                ApplicationModel.setServerInfo(getServerName(), version);
                return true;
            }
        } catch (Throwable t) {
            LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Handle tomcat startup failed", t);
        }
        return false;
    }

    private boolean isJboss(ClassLoader classLoader) {
        Package jbossBootPackage = null;
        try {
            Method getPackageMethod = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
            getPackageMethod.setAccessible(true);
            jbossBootPackage = (Package) getPackageMethod.invoke(classLoader, "org.jboss");
            if (jbossBootPackage == null) {
                jbossBootPackage = (Package) getPackageMethod.invoke(classLoader, "org.jboss.modules");
            }
        } catch (Throwable e) {
            // ignore
        }
        return jbossBootPackage != null;
    }

}
