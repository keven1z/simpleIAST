package com.keven1z.core.hook.server.detectors;

import java.security.ProtectionDomain;

public abstract class ServerDetector {
    public abstract boolean isClassMatched(String className);
    public abstract boolean processServerInfo(ClassLoader classLoader, ProtectionDomain domain);

    public abstract String getServerName();
}
