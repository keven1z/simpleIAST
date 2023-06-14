package com.keven1z.core.policy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keven1z.core.Config;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.utils.FileUtils;
import com.keven1z.core.utils.JsonUtils;
import com.keven1z.core.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 负责上下文中的策略，模式等提取、加载
 */
public class ContextLoader {

    /**
     * 从policy.json加载hook策略
     *
     * @return {@link PolicyContainer}
     */
    public static PolicyContainer load(ClassLoader classLoader) throws JsonProcessingException {
        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(Config.POLICY_FILE_PATH);
            String jsonFile = FileUtils.readJsonFile(inputStream);
            if (jsonFile == null) {
                return null;
            }

            PolicyContainer policyContainer = JsonUtils.toObject(jsonFile, PolicyContainer.class);

            if (policyContainer == null) {
                return null;
            }
            List<Policy> sources = policyContainer.getSource();
            for (Policy source : sources) {
                source.setType(PolicyTypeEnum.SOURCE);
            }
            List<Policy> propagations = policyContainer.getPropagation();
            for (Policy propagation : propagations) {
                propagation.setType(PolicyTypeEnum.PROPAGATION);
            }
            List<Policy> sinks = policyContainer.getSink();
            for (Policy sink : sinks) {
                sink.setType(PolicyTypeEnum.SINK);
            }
            List<Policy> https = policyContainer.getHttp();
            for (Policy http : https) {
                http.setType(PolicyTypeEnum.HTTP);
            }
            List<Policy> sanitizers = policyContainer.getSanitizers();
            for (Policy sanitizer : sanitizers) {
                sanitizer.setType(PolicyTypeEnum.SANITIZER);
            }

            if (LogTool.isDebugEnabled()) {
                Logger.getLogger(ContextLoader.class.getPackage().getName()).info(JsonUtils.toString(policyContainer.getAllPolicies()));
            }
            return policyContainer;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    /**
     * hook后获取策略
     *
     * @return {@link PolicyContainer}
     */
    @SuppressWarnings("unuse")
    public static PolicyContainer getPolicies() {

        try {
            Object context = getContext();
            Object policyContainerObject = ReflectionUtils.invokeMethod(context, "getPolicyContainer", new Class[]{});
            if (policyContainerObject == null) {
                return null;
            }
            return (PolicyContainer) policyContainerObject;
        } catch (Exception e) {
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to get the policy when hook", e);
            throw new RuntimeException(e);
        }
    }

    private static Object getContext() {
        try {
            Class<?> loadClass = ClassLoader.getSystemClassLoader().loadClass("com.keven1z.ModuleLoader");
            Field field = loadClass.getField("classLoader");
            Object classloader = field.get(loadClass);
            Class<?> engineBoot = (Class<?>) ReflectionUtils.invokeMethod(classloader, "loadClass", new Class[]{String.class}, "com.keven1z.core.EngineLoader");

            Field contextFiled = engineBoot.getField("context");

            return contextFiled.get(engineBoot);
        } catch (Exception e) {
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to get the Context when hook", e);
            return null;
        }
    }

    public static Instrumentation getInst() {

        try {
            Object context = getContext();
            Object inst = ReflectionUtils.invokeMethod(context, "getInstrumentation", new Class[]{});
            if (inst == null) {
                return null;
            }
            return (Instrumentation) inst;
        } catch (Exception e) {
            LogTool.error(ErrorType.HOOK_ERROR, "Failed to get the Instrumentation when hook", e);
            throw new RuntimeException(e);
        }
    }

}
