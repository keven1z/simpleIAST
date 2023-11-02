package com.keven1z;




import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.keven1z.Module.*;

/**
 * @author keven1z
 * @date 2023/5/16
 */
public class Agent {
    public static String projectVersion;
    public static String buildTime;
    public static String gitCommit;
    /*
     * 启动模式.
     *  START_MODE_OFFLINE:离线模式.
     *  START_MODE_SERVER:服务器模式.
     *
     */
    public static final String START_MODE = START_MODE_OFFLINE;

    public static void premain(String args, Instrumentation inst) {
        init(START_MODE, START_ACTION_INSTALL, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        init(START_MODE, START_ACTION_INSTALL, inst);
    }

    /**
     * attack 机制加载 agent
     *
     * @param mode 启动模式
     * @param inst {@link Instrumentation}
     */
    public static synchronized void init(String mode, String action, Instrumentation inst) {
        try {
            JarFileHelper.addJarToBootstrap(inst);
            readVersion();
            ModuleLoader.load(mode, action, inst);
        } catch (Throwable e) {
            System.err.println("[SimpleIAST] Failed to initialize, will continue without security protection.");
            e.printStackTrace();
        }
    }

    public static void readVersion() throws IOException {
        Class<?> clazz = Agent.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = Objects.requireNonNull(clazz.getResource(className)).toString();
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        projectVersion = attr.getValue("Project-Version");
        buildTime = attr.getValue("Build-Time");
        gitCommit = attr.getValue("Git-Commit");

        projectVersion = (projectVersion == null ? "UNKNOWN" : projectVersion);
        buildTime = (buildTime == null ? "UNKNOWN" : buildTime);
        gitCommit = (gitCommit == null ? "UNKNOWN" : gitCommit);
    }

}
