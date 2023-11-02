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
    private final Object objectOfCoreConfigure;
    public ModuleContainer(String jarName) throws Throwable {
        try {
            File originFile = new File(baseDirectory + File.separator + jarName);
            ModuleLoader.classLoader = new SimpleIASTClassLoader(originFile.getAbsolutePath()) ;
            Class<?> classOfEngine = classLoader.loadClass(CLASS_OF_CORE_ENGINE);
            objectOfCoreConfigure = classOfEngine.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            System.err.println("[SimpleIAST] Failed to initialize module jar: " + jarName);
            throw t;
        }
    }

    public void start(String mode, Instrumentation inst) throws Throwable {
        Method method = objectOfCoreConfigure.getClass().getMethod("start", String.class, Instrumentation.class);
        method.invoke(objectOfCoreConfigure, mode, inst);
    }

//    public void release(String mode) throws Throwable {
//        try {
//            if (iastCore != null) {
//                iastCore.release(mode);
//            }
//        } catch (Throwable t) {
//            System.err.println("[OpenRASP] Failed to release module: " + moduleName);
//            throw t;
//        }
//    }

}
