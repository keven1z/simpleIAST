package com.keven1z.core.hook.transforms;

import com.keven1z.core.hook.server.detectors.ServerDetector;
import com.keven1z.core.hook.server.detectors.TomcatDetector;
import com.keven1z.core.hook.server.detectors.SpringbootDetector;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import org.apache.log4j.Logger;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;


public class ServerDetectTransform implements ClassFileTransformer {
    private static final List<ServerDetector> SERVER_HOOKS = new ArrayList<>();

    static {
        SERVER_HOOKS.add(new TomcatDetector());
        SERVER_HOOKS.add(new SpringbootDetector());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (null == className) {
            return classfileBuffer;
        }
        for (final ServerDetector hook : SERVER_HOOKS) {
            if (hook.isClassMatched(className)) {
                boolean isDetect = hook.handleServerInfo(loader, protectionDomain);
                if (isDetect) {
                    if (LogTool.isDebugEnabled()) {
                        Logger.getLogger(getClass()).info("Detect server successfully,server type:" + ApplicationModel.getServerName());
                    }
                } else {
                    LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "Failed to detect server type by " + hook);
                }
            }
        }
        return classfileBuffer;
    }
}
