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
import com.keven1z.core.utils.ClassUtils;
import com.keven1z.core.utils.PolicyUtils;
import com.keven1z.core.utils.StackUtils;
import com.keven1z.core.utils.StringUtils;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


import static com.keven1z.core.consts.CommonConst.OFF;
import static com.keven1z.core.consts.CommonConst.ON;
import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;
import static com.keven1z.core.utils.PolicyUtils.getPositionObject;

/**
 * 污染源的解析器
 *
 * @author keven1z
 * @date 2023/01/15
 */
public class SourceClassResolver implements HandlerHookClassResolver {
    private static final String[] USER_PACKAGE_PREFIX = new String[]{"java", "javax", " org.spring".substring(1), " org.apache".substring(1), " io.undertow".substring(1)};

    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName) {
        if (returnObject == null || returnObject.equals("")) {
            return;
        }

        Policy policy = PolicyUtils.getHookedPolicy(className, method, desc, TaintSpy.policyContainer.getSource());
        if (policy == null) {
            if (LogTool.isDebugEnabled()) {
                LogTool.warn(ErrorType.POLICY_ERROR, "Can't match the policy,className:" + className + ",method:" + method + ",desc:" + desc);
            }
            return;
        }


        String to = policy.getTo();
        List<Object> taintObjects = getPositionObject(to, parameters, returnObject, thisObject);
        if (taintObjects.isEmpty()) {
            return;
        }
        String from = policy.getFrom();
        List<Object> fromList = PolicyUtils.getPositionObject(from, parameters, returnObject, thisObject);
        if (fromList.isEmpty()) {
            return;
        }
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();

        if (policy.isBeanHook()){
            for (Object fromObject : fromList) {
                TaintNode taintNode = PolicyUtils.searchFromNode(fromObject, taintGraph);
                if (taintNode == null) {
                    continue;
                }
                TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);

                List<Object> toList = PolicyUtils.getPositionObject(policy.getTo(), parameters, returnObject, thisObject);
                if (toList.isEmpty()) {
                    return;
                }
                Object toObject = toList.get(0);
                taintData.setToObjectHashCode(System.identityHashCode(toObject));

                if (toObject instanceof String) {
                    taintData.setToValue(toObject.toString());
                }
                taintData.setFromValue(fromObject.toString());
                taintData.setFromObjectHashCode(System.identityHashCode(fromObject));
                taintData.setStackList(StackUtils.getStackTraceArray(true, true));
                taintData.setToValue(toObject.toString());
                taintData.setTaintValueType(toObject.getClass().getTypeName());
                taintGraph.addNode(taintData);
                taintGraph.addEdge(taintNode.getTaintData(), taintData);
            }
            return;
        }
        //Source的to只允许一个
        Object taintObject = taintObjects.get(0);
        TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);
        taintData.setStackList(StackUtils.getStackTraceArray(true, true));
        taintData.setToObjectHashCode(System.identityHashCode(taintObject));
        taintData.setToValue(taintObject.toString());
        taintData.setFromValue(fromList.get(0).toString());
        taintData.setTaintValueType(taintObject.getClass().getTypeName());
        taintGraph.addNode(taintData);

        //如果污染源为用户对象，则判断为bean对象，将其对象所有get方法加入hook策略
        if (!StringUtils.isStartsWithElementInArray(taintObject.getClass().getName(), USER_PACKAGE_PREFIX)) {
            addBeanObjectPolicy(taintObject.getClass());
        }

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
            policy.setBeanHook(true);
            policy.setType(PolicyTypeEnum.SOURCE);
            TaintSpy.policyContainer.addPolicy(policy);
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

                if (!PolicyUtils.isExistInPolicy(taintClassName.replace(".", "/"), TaintSpy.policyContainer.getSource())) {
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
