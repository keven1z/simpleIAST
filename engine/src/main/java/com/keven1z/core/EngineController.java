package com.keven1z.core;

import com.keven1z.core.hook.http.HttpSpy;
import com.keven1z.core.hook.normal.SingleSpy;
import com.keven1z.core.monitor.FindingReportMonitor;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.hook.transforms.HookTransformer;
import com.keven1z.core.hook.transforms.ServerDetectTransform;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogConfig;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.monitor.InstructionMonitor;
import com.keven1z.core.monitor.MonitorManager;
import com.keven1z.core.monitor.ReportMonitor;
import com.keven1z.core.pojo.AgentDTO;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.utils.FileUtils;
import com.keven1z.core.utils.IASTHttpClient;
import com.keven1z.core.utils.JsonUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.spy.SimpleIASTSpyManager;
import java.lang.instrument.Instrumentation;
import java.util.List;

import static com.keven1z.core.consts.CommonConst.*;


/**
 * 引擎加载类
 *
 * @author keven1z
 * @date 2023/02/21
 */
public class EngineController {
    public static final IASTContext context = IASTContext.getContext();
    private static final Logger logger = Logger.getLogger(EngineController.class);

    public void start(Instrumentation inst, String appName, boolean isDebug) throws Exception {
        /*
         * 打印banner
         */
        banner();
        /*
         * 构建agent上下文对象
         */
        buildContext(inst, appName, isDebug);
        /*
         * 加载日志
         */
        loadLog();

        /*
         * 判定是否为debug模式，若不为debug模式，则进行正常注册
         */
        if (!context.isOfflineEnabled()) {
            try {
                /*
                 * 设置服务器地址
                 */
                IASTHttpClient.getClient().setRequestHost(context.getServerUrl());
                if (!register()) {
                    System.err.println("[SimpleIAST] Failed to register,server url:" + context.getServerUrl());
                    throw new RuntimeException("The failure occurred when registering,server url:" + context.getServerUrl());
                } else {
                    System.out.println("[SimpleIAST] IAST agent successfully registered,server url:" + context.getServerUrl());
                }
            } catch (Exception e) {
                LogTool.error(ErrorType.REGISTER_ERROR, "Register failed,hostName:" + ApplicationModel.getHostName(), e);
                throw new RuntimeException("Exception occurred during registration");
            }
        } else {
            ApplicationModel.setAgentId(OFFLINE_AGENT_NAME);
        }
        /*
         * 加载策略
         */
        loadPolicy();
        /*
         * 加载hook黑名单
         */
        loadBlackList();
        /*
         * 加载转化类
         */
        loadTransform();
        /*
         * 启动监控进程
         */
        MonitorManager.start(new ReportMonitor(), new FindingReportMonitor(), new InstructionMonitor());
        /*
         * agent设置为启动状态
         */
        ApplicationModel.start();

        System.out.println("[SimpleIAST] IAST run successfully");
        Logger.getLogger(getClass()).info("SimpleIAST run successfully,hostName:" + ApplicationModel.getHostName());
        if (LogTool.isDebugEnabled()) {
            logger.info("HostName:" + ApplicationModel.getHostName());
            logger.info("OS:" + ApplicationModel.getOS());
            logger.info("PID:" + ApplicationModel.getPID());
            logger.info("Jdk version:" + ApplicationModel.getJdkVersion());
            if (!context.isOfflineEnabled()) {
                logger.info("Bind app name:" + context.getBindApplicationName());
            }
            logger.info("The number of Policy:" + context.getPolicyContainer().getPolicySize());
            logger.info("The number of hook black list:" + context.getBlackList().size());
        }
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
        String webServerPath = ApplicationModel.getPath();
        AgentDTO agentDTO = new AgentDTO(agentId, hostName, os, webServerPath, ApplicationModel.getWebClass());
        agentDTO.setAppName(context.getBindApplicationName());
        boolean isSuccess = IASTHttpClient.getClient().register(JsonUtils.toString(agentDTO));
        if (isSuccess) {
            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(EngineController.class).info("Register successful,agentId:" + ApplicationModel.getAgentId());
            }
            return true;
        }
        if (LogTool.isDebugEnabled()) {
            Logger.getLogger(EngineController.class).info("Register failed,Server url:" + context.getServerUrl());
        }
        return false;
    }

    /**
     * @param inst    Instrumentation
     * @param appName 绑定的应用名
     */
    private void buildContext(Instrumentation inst, String appName, boolean isDebug) {
        loadProperties();
        context.setInstrumentation(inst);
        context.setBindApplicationName(appName);
        context.setDebug(isDebug);
    }

    /**
     * 加载iast.properties
     */
    private void loadProperties() {
        String url = FileUtils.loadIASTProperties(EngineController.class.getClassLoader(), SERVER_URL_IAST_PROPERTIES, DEFAULT_SERVER_URL);
        context.setServerUrl(url);
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
            SimpleIASTSpyManager.init(TaintSpy.getInstance(), new HttpSpy(), new SingleSpy());
        }
    }

    /**
     * 加载策略
     */
    private void loadPolicy() throws IOException {
        PolicyContainer policyContainer = FileUtils.load(this.getClass().getClassLoader());
        if (policyContainer == null) {
            LogTool.error(ErrorType.POLICY_ERROR, "policyContainer is null");
            throw new RuntimeException("Policy load failed");
        }
        context.setPolicyContainer(policyContainer);
    }

    private void loadBlackList() throws IOException {
        List<String> blackList = FileUtils.loadBlackList(this.getClass().getClassLoader());
        context.setBlackList(blackList);
    }

    /**
     * 初始化类字节码的转换器
     */
    private void initTransformer() {
        Instrumentation instrumentation = context.getInstrumentation();
        context.getInstrumentation().addTransformer(new ServerDetectTransform(), true);
        HookTransformer hookTransformer = new HookTransformer(context.getPolicy(),instrumentation );
        if(instrumentation.isNativeMethodPrefixSupported()){
            instrumentation.setNativeMethodPrefix(hookTransformer,hookTransformer.getNativePrefix());
        }
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
