package com.keven1z.core;


import com.keven1z.JarFileHelper;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.monitor.MonitorManager;
import com.keven1z.core.utils.ClassUtils;
import com.keven1z.core.utils.HttpClientUtils;
import net.bytebuddy.agent.VirtualMachine;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import java.lang.instrument.Instrumentation;
import static com.keven1z.Agent.*;

/**
 * @author keven1z
 * @date 2023/02/21
 */
public class EngineBoot {
    EngineController engineController = null;

    public void start(Instrumentation inst) {

        try {
            addShutdownHook();
            engineController = new EngineController();
            engineController.start(inst);
        } catch (Throwable e) {
            System.err.println("[SimpleIAST] Engine load error,cause:" + e.getMessage());
        }
    }

    /**
     * 关闭agent
     */
    public void shutdown() {
        if (HttpClientUtils.deregister()) {
            Logger.getLogger(getClass()).info("Agent deregister successfully,hostName:" + ApplicationModel.getHostName() + ",id:" + ApplicationModel.getAgentId());
        } else {
            Logger.getLogger(getClass()).warn("Agent deregister failed,hostName:" + ApplicationModel.getHostName() + ",id:" + ApplicationModel.getAgentId());
        }
        ApplicationModel.stop();
        System.out.println("[SimpleIAST] Stop Hook Successfully");
        EngineController.context.clear();
        System.out.println("[SimpleIAST] Clear Cache Successfully");
        HttpClientUtils.close();
        System.out.println("[SimpleIAST] Close HttpClient Successfully");
        MonitorManager.clear();
        try {
            ClassUtils.closeSimpleIASTClassLoader();
        } catch (Exception e) {
            System.err.println("[SimpleIAST] Shutdown Failed,Reason:" + e.getMessage());
        }
        System.out.println("[SimpleIAST] Stop Running Successfully");

    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("SimpleIAST-Deregister-Thread") {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    /**
     * @param targetJvmPid 目标进程id
     * @param agentJarPath agent jar包路径
     * @param mode         运行模式
     */
    private static void attachAgent(final String targetJvmPid,
                                    final String agentJarPath,
                                    final String mode) throws Exception {

        VirtualMachine machine = null;
        try {
            machine = VirtualMachine.ForHotSpot.attach(targetJvmPid);
            machine.loadAgent(agentJarPath, mode);
        } finally {
            if (null != machine) {
                machine.detach();
            }
        }
        if (INSTALL.equals(mode)) {
            System.out.println("[SimpleIAST] Agent install successfully,Application pid:" + targetJvmPid);
        } else {
            System.out.println("[SimpleIAST] Agent uninstall successfully.Application pid:" + targetJvmPid);
        }

    }

    private static final String INSTALL = "install";
    private static final String UNINSTALL = "uninstall";

    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("h", "help", false, "print options information");
            options.addOption("v", "version", false, "print the version of iast");
            options.addOption("m", "mode", true, "使用模式: install 和 uninstall");
            options.addOption("p", "pid", true, "attach jvm pid");

            HelpFormatter helpFormatter = new HelpFormatter();
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("v")) {
                readVersion();
                System.out.println("Version:       " + projectVersion + "\n" +
                        "Build Time:    " + buildTime + "\n" +
                        "Git Commit ID: " + gitCommit);
            } else if (cmd.hasOption("h")) {
                helpFormatter.printHelp("java -jar iast-engine.jar", options, true);
            } else if (cmd.hasOption("m")) {
                String mode = cmd.getOptionValue("m");
                checkMode(mode);
                String agentPath = JarFileHelper.getAgentPath();
                if (System.getProperty("os.name").startsWith("Windows")) {
                    agentPath = agentPath.substring(1);
                    agentPath = agentPath.replace("/", "\\");
                }
                attachAgent(cmd.getOptionValue("p"), agentPath, mode);
            } else {
                helpFormatter.printHelp("java -jar iast-engine.jar", options, true);
            }
        } catch (Throwable e) {
            System.err.println("Failed to Attach,reason:" + e.getMessage());
        }
    }

    /**
     * 检测mode参数是否为 install 或者 uninstall
     *
     * @param mode 运行模式
     */
    private static void checkMode(String mode) {
        if (INSTALL.equals(mode) || UNINSTALL.equals(mode)) {
            return;
        } else {
            throw new RuntimeException("Illegal parameter mode，Please please add the parameter -m/--mode,only install or uninstall");
        }
    }

}
