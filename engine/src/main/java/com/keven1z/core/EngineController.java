package com.keven1z.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.error.RegistrationException;
import com.keven1z.core.hook.http.HttpSpy;
import com.keven1z.core.hook.normal.SingleSpy;
import com.keven1z.core.monitor.TrafficReadingReportMonitor;
import com.keven1z.core.pojo.AuthenticationDTO;
import com.keven1z.core.pojo.ResponseDTO;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.hook.transforms.HookTransformer;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogConfig;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.monitor.InstructionMonitor;
import com.keven1z.core.monitor.MonitorManager;
import com.keven1z.core.monitor.DirectReportMonitor;
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

    public void start(Instrumentation inst, String projectName, boolean isDebug, String projectVersion) throws Exception {
        /*
         * 打印banner
         */
        banner();
        /*
         * 构建agent上下文对象
         */
        buildContext(inst, projectName, isDebug, projectVersion);
        /*
         * 加载日志
         */
        loadLog();

        /*
         * 判定是否为debug模式，若不为debug模式，则进行正常注册
         */
        if (!context.isOfflineEnabled()) {
            /*
             * 设置服务器地址
             */
            IASTHttpClient.getClient().setRequestHost(context.getServerUrl());
            register();
            System.out.println(String.format("[SimpleIAST] IAST agent successfully registered, server url: %s", context.getServerUrl()));

        } else {
            ApplicationModel.setAgentId(OFFLINE_AGENT_NAME);
        }
        /*
         * 加载hook策略
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
        MonitorManager.start(new DirectReportMonitor(), new TrafficReadingReportMonitor(), new InstructionMonitor());
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
            logger.info("Server path:" + ApplicationModel.getPath());

            if (!context.isOfflineEnabled()) {
                logger.info("Bind project name:" + context.getBindProjectName());
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
     * 发送注册请求
     */
    public static void register() throws RegistrationException {
        try {
            // 构建注册信息并发送请求
            AgentDTO agentDTO = buildRegisterInformation();
            String requestBody = JsonUtils.toString(agentDTO);
            String responseBody = IASTHttpClient.getClient().register(requestBody);

            // 解析响应
            ResponseDTO<Object> responseDTO = JsonUtils.toObject(responseBody, ResponseDTO.class);
            // 处理失败响应
            if (!responseDTO.isFlag()) {
                String errorMsg = String.format("Registration failed. Reason: %s", responseDTO.getMessage());
                logger.warn(errorMsg);
                throw new RegistrationException(errorMsg);
            }
            // 校验响应数据类型
            AuthenticationDTO authData = JsonUtils.convertObject(responseDTO.getData(), AuthenticationDTO.class);
            if (authData == null || authData.getAgentId() == null || authData.getToken() == null) {
                String errorMsg = "Incomplete authentication data. AgentId or Token is missing.";
                logger.error(errorMsg);
                throw new RegistrationException(errorMsg);
            }

            // 更新应用配置
            ApplicationModel.setAgentId(authData.getAgentId());
            EngineController.context.setToken(authData.getToken());

            // 记录成功日志
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Registration successful. AgentID: %s", authData.getAgentId()));
            }
        } catch (JsonProcessingException e) {
            String errorMsg = "JSON serialization/deserialization failed during registration.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常

        } catch (IOException e) {
            String errorMsg = "Network communication error during registration.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常

        } catch (RegistrationException e) {
            throw e;
        } catch (Exception e) {
            String errorMsg = "Unexpected error during registration process.";
            logger.error(errorMsg, e);
            throw new RegistrationException(errorMsg, e); // 重新抛出自定义异常
        }
    }


    /**
     * @param inst        Instrumentation
     * @param projectName 绑定的项目名
     */
    private void buildContext(Instrumentation inst, String projectName, boolean isDebug, String projectVersion) {
        loadProperties();
        context.setInstrumentation(inst);
        context.setBindProjectName(projectName);
        context.setDebug(isDebug);
        context.setAgentVersion(projectVersion);
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

    private static AgentDTO buildRegisterInformation() {
        AgentDTO agentDTO = new AgentDTO(ApplicationModel.getAgentId(),
                ApplicationModel.getHostName(), ApplicationModel.getOS(), ApplicationModel.getPath(), ApplicationModel.getWebClass());
        agentDTO.setVersion(context.getAgentVersion());
        agentDTO.setProjectName(context.getBindProjectName());
        agentDTO.setJdkVersion(ApplicationModel.getJdkVersion());
        agentDTO.setProcess(ApplicationModel.getPID());
        return agentDTO;
    }

    /**
     * 初始化类字节码的转换器
     */
    private void initTransformer() {
        Instrumentation instrumentation = context.getInstrumentation();
        HookTransformer hookTransformer = new HookTransformer(context.getPolicy(), instrumentation);
        if (instrumentation.isNativeMethodPrefixSupported()) {
            instrumentation.setNativeMethodPrefix(hookTransformer, hookTransformer.getNativePrefix());
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
