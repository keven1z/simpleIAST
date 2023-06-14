package com.keven1z.core.hook.server.detectors;

import com.keven1z.core.model.ApplicationModel;

import java.security.ProtectionDomain;

/**
 * @description: 标识tomcat启动方式
 */
public class SpringbootDetector extends ServerDetector {
    @Override
    public boolean isClassMatched(String className) {
        return "org/apache/catalina/startup/Bootstrap".equals(className);
    }

    @Override
    public boolean handleServerInfo(ClassLoader classLoader, ProtectionDomain domain) {
        //tomcat标准启动方式设置为true，内置tomcat启动设置为false
        ApplicationModel.setStartUpInfo("true");
        return false;
    }

    @Override
    public String getServerName() {
        return ServerEnum.SPRINGBOOT.name();
    }
}
