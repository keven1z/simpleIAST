package com.keven1z.core.utils;

import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.consts.PolicyConst;
import com.keven1z.core.graph.taint.TaintGraph;
import com.keven1z.core.graph.taint.TaintNode;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyContainer;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.*;

public class PolicyUtils {

    /**
     * 判断是否是hook点或者是hook接口的实现类
     *
     * @param className       待判断的类名
     * @param policyContainer 策略集
     * @param interfaces      待判断类的单一接口
     * @param loader          classloader
     * @return 是否是hook点或者是hook接口的实现类
     */
    public static boolean isHook(String className, PolicyContainer policyContainer, String[] interfaces, String superClass, ClassLoader loader) throws IOException {
        if (PolicyUtils.isHookedByClassName(className, policyContainer)) {
            return true;
        }
        Set<String> ancestors = ClassUtils.buildAncestors(interfaces, superClass);
        Set<String> allInterfaces = ClassUtils.getAllInterfaces(ancestors, loader);
        return PolicyUtils.isHookedByAncestors(className, allInterfaces, policyContainer);
    }

    public static boolean isHook(PolicyContainer policyContainer, Class<?> clazz) {
        String className = clazz.getName();
        className = className.replace(".", "/");
        if (PolicyUtils.isHookedByClassName(className, policyContainer)) {
            return true;
        }
        Set<String> allInterfaces = ClassUtils.getAllInterfaces(clazz);
        return PolicyUtils.isHookedByAncestors(className, allInterfaces, policyContainer);
    }

    /**
     * 判断className是否是hook点
     *
     * @param className 类名(.分割)
     * @return 是否在hook点中。
     */
    private static boolean isHookedByClassName(String className, PolicyContainer policyContainer) {

        List<Policy> policies = policyContainer.getAllPolicies();
        for (Policy hookPolicy : policies) {
            //如果为接口hook类或者已经hook，不进行hook
            if (hookPolicy.getInter() || hookPolicy.isHooked()) {
                continue;
            }
            String name = hookPolicy.getClassName();
            if (name.equals(className) && hookPolicy.getState() == CommonConst.ON) {
                hookPolicy.setHooked(true);
                return true;
            }
        }
        return false;
    }

    /**
     * 判断className是否是hook点
     *
     * @param className 类名(.分割)
     * @return 是否在hook点中。
     */
    public static boolean isHookedByAncestors(String className, Set<String> ancestors, PolicyContainer policyContainer) {
        List<Policy> policies = policyContainer.getAllPolicies();
        for (Policy hookPolicy : policies) {
            //如果不是接口，则不允许作为父类的比较
            if (!hookPolicy.getInter()) {
                continue;
            }

            String name = hookPolicy.getClassName();
            for (String ancestor : ancestors) {
                //由于传进来的className以.分割，策略为/分割，需要进行转化
                ancestor = ancestor.replace(".", "/");
                //className匹配和黑名单匹配
                if (!name.equals(ancestor) || StringUtils.isEndWithElementInList(className, hookPolicy.getExclude())) {
                    continue;
                }

                Policy clonePolicy = SerializationUtils.clone(hookPolicy);
                clonePolicy.setClassName(className);
                clonePolicy.setState(CommonConst.OFF);
                clonePolicy.setInter(false);
                clonePolicy.setHooked(true);
                policyContainer.addPolicy(clonePolicy);
                return true;

            }
        }
        return false;
    }


    /**
     * @param className 类名
     * @param method    方法名
     * @param desc      方法描述
     * @return hook点的策略信息
     */
    public static Policy getHookedPolicy(String className, String method, String desc, PolicyContainer policyContainer) {
        if (policyContainer == null) {
            throw new RuntimeException("Policy is null");
        }
        return getHookedPolicy(className, method, desc, policyContainer.getAllPolicies());
    }

    public static Policy getHookedPolicy(String className, String method, String desc, List<Policy> policies) {
        if (policies == null || policies.isEmpty()) {
            throw new RuntimeException("Policy is null");
        }
        for (Policy p : policies) {
            String name = p.getClassName();
            String m = p.getMethod();
            String d = p.getDesc();
            if (name.equals(className) && m.equals(method) && d.equals(desc)) {
                return p;
            }
        }
        return null;
    }

//    public static Policy getHookedPolicy(String method, String desc, List<Policy> policies) {
//        if (policies == null || policies.isEmpty()) {
//            throw new RuntimeException("Policy is null");
//        }
//        for (Policy p : policies) {
//            String m = p.getMethod();
//            String d = p.getDesc();
//            if (m.equals(method) && d.equals(desc)) {
//                return p;
//            }
//        }
//        return null;
//    }

    /**
     * 获取对应from、to、conditions的对象
     *
     * @param position     from或to 路径表示字符串，如P1，R，O
     * @param parameters   hook类参数
     * @param returnObject hook类返回对象
     * @param thisObject   hook类当前对象
     * @return 对应的对象List
     */
    public static List<Object> getPositionObject(String position, Object[] parameters, Object returnObject, Object thisObject) {
        ArrayList<Object> list = new ArrayList<>();
        if (position == null) {
            return list;
        }
        String[] paths = position.split(PolicyConst.PATH_SPLIT_SEPARATOR);
        for (String path : paths) {
            if (PolicyConst.R.equals(path)) {
                if (returnObject != null) {
                    list.add(returnObject);
                }
            } else if (PolicyConst.O.equals(path)) {
                if (thisObject != null) {
                    list.add(thisObject);
                }
            } else if (path.startsWith(PolicyConst.P)) {
                int index = Integer.parseInt(path.substring(path.indexOf(PolicyConst.P) + 1));
                //策略按照1，2，3表示顺序，list按照0,1,2
                list.add(parameters[index - 1]);
            } else {
                LogTool.warn(ErrorType.POLICY_ERROR, "Failed to find position,path is " + path);

            }
        }
        return list;
    }

    /**
     * 查找来源的节点,来源不仅仅是source阶段的节点
     */
    public static TaintNode searchFromNode(Object fromObject, TaintGraph taintGraph) {
        Set<TaintNode> allNode = taintGraph.getAllNode();
        ArrayList<TaintNode> taintNodes = new ArrayList<>(allNode);
        int fromObjectHashCode = System.identityHashCode(fromObject);
        for (int i = allNode.size() - 1; i >= 0; i--) {//一般是在后面，所以需要倒序，fromObject提前计算
            TaintNode node = taintNodes.get(i);
            /*
             * 是否为污点对象，eg: fromObject= "sql",传播方法为StringBuilder.toString,污点传出方向为返回值，返回值为sql，则判定为污点
             */
            if (node.isToObject(fromObjectHashCode)) {
                return node;
            }
        }
        return null;
    }
}
