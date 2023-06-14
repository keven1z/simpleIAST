package com.keven1z.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.hook.transforms.HookTransformer;
import com.keven1z.core.hook.transforms.ServerDetectTransform;
import com.keven1z.core.hook.spy.HookSpy;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogConfig;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.monitor.InstructionMonitor;
import com.keven1z.core.monitor.MonitorManager;
import com.keven1z.core.monitor.PerformanceMonitor;
import com.keven1z.core.monitor.ReportMonitor;
import com.keven1z.core.pojo.AgentDTO;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.policy.ContextLoader;
import com.keven1z.core.utils.HttpClientUtils;
import com.keven1z.core.utils.JsonUtils;
import com.keven1z.core.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.spy.SimpleIASTSpyManager;
import java.lang.instrument.Instrumentation;


/**
 * 引擎加载类
 *
 * @author keven1z
 * @date 2023/02/21
 */
public class EngineController {
    public static final IASTContext context = IASTContext.getContext();

    public void start(String mode, Instrumentation inst) {

        banner();
        loadLog();
        addShutdownHook();

        boolean isInit = init(inst, mode);
        /*
         * 如果初始化失败，退出agent
         */
        if (!isInit) {
            ApplicationModel.stop();
            return;
        }
        if (LogTool.isDebugEnabled()) {
            Logger.getLogger(getClass()).info(">>>>>>>Start Running, Mode:" + mode);
        }

        /*
         * 判定是否为debug模式，若不为debug模式，则进行正常注册
         */
        if (!context.isOfflineEnabled()) {
            try {
                if (!register()) {
                    System.err.println("[SimpleIAST] Register failed,hostName:" + ApplicationModel.getHostName());
                    return;
                } else {
                    System.out.println("[SimpleIAST] Register successful,hostName:" + ApplicationModel.getHostName());
                }
            } catch (Exception e) {
                LogTool.error(ErrorType.REGISTER_ERROR, "Register failed,hostName:" + ApplicationModel.getHostName(), e);
                System.err.println("Register failed,hostName:" + ApplicationModel.getHostName());
                return;
            }
        }

        loadPolicy();
        loadTransform();
        ApplicationModel.start();

        //启动监控进程
        MonitorManager.start(new ReportMonitor(), new PerformanceMonitor(), new InstructionMonitor());

        Logger.getLogger(getClass()).info("Agent run successfully,hostName:" + ApplicationModel.getHostName());
    }

    /**
     * 关闭agent
     */
    public void shutdown() {
        if (context.isOfflineEnabled()){
            if (HttpClientUtils.deregister()) {
                Logger.getLogger(getClass()).info("Agent deregister successfully,hostName:" + ApplicationModel.getHostName() + ",id:" + ApplicationModel.getAgentId());
            } else {
                Logger.getLogger(getClass()).warn("Agent deregister failed,hostName:" + ApplicationModel.getHostName() + ",id:" + ApplicationModel.getAgentId());
            }
        }

        ApplicationModel.stop();
        System.out.println("[SimpleIAST] Stop Hook Successfully");
        EngineController.context.clear();
        System.out.println("[SimpleIAST] Clear Cache Successfully");
        HttpClientUtils.close();
        System.out.println("[SimpleIAST] Close HttpClient Successfully");
        MonitorManager.clear();
        try {
            //关闭classLoader
            Class<?> loadClass = ClassLoader.getSystemClassLoader().loadClass("com.keven1z.ModuleLoader");
            Field field = loadClass.getField("classLoader");
            Object classloader = field.get(loadClass);
            ReflectionUtils.invokeMethod(classloader, "closeIfPossible", new Class[]{});
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

    private void loadTransform() {
        initSpy();
        initTransformer();
    }

    /**
     * 向服务器注册该应用
     */
    public static boolean register() throws Exception {
        String agentId = ApplicationModel.getAgentId();
        String hostName = ApplicationModel.getHostName();
        String os = ApplicationModel.getOS();
        String webServerPath = ApplicationModel.getWebServerPath();
        AgentDTO agentDTO = new AgentDTO(agentId, hostName, os, webServerPath);
        boolean isSuccess = HttpClientUtils.register(JsonUtils.toString(agentDTO));
        if (isSuccess) {
            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(EngineController.class).info("Register successful,agentId:" + ApplicationModel.getAgentId());
            }
            return true;
        }
        if (LogTool.isDebugEnabled()) {
            Logger.getLogger(EngineController.class).info("Register failed,agentId:" + ApplicationModel.getAgentId());
        }
        return false;
    }

    /**
     * @param inst Instrumentation
     * @param mode 启动模式
     * @return 是否初始化成功
     */
    private boolean init(Instrumentation inst, String mode) {
        try {
            initContext(inst, mode);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 加载日志类
     */
    private void loadLog() {
        try {
            LogConfig.ConfigFileAppender();
        } catch (Exception e) {
            System.err.println("[SimpleIAST] Log init failed,cause:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化hook监听处理类
     */
    private void initSpy() {
        if (!SimpleIASTSpyManager.isInit()) {
            SimpleIASTSpyManager.init(new HookSpy());
        }
    }

    /**
     * 初始化上下文对象
     *
     * @param inst Instrumentation
     * @param mode 启动模式
     */
    private void initContext(Instrumentation inst, String mode) {
        context.setInstrumentation(inst);
        context.setMode(mode);
    }

    /**
     * 加载策略
     */
    public void loadPolicy() {
        PolicyContainer policyContainer = loadPolicy(this.getClass().getClassLoader());
        if (policyContainer == null) {
            LogTool.error(ErrorType.POLICY_ERROR, "policyContainer is null");
            throw new RuntimeException("Policy load failed");
        } else {
            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(getClass()).info("Policy load count:" + policyContainer.getAllPolicies().size());
            }
        }
        context.setPolicyContainer(policyContainer);
    }


    /**
     * 加载策略文件
     *
     * @return {@link PolicyContainer} 策略容器
     */
    public PolicyContainer loadPolicy(ClassLoader classLoader) {
        try {
            return ContextLoader.load(classLoader);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化类字节码的转换器
     */
    private void initTransformer() {
        context.getInstrumentation().addTransformer(new ServerDetectTransform(), false);
        HookTransformer hookTransformer = new HookTransformer(context.getPolicy(), context.getInstrumentation());
        hookTransformer.reTransform();

    }

    /**
     * 打印banner信息
     */
    private void banner() {
        String s = " __ _                 _         _____  _    __  _____ \n" +
                "/ _(_)_ __ ___  _ __ | | ___    \\_   \\/_\\  / _\\/__   \\\n" +
                "\\ \\| | '_ ` _ \\| '_ \\| |/ _ \\    / /\\//_\\\\ \\ \\   / /\\/\n" +
                "_\\ \\ | | | | | | |_) | |  __/ /\\/ /_/  _  \\_\\ \\ / /   \n" +
                "\\__/_|_| |_| |_| .__/|_|\\___| \\____/\\_/ \\_/\\__/ \\/    \n" +
                "               |_|                                    ";
        System.out.println(s);
    }


}
