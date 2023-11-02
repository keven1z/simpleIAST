package com.keven1z.core.utils;

import com.keven1z.core.Config;
import com.keven1z.core.EngineBoot;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyContainer;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.JsonUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 负责资源文件加载
 */
public class FileUtils {

    /**
     * 从policy.json加载hook策略
     *
     * @return {@link PolicyContainer}
     */
    public static PolicyContainer load(ClassLoader classLoader) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(Config.POLICY_FILE_PATH);
            String jsonFile = readJsonFile(inputStream);
            if (jsonFile == null) {
                return null;
            }

            PolicyContainer policyContainer = JsonUtils.toObject(jsonFile, PolicyContainer.class);

            if (policyContainer == null) {
                return null;
            }

            List<Policy> sources = policyContainer.getSource();
            List<Policy> interfacePolicy = policyContainer.getInterfacePolicy();
            for (Policy source : sources) {
                if (source.getInter()) {
                    interfacePolicy.add(source);
                }
                source.setType(PolicyTypeEnum.SOURCE);
            }
            List<Policy> propagations = policyContainer.getPropagation();
            for (Policy propagation : propagations) {
                if (propagation.getInter()) {
                    interfacePolicy.add(propagation);
                }
                propagation.setType(PolicyTypeEnum.PROPAGATION);
            }
            List<Policy> sinks = policyContainer.getSink();
            for (Policy sink : sinks) {
                if (sink.getInter()) {
                    interfacePolicy.add(sink);
                }
                sink.setType(PolicyTypeEnum.SINK);
            }
            List<Policy> https = policyContainer.getHttp();
            for (Policy http : https) {
                if (http.getInter()) {
                    interfacePolicy.add(http);
                }
                http.setType(PolicyTypeEnum.HTTP);
            }
            List<Policy> sanitizers = policyContainer.getSanitizers();
            for (Policy sanitizer : sanitizers) {
                if (sanitizer.getInter()) {
                    interfacePolicy.add(sanitizer);
                }
                sanitizer.setType(PolicyTypeEnum.SANITIZER);
            }
            List<Policy> singles = policyContainer.getSingles();
            for (Policy single : singles) {
                if (single.getInter()) {
                    interfacePolicy.add(single);
                }
                single.setType(PolicyTypeEnum.SINGLE);
            }

            return policyContainer;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * @return 加载黑名单文件
     */
    public static List<String> loadBlackList(ClassLoader classLoader) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(Config.BLACK_LIST_FILE_PATH))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\n", "");
                if (!line.isEmpty()) {
                    arrayList.add(line);
                }
            }
        }
        return arrayList;
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
