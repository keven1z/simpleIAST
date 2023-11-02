package com.keven1z.core.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 存储应用信息
 */
public class ApplicationModel {
    public static final String WINDOWS = "windows";
    public static final String SERVER_PATH = "server_path";

    public static final String LINUX = "linux";
    public static final String MAC = "MAC";
    public static final String HOST_NAME = "hostname";
    public static final String UNKNOWN_HOST = "UnknownHost";
    public static final String OS = "os";
    public static final String PID = "pid";
    public static final String AGENT_ID = "agentId";
    public static final String WEB_CLASS = "web_class";

    private static final Map<String, String> applicationInfo;

    private static Map<String, String> systemEnvInfo;

    private static final String SERVER_TYPE_PATH = "/proc/self/cgroup";
    private static boolean APPLICATION_IS_RUNNING = false;

    static {
        systemEnvInfo = System.getenv();
        if (systemEnvInfo == null) {
            systemEnvInfo = new HashMap<>();
        }
        applicationInfo = new HashMap<>(8);
        String osName = System.getProperty("os.name");
        if (osName != null && osName.startsWith(LINUX)) {
            applicationInfo.put(OS, LINUX);
        } else if (osName != null && osName.startsWith(WINDOWS)) {
            applicationInfo.put(OS, WINDOWS);
        } else if (osName != null && osName.startsWith(MAC)) {
            applicationInfo.put(OS, MAC);
        } else {
            applicationInfo.put(OS, osName);
        }
        applicationInfo.put("language", "java");
        applicationInfo.put("server", "");
        applicationInfo.put("version", "");
        applicationInfo.put("extra", "");
        applicationInfo.put("StandardStart", "false");
        applicationInfo.put(HOST_NAME, getHostName());
        applicationInfo.put(PID, getPID());
        applicationInfo.put(AGENT_ID, getAgentId());
        applicationInfo.put(SERVER_PATH, getWebServerPath());
        applicationInfo.put(WEB_CLASS, getWebClass());

    }

    public static String getOS() {
        return applicationInfo.get(OS);
    }

    public static synchronized void setServerInfo(String serverName, String version) {
        serverName = (serverName == null ? "" : serverName);
        version = (version == null ? "" : version);
        applicationInfo.put("server", serverName);
        applicationInfo.put("version", version);
    }

    public static String getAgentId() {
        return applicationInfo.get(AGENT_ID);
    }

    public static void setAgentId(String agentId) {
        applicationInfo.put(AGENT_ID, agentId);
    }

    public static String getHostName() {
        if (isLinux()) {
            try {
                return (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException uhe) {
                String host = uhe.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        return host.substring(0, colon);
                    }
                }
                return UNKNOWN_HOST;
            }
        } else {
            return System.getenv("COMPUTERNAME") == null ? UNKNOWN_HOST : System.getenv("COMPUTERNAME");
        }
    }

    public static synchronized void setExtraInfo(String extra, String extraVersion) {
        extra = (extra == null ? "" : extra);
        applicationInfo.put("extra", extra);
        extraVersion = (extraVersion == null ? "" : extraVersion);
        applicationInfo.put("extraVersion", extraVersion);
    }

    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static synchronized void setStartUpInfo(String startUpInfo) {
        applicationInfo.put("StandardStart", startUpInfo);
    }

    public static Map<String, String> getApplicationInfo() {
        return applicationInfo;
    }

    public static Map<String, String> getSystemEnv() {
        return systemEnvInfo;
    }

    public static String getVersion() {
        String result = applicationInfo.get("version");
        if (StringUtils.isEmpty(result)) {
            result = applicationInfo.get("extraVersion");
        }
        return result;
    }

    public static String getServerName() {
        String result = applicationInfo.get("server");
        if (StringUtils.isEmpty(result)) {
            result = applicationInfo.get("extra");
        }
        return result;
    }

    public static boolean getStartUpInfo() {
        String result = applicationInfo.get("StandardStart");
        return Boolean.parseBoolean(result);
    }
    public static String getWebClass() {
        return System.getProperty("sun.java.command");
    }

    public static String getVMType() {
        String type = null;
        if (isLinux()) {
            File file = new File(SERVER_TYPE_PATH);
            if (file.exists() && file.isFile() && file.canRead()) {
                try {
                    String content = FileUtils.readFileToString(file, "utf-8");
                    if (StringUtils.contains(content, "docker")) {
                        type = "docker";
                    }
                } catch (IOException e) {
//                    LogTool.warn(ErrorType.DETECT_SERVER_ERROR, "get VM type failed: " + e.getMessage(), e);
                }
            }
        }
        return type;
    }

    private static boolean isLinux() {
        String serverName = System.getProperty("os.name");
        return StringUtils.startsWith(serverName, "Linux");
    }

    public static void clear() {
        ApplicationModel.getApplicationInfo().clear();
    }

    /**
     * 获取应用绝对路径
     */
    public static String getWebServerPath() {
        String serverPath = applicationInfo.get(SERVER_PATH);
        if (serverPath != null) {
            return serverPath;
        }
        File file = new File(".");
        String path = file.getAbsolutePath();
        return path.substring(0, path.length() - 2);
    }

    /**
     * 设置agent运行
     */
    public static void start() {
        APPLICATION_IS_RUNNING = true;
    }

    public static void stop() {
        APPLICATION_IS_RUNNING = false;
    }

    public static boolean isRunning() {
        return APPLICATION_IS_RUNNING;
    }

}