package com.keven1z.core.utils;

import com.keven1z.core.model.Config;
import com.keven1z.core.EngineBoot;
import com.keven1z.core.policy.HookPolicy;
import com.keven1z.core.policy.HookPolicyContainer;
import com.keven1z.core.consts.PolicyType;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 资源文件加载工具类
 */
public class FileUtils {

    /**
     * 从policy.json加载hook策略
     *
     * @return {@link HookPolicyContainer}
     */
    public static HookPolicyContainer load(ClassLoader classLoader) throws IOException {
        try (InputStream inputStream = classLoader.getResourceAsStream(Config.POLICY_FILE_PATH)) {
            String jsonFile = readJsonFile(inputStream);
            if (jsonFile == null) {
                return null;
            }

            HookPolicyContainer hookPolicyContainer = JsonUtils.toObject(jsonFile, HookPolicyContainer.class);
            if (hookPolicyContainer == null) {
                return null;
            }

            List<HookPolicy> interfaceHookPolicy = hookPolicyContainer.getInterfacePolicy();

            processPolicyList(hookPolicyContainer.getSource(), interfaceHookPolicy, PolicyType.SOURCE);
            processPolicyList(hookPolicyContainer.getPropagation(), interfaceHookPolicy, PolicyType.PROPAGATION);
            processPolicyList(hookPolicyContainer.getSink(), interfaceHookPolicy, PolicyType.SINK);
            processPolicyList(hookPolicyContainer.getHttp(), interfaceHookPolicy, PolicyType.HTTP);
            processPolicyList(hookPolicyContainer.getSanitizers(), interfaceHookPolicy, PolicyType.SANITIZER);
            processPolicyList(hookPolicyContainer.getSingles(), interfaceHookPolicy, PolicyType.SINGLE);

            return hookPolicyContainer;
        }
    }
    private static void processPolicyList(List<HookPolicy> policies, List<HookPolicy> interfacePolicies, PolicyType type) {
        policies.forEach(policy -> {
            if (policy.getInter()) {
                interfacePolicies.add(policy);
            }
            policy.setType(type);
        });
    }
    /**
     * @return 加载黑名单文件
     */
    public static Set<String> loadBlackList(ClassLoader classLoader) throws IOException {
        Set<String> blackSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(Config.BLACK_LIST_FILE_PATH))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\n", "");
                if (!line.isEmpty()) {
                    blackSet.add(line);
                }
            }
        }
        return blackSet;
    }

    public static String loadIASTProperties(ClassLoader classLoader, String key, String defaultValue){
        Properties properties = new Properties();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(Config.IAST_PROPERTIES_FILE_PATH))))) {
            properties.load(reader);
            return properties.getProperty(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<String> weakPasswordList;

    public static List<String> loadWeakPassword(ClassLoader classLoader) throws IOException {
        if (weakPasswordList != null) {
            return weakPasswordList;
        }
        weakPasswordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(Config.WEAK_PASSWORD_FILE_PATH))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\n", "");
                if (!line.isEmpty()) {
                    weakPasswordList.add(line);
                }
            }
        }
        return weakPasswordList;
    }

    public static String readJsonFile(InputStream inputStream) throws IOException {
        if (inputStream == null) return null;
        BufferedReader bufferedReader = null;
        Reader reader = null;
        try {

            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            bufferedReader = new BufferedReader(reader);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line.replace("\r", "").replace("\n", ""));
            }
            return sb.toString();
        } finally {
            inputStream.close();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (reader != null) {
                reader.close();
            }

        }
    }

    /**
     * 获取当前jar包所在的文件夹路径
     *
     * @return jar包所在文件夹路径
     */
    public static String getBaseDir() {
        String baseDir;
        String jarPath = EngineBoot.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            baseDir = URLDecoder.decode(new File(jarPath).getParent(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            baseDir = new File(jarPath).getParent();
        }
        return baseDir;
    }
}
