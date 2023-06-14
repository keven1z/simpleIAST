package com.keven1z.core;


import com.keven1z.JarFileHelper;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.cli.*;
import java.lang.instrument.Instrumentation;
import java.util.List;

import static com.keven1z.Agent.*;

/**
 * @author keven1z
 * @date 2023/02/21
 */
public class EngineBoot {
    EngineController engineController = null;
    public void start(String mode, Instrumentation inst) {

        try {
            engineController = new EngineController();
            engineController.start(mode, inst);
        } catch (Throwable e) {
            System.err.println("[SimpleIAST] Engine load error,cause:" + e.getMessage());
        }
    }

    /**
     * @param targetJvmPid 目标进程id
     * @param agentJarPath agent jar包路径
     * @param cfg
     * @throws Exception
     */
    private static void attachAgent(final String targetJvmPid,
                                    final String agentJarPath,
                                    final String cfg) throws Exception {

        VirtualMachine vmObj = null;
        try {

            vmObj = VirtualMachine.attach(targetJvmPid);
            if (vmObj != null) {
                vmObj.loadAgent(agentJarPath, cfg);
            }

        } finally {
            if (null != vmObj) {
                vmObj.detach();
            }
        }
        System.out.println("Attach successfully jvm pid:" + targetJvmPid + ",agentJarPath:" + agentJarPath);

    }

    private static void printVirtualMachine() {
        List<VirtualMachineDescriptor> vmList = VirtualMachine.list();
        System.out.println("Process Name\tPID");
        for (VirtualMachineDescriptor vmd : vmList) {
            System.out.println(vmd.displayName() + "\t" + vmd.id());
        }
    }

    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("h", "help", false, "print options information");
            options.addOption("v", "version", false, "print the version of iast");
            options.addOption("p", "pid", true, "attach jvm pid");
            options.addOption("l", "list", false, "list all jvm pid");

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
            } else if (cmd.hasOption("p")) {
                attachAgent(cmd.getOptionValue("p"), JarFileHelper.getAgentPath(), "");
            } else if (cmd.hasOption("l")) {
                printVirtualMachine();
            } else {
                helpFormatter.printHelp("java -jar iast-engine.jar", options, true);
            }
        } catch (Throwable e) {
            System.err.println("Failed to Attach,reason:" + e.getMessage()+",Java.home:"+System.getProperty("java.home"));
        }
    }

}
