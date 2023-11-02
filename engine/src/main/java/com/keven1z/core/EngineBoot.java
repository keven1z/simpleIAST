package com.keven1z.core;


import com.keven1z.JarFileHelper;

import net.bytebuddy.agent.VirtualMachine;
import org.apache.commons.cli.*;
import java.lang.instrument.Instrumentation;

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

        VirtualMachine machine = null;
        try {
            machine = VirtualMachine.ForHotSpot.attach(targetJvmPid);
            machine.loadAgent(agentJarPath);
        } finally {
            if (null != machine) {
                machine.detach();
            }

        }
        System.out.println("Attach successfully.\n\tjvm pid:" + targetJvmPid + "\n\tagentJarPath:" + agentJarPath);

    }

//    private static void printVirtualMachine() {
//        List<VirtualMachineDescriptor> vmList = com.sun.tools.attach.VirtualMachine.list();
//        System.out.println("Process Name\tPID");
//        for (VirtualMachineDescriptor vmd : vmList) {
//            System.out.println(vmd.displayName() + "\t" + vmd.id());
//        }
//    }

    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("h", "help", false, "print options information");
            options.addOption("v", "version", false, "print the version of iast");
            options.addOption("p", "pid", true, "attach jvm pid");
//            options.addOption("l", "list", false, "list all jvm pid");

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
                String agentPath = JarFileHelper.getAgentPath();
                if (System.getProperty("os.name").startsWith("Windows")){
                    agentPath = agentPath.substring(1);
                    agentPath = agentPath.replace("/","\\");
                }
                attachAgent(cmd.getOptionValue("p"), agentPath, "");
            }  else {
                helpFormatter.printHelp("java -jar iast-engine.jar", options, true);
            }
        } catch (Throwable e) {
            System.err.println("Failed to Attach,reason:" + e.getMessage());
            e.printStackTrace();
        }
    }

}
