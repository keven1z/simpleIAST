package com.keven1z.core.utils;


import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.consts.PolicyConst;
import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintGraph;
import com.keven1z.core.model.taint.PathNode;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.HookPolicy;
import com.keven1z.core.policy.HookPolicyContainer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.keven1z.core.consts.CommonConst.OFF;

public class PolicyUtils {
    private final static ConcurrentHashMap<String, Boolean> superClassCache = new ConcurrentHashMap<>(1024);

    /**
     * 判断是否是hook点或者是hook接口的实现类
     *
     * @param className       待判断的类名
     * @param hookPolicyContainer 策略集
     * @param interfaces      待判断类的单一接口
     * @param loader          classloader
     * @return 是否是hook点或者是hook接口的实现类
     */
    public static boolean isHook(String className, HookPolicyContainer hookPolicyContainer, String[] interfaces, String superClass, ClassLoader loader) throws IOException {
        if (PolicyUtils.isHookedByClassName(className, hookPolicyContainer, true)) {
            return true;
        }
        if (superClassCache.containsKey(className)) {
            return superClassCache.get(className);
        }

        Set<String> allInterfaces = getAllInterfaces(interfaces, superClass, loader);
        boolean isHooked = PolicyUtils.isHookedByAncestors(className, allInterfaces, hookPolicyContainer);
        superClassCache.put(className, isHooked);
        return isHooked;

    }

    public static Set<String> getAllInterfaces(String[] interfaces, String superClass, ClassLoader loader) throws IOException {
        Set<String> ancestors = ClassUtils.buildAncestors(interfaces, superClass);
        return ClassUtils.getAncestors(ancestors, loader, 0);
    }

    public static boolean isHook(HookPolicyContainer hookPolicyContainer, Class<?> clazz) {
        String className = clazz.getName();
        className = className.replace(".", "/");
        if (PolicyUtils.isHookedByClassName(className, hookPolicyContainer, false)) {
            return true;
        }
        if (superClassCache.containsKey(className)) {
            return superClassCache.get(className);
        }
        Set<String> allInterfaces = ClassUtils.getAllInterfaces(clazz);
        boolean isHooked = PolicyUtils.isHookedByAncestors(className, allInterfaces, hookPolicyContainer);
        superClassCache.put(className, isHooked);
        return isHooked;
    }

    /**
     * 判断className是否是hook点
     *
     * @param className 类名(.分割)
     * @return 是否在hook点中。
     */
    private static boolean isHookedByClassName(String className, HookPolicyContainer hookPolicyContainer, boolean isSetHooked) {
        List<HookPolicy> https = hookPolicyContainer.getHttp();
        if (isHookedByClassName(className, https, isSetHooked)) {
            return true;
        }
        List<HookPolicy> sources = hookPolicyContainer.getSource();
        if (isHookedByClassName(className, sources, isSetHooked)) {
            return true;
        }
        List<HookPolicy> sanitizers = hookPolicyContainer.getSanitizers();
        if (isHookedByClassName(className, sanitizers, isSetHooked)) {
            return true;
        }
        List<HookPolicy> sinks = hookPolicyContainer.getSink();
        if (isHookedByClassName(className, sinks, isSetHooked)) {
            return true;
        }
        List<HookPolicy> propagations = hookPolicyContainer.getPropagation();

        return isHookedByClassName(className, propagations, isSetHooked);
    }

    public static boolean isHookedByClassName(String className, List<HookPolicy> policies, boolean isSetHooked) {
        for (HookPolicy hookPolicy : policies) {
            //如果为接口hook类或者已经hook，不进行hook
            if (hookPolicy.getState() == OFF || hookPolicy.getInter() || hookPolicy.isHooked()) {
                continue;
            }
            String name = hookPolicy.getClassName();
            if (name.equals(className) && hookPolicy.getState() == CommonConst.ON) {
                if (isSetHooked) {
                    hookPolicy.setHooked(true);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在于hook策略集合中
     */
    public static boolean isExistInPolicy(String className, List<HookPolicy> policies) {
        for (HookPolicy hookPolicy : policies) {
            String name = hookPolicy.getClassName();
            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断class的接口或者父类是否是hook点
     *
     * @param className 类名(.分割)
     * @return 是否在hook点中。
     */
    public static boolean isHookedByAncestors(String className, Set<String> ancestors, HookPolicyContainer hookPolicyContainer) {
        if (ancestors.isEmpty()) {
            return false;
        }
        List<HookPolicy> interfaceHookPolicy = hookPolicyContainer.getInterfacePolicy();
        return isHookedByAncestors(className, ancestors, interfaceHookPolicy, hookPolicyContainer);
    }

    private static boolean isHookedByAncestors(String className, Set<String> ancestors, List<HookPolicy> policies, HookPolicyContainer hookPolicyContainer) {
        boolean isHit = false;
        for (HookPolicy hookPolicy : policies) {
            String name = hookPolicy.getClassName();
            for (String ancestor : ancestors) {
                if (hookPolicy.getState() == OFF) {
                    continue;
                }
                //由于传进来的className以.分割，策略为/分割，需要进行转化
                ancestor = ancestor.replace(".", "/");
                //className匹配和黑名单匹配
                if (!name.equals(ancestor) || CommonUtils.isEndWithElementInList(className, hookPolicy.getExclude())) {
                    continue;
                }

                HookPolicy childHookPolicy = createPolicy(className, hookPolicy, ancestor);

                hookPolicyContainer.addPolicy(childHookPolicy);
                isHit = true;
            }
        }
        return isHit;
    }

    private static HookPolicy createPolicy(String className, HookPolicy hookPolicy, String ancestor) {
        return new HookPolicy.Builder().conditions(hookPolicy.getConditions())
                .className(className)
                .method(hookPolicy.getMethod())
                .desc(hookPolicy.getDesc())
                .enter(hookPolicy.getEnter())
                .exit(hookPolicy.getExit())
                .from(hookPolicy.getFrom())
                .to(hookPolicy.getTo())
                .name(hookPolicy.getName())
                .type(hookPolicy.getType())
                .state(hookPolicy.getState())
                .inter(false)
                .isHooked(true)
                .originClassName(ancestor)
                .build();
    }

    /**
     * @param className 类名
     * @param method    方法名
     * @param desc      方法描述
     * @return hook点的策略信息
     */
    public static HookPolicy getHookedPolicy(String className, String method, String desc, List<HookPolicy> policies) {

        for (HookPolicy p : policies) {
            if (p.getState() == OFF) {
                continue;
            }
            String name = p.getClassName();
            String m = p.getMethod();
            String d = p.getDesc();
            if (name.equals(className) && m.equals(method) && d.equals(desc)) {
                return p;
            }
        }
        return null;
    }
    /**
     *
     */
    public static HookPolicy getHookedPolicyByBisection(String className, String method, String desc, List<HookPolicy> policies) {
        HookPolicy target =HookPolicy.builder()
                .className(className)
                .method(method)
                .desc(desc)
                .build();
        Collections.sort(policies);
        // Perform binary search
        int index = Collections.binarySearch(policies, target);

        if (index >= 0) {
            HookPolicy foundHookPolicy = policies.get(index);
            if (foundHookPolicy.getState() != OFF) {
                return foundHookPolicy;
            }
        }
        return null;
    }
    /**
     * 获取对应from、to、conditions的对象
     *
     * @param position     from或to 路径表示字符串，如P1，R，O
     * @param parameters   hook类参数
     * @param returnObject hook类返回对象
     * @param thisObject   hook类当前对象
     * @return 对应的对象List
     */
    public static List<FlowObject> getFromPositionObject(String position, Object[] parameters, Object returnObject, Object thisObject) {
        if (position == null) {
            return null;
        }
        ArrayList<FlowObject> flowObjects = new ArrayList<>();
        String[] paths = position.split(PolicyConst.PATH_SPLIT_SEPARATOR);
        for (String path : paths) {
            Object pathObject = getPathObject(path, parameters, returnObject, thisObject);
            if (pathObject != null) {
                flowObjects.add(new FlowObject(path, pathObject));
            }
        }
        return flowObjects;
    }
    public static FlowObject getSourceAndSinkFromPositionObject(String position, Object[] parameters, Object returnObject, Object thisObject) {
        List<FlowObject> fromPositionObjects = getFromPositionObject(position, parameters, returnObject, thisObject);
        if (fromPositionObjects.isEmpty()) {
            return null;
        }
        return fromPositionObjects.get(0);
    }

    public static Object getToPositionObject(String position, Object[] parameters, Object returnObject, Object thisObject) {
        if (position == null) {
            return null;
        }
        return getPathObject(position, parameters, returnObject, thisObject);
    }

    private static Object getPathObject(String path, Object[] parameters, Object returnObject, Object thisObject) {
        if (PolicyConst.R.equals(path)) {
            return returnObject;
        } else if (PolicyConst.O.equals(path)) {
            return thisObject;
        } else if (path.startsWith(PolicyConst.P)) {
            int index = Integer.parseInt(path.substring(path.indexOf(PolicyConst.P) + 1));
            //策略按照1，2，3表示顺序，list按照0,1,2
            Object parameter = parameters[index - 1];
            if (parameter != null) {
                return parameters[index - 1];
            }
        } else {
            LogTool.warn(ErrorType.POLICY_ERROR, "Failed to find position,path is " + path);
            return null;
        }
        return null;
    }

    /**
     * 查找来源的节点,来源不仅仅是source阶段的节点
     */
    public static PathNode searchParentNode(Object fromObject, TaintGraph taintGraph) {
        int fromObjectHashCode = System.identityHashCode(fromObject);
        if (taintGraph.isTaint(fromObjectHashCode)) {
            return null;
        }
        List<PathNode> allNode = taintGraph.getAllNode();
        //倒序查询，符合代码执行的流程
        for (int i = allNode.size() - 1; i >= 0; i--) {
            /*
             * 是否为污点对象，eg: fromObject= "sql",传播方法为StringBuilder.toString,污点传出方向为返回值，返回值为sql，则判定为污点
             */
            PathNode node = allNode.get(i);
            if (node.isToObject(fromObjectHashCode)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 查找来源的节点,来源不仅仅是source阶段的节点
     */
    public static Set<PathNode> searchParentNodes(Object fromObject, TaintGraph taintGraph) {
        int fromObjectHashCode = System.identityHashCode(fromObject);
        if (taintGraph.isTaint(fromObjectHashCode)) {
            return null;
        }
        List<PathNode> allNode = taintGraph.getAllNode();
        Set<PathNode> parentNodes = new LinkedHashSet<>();
        //倒序查询，符合代码执行的流程
        for (int i = allNode.size() - 1; i >= 0; i--) {
            PathNode PathNode = allNode.get(i);
            /*
             * 是否为污点对象，eg: fromObject= "sql",传播方法为StringBuilder.toString,污点传出方向为返回值，返回值为sql，则判定为污点
             */
            if (PathNode.isToObject(fromObjectHashCode)) {
                parentNodes.add(PathNode);
            }
        }
        return parentNodes;
    }
}
