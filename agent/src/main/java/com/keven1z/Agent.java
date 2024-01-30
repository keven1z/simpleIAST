package com.keven1z;


import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.keven1z.Module.*;
import static com.keven1z.ModuleLoader.release;

/**
 * @author keven1z
 * @date 2023/5/16
 */
public class Agent {
    public static String projectVersion;
    public static String buildTime;
    public static String gitCommit;


    public static void premain(String args, Instrumentation inst) {
        init(START_ACTION_INSTALL, getAgentBindAppName(), isDebugMode(), inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        String[] agentArgArr = agentArgs.split(",");
        if (agentArgArr.length < 3) {
            System.err.println("[SimpleIAST] Missing attach parameters,parameters length is" + agentArgArr.length);
        }
        init(agentArgArr[0], agentArgArr[1], Boolean.parseBoolean(agentArgArr[2]), inst);
    }

    /**
     * attack 机制加载 agent
     *
     * @param appName 应用名称
     * @param inst    {@link Instrumentation}
     */
    public static synchronized void init(String action, String appName, boolean isDebug, Instrumentation inst) {
        try {
            JarFileHelper.addJarToBootstrap(inst);
            readVersion();
            ModuleLoader.load(action, appName, isDebug, inst);
        } catch (Exception e) {
            System.err.println("[SimpleIAST] Failed to initialize, will continue without simpleIAST.");
            System.err.println("[SimpleIAST] Reason:" + e.getMessage());
            release();
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

    public static String getAgentBindAppName() {
        return System.getProperty("iast.app.name", DEFAULT_APP_NAME);
    }

    public static boolean isDebugMode() {
        return Boolean.parseBoolean(System.getProperty("iast.debug"));
    }

}
