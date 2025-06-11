package com.keven1z.core.policy;

import com.keven1z.core.utils.HookPolicyDeserializer;
import com.keven1z.core.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.keven1z.core.utils.FileUtils.readJsonFile;

/**
 * 类 Hook 配置
 * @author keven1z
 * @date 2025/6/8
 */
public class ClassHookConfig {
    /**
     * 需要 Hook 的类名
     */
    private String className;
    /**
     * 需要 Hook 的方法列表
     */
    private List<MethodHookConfig> methods;
    /**
     * 是否是接口类
     */
    private boolean isInterface;
    private List<String> excludeClasses;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MethodHookConfig> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodHookConfig> methods) {
        this.methods = methods;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public List<String> getExcludeClasses() {
        return excludeClasses;
    }

    public void setExcludeClasses(List<String> excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    @Override
    public String toString() {
        return "HookConfig{" +
                "className='" + className + '\'' +
                ", methods=" + methods +
                '}';
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = ClassHookConfig.class.getClassLoader().getResourceAsStream("policy.json");
        String jsonFile = readJsonFile(inputStream);
        IastHookConfig iastHookConfig = JsonUtils.toObject(jsonFile, IastHookConfig.class, new HookPolicyDeserializer());
        System.out.println(iastHookConfig);
    }
}
