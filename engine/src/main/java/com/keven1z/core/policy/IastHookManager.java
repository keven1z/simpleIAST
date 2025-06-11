package com.keven1z.core.policy;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IAST Hook 管理器，用于管理类和方法级别的Hook配置。
 * @author keven1z
 * @date 2025/6/8
 */
public class IastHookManager {
    private final Map<String, ClassHookConfig> hookRegistry = new ConcurrentHashMap<>();
    private final Map<String, MethodHookConfig> methodCache = new ConcurrentHashMap<>();
    private IastHookManager() {}
    private final static IastHookManager instance = InstanceHolder.INSTANCE;
    public static IastHookManager getManager() {return instance;}
    private static class InstanceHolder {
        public static final IastHookManager INSTANCE = new IastHookManager();
    }
    public void loadConfig(IastHookConfig config) {

        for (ClassHookConfig classConfig : config.getHooks()) {
            String className = classConfig.getClassName().replace('.', '/');
            hookRegistry.put(className, classConfig);

            // 构建方法缓存
            for (MethodHookConfig methodConfig : classConfig.getMethods()) {
                String methodKey = getMethodKey(className,
                        methodConfig.getName(),
                        methodConfig.getDesc());
                methodCache.put(methodKey, methodConfig);
            }
        }
    }
    /**
     * 判断是否需要Hook
     *
     * @param className 类名
     * @param method    方法名
     * @param desc      方法描述符
     * @return 如果需要Hook则返回true，否则返回false
     */
    public boolean shouldHookMethod(String className, String method, String desc) {
        // 检查类级别Hook
        if (!shouldHookClass(className)) {return false; }

        // 检查方法缓存
        String methodKey = getMethodKey(className, method, desc);
        return methodCache.containsKey(methodKey);
    }
    public MethodHookConfig getHookMethod(String className, String method, String desc) {
        // 检查类级别Hook
        if (!shouldHookClass(className)) {return null; }
        // 检查方法缓存
        String methodKey = getMethodKey(className, method, desc);
        return methodCache.get(methodKey);
    }

    public boolean shouldHookClass(String className) {
        ClassHookConfig classConfig = hookRegistry.get(className);
        return classConfig != null;
    }
    public ClassHookConfig getClassHook(String className) {
        return hookRegistry.get(className);
    }

    /**
     * @param className 待hook的类名
     * @param ancestors 当前类的父类和接口
     * @return 是否应该hook祖先类
     */
    public boolean shouldHookAncestors(String className, Set<String> ancestors) {
        if (ancestors == null || ancestors.isEmpty()) {
            return false;
        }
        for (String ancestor : ancestors) {
            if (shouldHookClass(ancestor)) {
                ClassHookConfig classHook = getClassHook(ancestor);
                if (classHook.getExcludeClasses().contains(className)){
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 获取最后一个匹配的 Hook 祖先类名
     */
    public String getMatchingAncestor(Set<String> ancestors) {
        if (ancestors == null || ancestors.isEmpty()) {
            return null;
        }
        for (String ancestor : ancestors) {
            if (shouldHookClass(ancestor)) {
                return ancestor;
            }
        }
        return null;
    }

    /**
     * 生成方法的唯一标识键
     * @param className 类的全限定名(内部格式，如 java/lang/String)
     * @param methodName 方法名
     * @param descriptor 方法描述符(ASM格式)
     * @return 格式为 "className#methodNameDescriptor" 的唯一键
     */
    private String getMethodKey(String className, String methodName, String descriptor) {
        return className + "#" + methodName + descriptor;
    }
}
