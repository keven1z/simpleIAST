package com.keven1z;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.net.URLDecoder;
import java.util.Objects;


/**
 * @author keven1z
 * @date 2023/02/21
 */
public class ModuleLoader {
    public static final String ENGINE_JAR = "iast-engine.jar";

    public static String baseDirectory;

    private static ModuleLoader instance;
    public static  SimpleIASTClassLoader classLoader;
    public static String projectVersion;
    public static String buildTime;
    public static String gitCommit;

    // ModuleLoader 为 classloader加载的，不能通过getProtectionDomain()的方法获得JAR路径
    static {
        // juli
        try {
            Class<?> clazz = Class.forName("java.nio.file.FileSystems");
            clazz.getMethod("getDefault").invoke(null);
        } catch (Throwable t) {
            // ignore
        }
        Class<?> clazz = ModuleLoader.class;
        // path值示例：　file:/opt/apache-tomcat-xxx/rasp/rasp.jar!/com/fuxi/javaagent/Agent.class
        String path = Objects.requireNonNull(clazz.getResource("/" + clazz.getName().replace(".", "/") + ".class")).getPath();
        if (path.startsWith("file:")) {
            path = path.substring(5);
        }
        if (path.contains("!")) {
            path = path.substring(0, path.indexOf("!"));
        }
        try {
            baseDirectory = URLDecoder.decode(new File(path).getParent(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            baseDirectory = new File(path).getParent();
        }
    }

    private ModuleLoader(String mode, Instrumentation inst){
        ModuleContainer engineContainer;
        try {
            engineContainer = new ModuleContainer(ENGINE_JAR);
            engineContainer.start(mode, inst);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

//    public static boolean isCustomClassloader() {
//        try {
//            String classLoader = ClassLoader.getSystemClassLoader().getClass().getName();
//            if (classLoader.startsWith("com.oracle") && classLoader.contains("weblogic")) {
//                return true;
//            }
//            return isModularityJdk();
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public static boolean isModularityJdk() {
        String javaVersion = System.getProperty("java.version");
        String[] version = javaVersion.split("\\.");
        if (version.length >= 2) {
            int major;
            int minor;
            try {
                major = Integer.parseInt(version[0]);
                minor = Integer.parseInt(version[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (major == 1) {
                return minor >= 9;
            } else return major >= 9;
        } else if (javaVersion.startsWith("9")) {
            return true;
        } else if (javaVersion.length() >= 2) {
            char first = javaVersion.charAt(0);
            char second = javaVersion.charAt(1);
            return first >= '1' && first <= '9' && second >= '0' && second <= '9';
        }
        return false;
    }

    /**
     * 加载所有 IAST 模块
     *
     * @param mode 启动模式
     * @param inst {@link java.lang.instrument.Instrumentation}
     */
    public static synchronized void load(String mode, String action, Instrumentation inst) {
        if (Module.START_ACTION_INSTALL.equals(action)) {
            if (instance == null) {
                try {
                    instance = new ModuleLoader(mode, inst);
                } catch (Throwable t) {
                    instance = null;
                    throw t;
                }
            } else {
                System.err.println("[SimpleIAST] The SimpleIAST has bean initialized and cannot be initialized again");
            }
        }
        else {
            throw new IllegalStateException("[SimpleIAST] Can not support the action: " + action);
        }

    }
}
