package com.keven1z.core.taint.resolvers;

import com.keven1z.core.EngineController;
import com.keven1z.core.consts.PolicyConst;
import com.keven1z.core.model.graph.TaintData;
import com.keven1z.core.model.graph.TaintGraph;
import com.keven1z.core.model.graph.TaintNode;
import com.keven1z.core.taint.TaintSpy;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.policy.Policy;
import com.keven1z.core.policy.PolicyTypeEnum;
import com.keven1z.core.utils.*;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.keven1z.core.consts.CommonConst.OFF;
import static com.keven1z.core.consts.CommonConst.ON;
import static com.keven1z.core.consts.PolicyConst.SOURCE_BEAN;
import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;
import static com.keven1z.core.utils.PolicyUtils.getToPositionObject;

/**
 * 污染源的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class SourceClassResolver implements HandlerHookClassResolver {
    private static final String[] USER_PACKAGE_PREFIX = new String[]{"java", "javax", " org.spring".substring(1), " org.apache".substring(1), " io.undertow".substring(1)};

    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, String from, String to) {
        if (returnObject == null || returnObject.equals("")) {
            return;
        }

        Map<String, Object> fromMap = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromMap == null || fromMap.isEmpty()) {
            return;
        }

        Object toObject = getToPositionObject(to, parameters, returnObject, thisObject);
        if (toObject == null) {
            return;
        }

        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        TaintData taintData = null;
        if (SOURCE_BEAN.equals(policyName)) {
            for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
                Object fromObject = entry.getValue();
                TaintNode parentNode = PolicyUtils.searchParentNode(fromObject, taintGraph);
                if (parentNode == null) {
                    continue;
                }
                if (taintData == null) {
                    taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);
                }
                taintGraph.addEdge(parentNode.getTaintData(), taintData, entry.getKey());
            }
            if (taintData != null) {
                TaintUtils.buildTaint(returnObject, taintData, toObject, true);
            }
            return;
        }

        taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);
        taintData.setToType(String.class.getTypeName());
        taintData.setFromValue(getSourceFromName(fromMap));
        TaintUtils.buildTaint(returnObject, taintData, toObject, true);


        //如果污染源为用户对象，则判断为bean对象，将其对象所有get方法加入hook策略
        if (!StringUtils.isStartsWithElementInArray(toObject.getClass().getName(), USER_PACKAGE_PREFIX)) {
            addBeanObjectPolicy(toObject.getClass());
        }

    }

    /**
     * 获取污染源的入参名，eg：id=1，入参名为id
     */
    private String getSourceFromName(Map<String, Object> fromMap) {
        for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
            Object fromObject = entry.getValue();
            if ("org.springframework.web.method.HandlerMethod$HandlerMethodParameter".equals(fromObject.getClass().getName())) {
                return ReflectionUtils.invokeStringMethod(fromObject, "getParameterName", new Class[]{});
            } else {
                return fromObject.toString();
            }
        }
        return null;
    }

    /**
     * 将污染对象的所有get方法作为污染源
     *
     * @param taintClass 污染对象类
     */
    private void addBeanObjectPolicy(Class<?> taintClass) {
        List<Method> toBeTransformedMethods = getMethodToBeTransformed(taintClass);
        if (toBeTransformedMethods.isEmpty()) {
            return;
        }

        Instrumentation inst = EngineController.context.getInstrumentation();
        if (inst == null) {
            return;
        }
        Class<?>[] loadedClasses = inst.getAllLoadedClasses();
        for (Method method : toBeTransformedMethods) {
            String name = method.getName();
            String taintClassName = method.getDeclaringClass().getName();
            Policy policy = new Policy(taintClassName.replace(".", "/"), name, ClassUtils.classToSmali(method.getReturnType()));
            policy.setFrom(PolicyConst.O);
            policy.setTo(PolicyConst.R);
            policy.setState(ON);
            policy.setEnter(OFF);
            policy.setExit(ON);
            policy.setName(SOURCE_BEAN);
            policy.setType(PolicyTypeEnum.SOURCE);
            TaintSpy.getInstance().getPolicyContainer().addPolicy(policy);
            reTransform(taintClassName, inst, loadedClasses);
        }

    }

    public List<Method> getMethodToBeTransformed(Class<?> taintClass) {
        Method[] methods = taintClass.getMethods();
        List<Method> toBeTransformedMethods = new ArrayList<>();
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isNative(modifiers)) {
                continue;
            }

            String taintClassName = method.getDeclaringClass().getName();
            String name = method.getName();
            if (name.startsWith("get")) {
                if (ClassUtils.classIsInteger(method.getReturnType())) {
                    continue;
                }

                if (StringUtils.isStartsWithElementInArray(taintClassName, USER_PACKAGE_PREFIX)) {
                    continue;
                }

                if (!PolicyUtils.isExistInPolicy(taintClassName.replace(".", "/"), TaintSpy.getInstance().getPolicyContainer().getSource())) {
                    toBeTransformedMethods.add(method);
                }
            }
        }
        return toBeTransformedMethods;
    }

    private void reTransform(String transformClassName, Instrumentation inst, Class<?>[] loadedClasses) {
        for (Class<?> clazz : loadedClasses) {
            if (transformClassName.equals(clazz.getName())) {
                try {
                    inst.retransformClasses(clazz);
                } catch (UnmodifiableClassException e) {
                    LogTool.error(ErrorType.TRANSFORM_ERROR, "In the source stage,transform " + transformClassName + " error");
                }
            }
        }
    }
}
