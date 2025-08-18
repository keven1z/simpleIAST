package com.keven1z.core.policy;

import com.keven1z.core.utils.CommonUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IAST Hook 管理器，用于管理类和方法级别的Hook配置。
 *
 * @author keven1z
 * @date 2025/6/8
 */
public class IastHookManager {
    private final Map<String, ClassHookConfig> hookRegistry = new ConcurrentHashMap<>();
    private final Map<String, MethodHookConfig> methodCache = new ConcurrentHashMap<>();
    private final Set<String> hookCache = new HashSet<>();
    private final Set<String> hookAncestorsCache = new HashSet<>();

    private IastHookManager() {
    }

    private static final IastHookManager instance = InstanceHolder.INSTANCE;

    public static IastHookManager getManager() {
        return instance;
    }

    public Set<String> getHookCache() {
        return hookCache;
    }

    public Set<String> getHookAncestorsCache() {
        return hookAncestorsCache;
    }

    private static class InstanceHolder {
        public static final IastHookManager INSTANCE = new IastHookManager();
    }

    /**
     * 加载配置信息
     *
     * @param config 配置信息对象
     */
    public void loadConfig(IastHookConfig config) {

        for (ClassHookConfig classConfig : config.getHooks()) {
            String className = classConfig.getClassName().replace('.', '/');
            hookRegistry.put(className, classConfig);

            // 构建方法缓存
            for (MethodHookConfig methodConfig : classConfig.getMethods()) {
                // 仅对启用的方法进行hook处理
                if (methodConfig.isState()) {
                    String methodKey = getMethodKey(className,
                            methodConfig.getName(),
                            methodConfig.getDesc());
                    methodCache.put(methodKey, methodConfig);
                }
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
        if (!shouldHookClass(className)) {
            return false;
        }

        // 检查方法缓存
        String methodKey = getMethodKey(className, method, desc);
        return methodCache.containsKey(methodKey);
    }

    public MethodHookConfig getHookMethod(String className, String method, String desc) {
        // 检查类级别Hook
        if (!shouldHookClass(className)) {
            return null;
        }
        // 检查方法缓存
        String methodKey = getMethodKey(className, method, desc);
        return methodCache.get(methodKey);
    }

    /**
     * 判断是否需要对指定类进行Hook操作
     *
     * @param className 需要判断的类名
     * @return 如果需要对指定类进行Hook操作，则返回true；否则返回false
     */
    public boolean shouldHookClass(String className) {
        if (hookCache.contains(className)) {
            return true;
        }
        ClassHookConfig classConfig = hookRegistry.get(className);
        if (classConfig != null) {
            hookCache.add(className);
            return true;
        }
        return false;
    }

    /**
     * 根据类名获取对应的类hook配置
     *
     * @param className 需要获取类钩子配置的类名
     * @return 对应类名的类hook配置，如果不存在则返回null
     */
    public ClassHookConfig getClassHook(String className) {
        return hookRegistry.get(className);
    }

    /**
     * @param className 待hook的类名
     * @param ancestors 当前类的父类和接口
     * @return 是否应该hook祖先类
     */
    public boolean shouldHookAncestors(String className, Set<String> ancestors) {
        if (className == null || ancestors == null || ancestors.isEmpty()) {
            return false;
        }
        // 如果类名已经在祖先类缓存中存在，则返回true
        if (hookAncestorsCache.contains(className)) {
            return true;
        }

        for (String ancestor : ancestors) {
            ancestor = CommonUtils.toInternalClassName(ancestor);
            // 如果需要hook当前祖先类
            if (shouldHookClass(ancestor)) {
                // 获取祖先类的钩子配置
                ClassHookConfig classHook = getClassHook(ancestor);
                // 如果当前类不在祖先类的排除列表中，则返回true
                if(!classHook.getExcludeClasses().contains(className)){
                    hookAncestorsCache.add(className);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取最后一个匹配的 Hook 祖先类名
     *
     * @param ancestors 祖先类集合
     * @return 最后一个匹配的 Hook 祖先类名，如果没有找到则返回 null
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
     *
     * @param className  类的全限定名(内部格式，如 java/lang/String)，不能为null
     * @param methodName 方法名，不能为null
     * @param descriptor 方法描述符(ASM格式，如 (I)V)，不能为null
     * @return 格式为 "className#methodNameDescriptor" 的唯一键
     */
    private String getMethodKey(String className, String methodName, String descriptor) {
        if (className == null || methodName == null || descriptor == null) {
            return null;
        }
        return new StringBuilder(className.length() + methodName.length() + descriptor.length() + 1)
                .append(className)
                .append('#')
                .append(methodName)
                .append(descriptor)
                .toString();
    }
}
