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
    private  Object engineObject;

    public ModuleContainer(String jarName) {
        try {
            File originFile = new File(baseDirectory + File.separator + jarName);
            ModuleLoader.classLoader = new SimpleIASTClassLoader(originFile.getAbsolutePath());
            Class<?> classOfEngine = classLoader.loadClass(CLASS_OF_CORE_ENGINE);
            engineObject = classOfEngine.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            System.err.println("[SimpleIAST] Failed to initialize module jar: " + jarName);
        }
    }

    public void start(Instrumentation inst) throws Throwable {
        Method method = engineObject.getClass().getMethod("start", Instrumentation.class);
        method.invoke(engineObject, inst);
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
