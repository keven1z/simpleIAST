package com.keven1z;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

import static com.keven1z.ModuleLoader.*;

/**
 * @author keven1z
 * @date 2023/02/21
 */

public class ModuleContainer implements Module {

    private static final String CLASS_OF_CORE_ENGINE = "com.keven1z.core.EngineBoot";
    private Object engineObject;
    private String appName;
    private boolean isDebug;

    public ModuleContainer(String jarName, String appName, boolean isDebug) {
        try {
            File originFile = new File(baseDirectory + File.separator + jarName);
            if (!originFile.exists()) {
                throw new RuntimeException("[SimpleIAST] The file " + originFile.getAbsolutePath() + " does not exist.");
            }
            ModuleLoader.classLoader = new SimpleIASTClassLoader(originFile.getAbsolutePath());
            Class<?> classOfEngine = classLoader.loadClass(CLASS_OF_CORE_ENGINE);
            engineObject = classOfEngine.getDeclaredConstructor().newInstance();
            this.appName = appName;
            this.isDebug = isDebug;
        } catch (Throwable t) {
            System.err.println("[SimpleIAST] Failed to initialize module jar: " + jarName);
        }
    }

    public void start(Instrumentation inst) throws Exception {
        Method method = engineObject.getClass().getMethod("start", Instrumentation.class, String.class, Boolean.class, String.class);
        method.invoke(engineObject, inst, appName, isDebug, projectVersion);
    }

    public void shutdown() throws Throwable {
        try {
            if (engineObject != null) {
                Method method = engineObject.getClass().getMethod("shutdown");
                method.invoke(engineObject);
            }
        } catch (Throwable t) {
            System.err.println("[SimpleIAST] Failed to shutdown module");
            throw t;
        }
    }

}
