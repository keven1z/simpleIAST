package com.keven1z;


import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
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
//            readVersion();
            ModuleLoader.load(action, appName, isDebug, inst);
        }catch (InvocationTargetException invocationTargetException){
            PrintStream err = System.err;
            err.println("[SimpleIAST] Failed to load engine, will continue without simpleIAST.");
            invocationTargetException.printStackTrace(err);
            release();
        }
        catch (Exception e) {
            PrintStream err = System.err;
            err.println("[SimpleIAST] Failed to initialize agent, will continue without simpleIAST.");
            e.printStackTrace(err);
            release();
        }
    }


    public static String getAgentBindAppName() {
        return System.getProperty("iast.app.name", DEFAULT_APP_NAME);
    }

    public static boolean isDebugMode() {
        return Boolean.parseBoolean(System.getProperty("iast.debug"));
    }

}
