package com.keven1z.core;

import com.keven1z.core.error.ConfigLoadException;
import com.keven1z.core.error.http.RegistrationException;
import com.keven1z.core.hook.http.HttpSpy;
import com.keven1z.core.hook.normal.SingleSpy;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.model.IASTContext;
import com.keven1z.core.monitor.*;
import com.keven1z.core.model.server.AuthenticationDto;
import com.keven1z.core.policy.IastHookConfig;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.policy.ServerPolicyManager;
import com.keven1z.core.hook.taint.TaintSpy;
import com.keven1z.core.hook.transforms.HookTransformer;
import com.keven1z.core.log.LogConfig;
import com.keven1z.core.model.server.AgentDTO;
import com.keven1z.core.utils.FileUtils;
import com.keven1z.core.utils.http.*;
import org.apache.log4j.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.lang.spy.SimpleIASTSpyManager;
import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.function.Supplier;
import static com.keven1z.core.consts.CommonConst.*;


/**
 * 引擎加载类
 *
 * @author keven1z
 * @since 2023/02/21
 */
public class EngineController {
    public static final IASTContext context = IASTContext.getContext();
    private static final Logger logger = Logger.getLogger(EngineController.class);

    public void start(Instrumentation inst, String projectName, Boolean enableDetailedLogging, String projectVersion) throws Exception {
        /*
         * 打印banner
         */
        printBanner();
        /*
         * 构建agent上下文对象
         */
        initializeContext(inst, projectName, enableDetailedLogging, projectVersion);
        /*
         * 加载日志
         */
        initializeLoggingSystem();

        /*
         * 判定是否为debug模式，若不为debug模式，则进行正常注册
         */
        handleRegistration();
        loadAgentComponents();
        /*
         * 启动监控进程
         */
        startMonitoringSystem();
        /*
         * 完成初始化
         */
        finalizeInitialization();
    }

    private void loadClassTransformers() {
        initSpy();
        initTransformer();
    }
    private void handleRegistration() throws RegistrationException {
        if (context.isOfflineEnabled()) {
            ApplicationModel.setAgentId(OFFLINE_AGENT_NAME);
            return;
        }

        configureServerConnection();
        performRegistration();
    }
    private void configureServerConnection() {
        Map<Class<?>, Supplier<? extends Closeable>> clients = Collections.unmodifiableMap(new HashMap<Class<?>, Supplier<? extends Closeable>>() {{
            put(AuthClient.class, () -> new AuthClient(context.getServerUrl()));
            put(ReportClient.class, () -> new ReportClient(context.getServerUrl()));
            put(HttpHeartbeatClient.class, () -> new HttpHeartbeatClient(context.getServerUrl()));
            put(PolicyClient.class, () -> new PolicyClient(context.getServerUrl()));
        }});
        HttpClientRegistry.getInstance().initAll(clients);
    }
    private void performRegistration() throws RegistrationException {
        AuthClient authClient = HttpClientRegistry.getInstance().getClient(AuthClient.class);
        AuthenticationDto authenticationDTO = authClient.register(buildRegisterInformation());
        if (authenticationDTO == null) {
            throw new RegistrationException("Failed to register agent");
        }
        // 更新应用配置
        ApplicationModel.setAgentId(authenticationDTO.getAgentId());
        EngineController.context.setToken(authenticationDTO.getToken());
    }
    private void loadAgentComponents() throws IOException {
        loadHookConfigs();
        if (!context.isOfflineEnabled()) {
            ServerPolicyManager serverPolicyManager = ServerPolicyManager.getInstance();
            serverPolicyManager.loadInitialPolicy();
        }
        loadHookBlacklist();
        loadClassTransformers();
    }
    private void startMonitoringSystem() {
        MonitorManager.start(
                new DirectReportMonitor(),
                new TrafficReadingReportMonitor(),
                new ServerPolicyMonitor(),
                new HeartbeatMonitor()
        );
    }
    /**
     * @param inst        Instrumentation
     * @param projectName 绑定的项目名
     */
    private void initializeContext(Instrumentation inst, String projectName, Boolean enableDetailedLogging, String projectVersion) {
        loadProperties();
        context.setInstrumentation(inst);
        context.setBindProjectName(projectName);
        context.setEnableDetailedLogging(enableDetailedLogging);
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
    private void initializeLoggingSystem() {
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
     * 加载hook配置
     */
    private void loadHookConfigs() throws ConfigLoadException {
        try {
            IastHookConfig iastHookConfig = FileUtils.loadHookConfig(this.getClass().getClassLoader());
            if (iastHookConfig == null) {
                throw new ConfigLoadException("Hook config file could be null");
            }
            context.setHookConfig(iastHookConfig);
            IastHookManager.getManager().loadConfig(iastHookConfig);
        }
        catch (IOException e) {
            System.err.println("[SimpleIAST] Failed to load hook config,cause:" + e.getMessage());
            throw new ConfigLoadException("Failed to load hook config,cause:" + e.getMessage());
        }

    }

    private void loadHookBlacklist() throws IOException {
        Set<String> blackSet = FileUtils.loadBlackList(this.getClass().getClassLoader());
        context.setBlackList(blackSet);
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
        HookTransformer hookTransformer = new HookTransformer(instrumentation);
        context.setTransformer(hookTransformer);
        if (instrumentation.isNativeMethodPrefixSupported()) {
            instrumentation.setNativeMethodPrefix(hookTransformer, hookTransformer.getNativePrefix());
        }
        hookTransformer.reTransform();

    }

    /**
     * 打印banner信息
     */
    private void printBanner() {
        String s = " __ _                 _         _____  _    __  _____ \n" +
                "/ _(_)_ __ ___  _ __ | | ___    \\_   \\/_\\  / _\\/__   \\\n" +
                "\\ \\| | '_ ` _ \\| '_ \\| |/ _ \\    / /\\//_\\\\ \\ \\   / /\\/\n" +
                "_\\ \\ | | | | | | |_) | |  __/ /\\/ /_/  _  \\_\\ \\ / /   \n" +
                "\\__/_|_| |_| |_| .__/|_|\\___| \\____/\\_/ \\_/\\__/ \\/    \n" +
                "               |_|                                    ";
        System.out.println(s);
    }
    private void finalizeInitialization() {/*
         * agent设置为启动状态
         */
        ApplicationModel.start();

        final String mode = context.isOfflineEnabled() ? "离线模式" : "在线模式";
        final String hostName = ApplicationModel.getHostName();
        System.out.printf("[SimpleIAST] IAST启动成功. 模式=%s, 主机=%s%n", mode, hostName);

        // 核心启动日志（INFO级别）
        logger.info(String.format("[SimpleIAST] IAST启动成功. 模式=%s, 主机=%s", mode, hostName));

        if (logger.isDebugEnabled()) {
            // 调试信息（DEBUG级别）
            String debugMsg = String.format(
                    "IAST启动详情 => 模式=%s | 主机=%s | 系统=%s | PID=%s | JDK=%s | 路径=%s",
                    mode,
                    hostName,
                    ApplicationModel.getOS(),
                    ApplicationModel.getPID(),
                    ApplicationModel.getJdkVersion(),
                    ApplicationModel.getPath()
            );
            logger.debug(debugMsg);

            // 项目绑定信息
            String projectMsg = context.isOfflineEnabled()
                    ? "离线模式运行，未绑定项目"
                    : String.format("绑定项目: %s", context.getBindProjectName());
            logger.debug(projectMsg);

            // 策略信息
            logger.debug(String.format("安全策略: 总数=%d, Hook黑名单=%d",
                    context.getIastHookConfig().getHooks().size(),
                    context.getBlackList().size()));
        }
    }
}
