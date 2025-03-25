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
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.keven1z.core.consts.CommonConst.OFF;
import static com.keven1z.core.consts.CommonConst.ON;
import static com.keven1z.core.consts.PolicyConst.SOURCE_BEAN;
import static com.keven1z.core.hook.HookThreadLocal.TAINT_GRAPH_THREAD_LOCAL;

/**
 * 污染源的解析器
 *
 * @author keven1z
 * @since 2023/01/15
 */
public class SourceClassResolver implements HandlerHookClassResolver {
    private static final String[] USER_PACKAGE_PREFIX = new String[]{"java", "javax", " org.spring".substring(1), " org.apache".substring(1), " io.undertow".substring(1)};
    private static final String[] BLACK_SPRINGFRAMEWORK_RETURN_OBJECT = new String[]{"org.springframework.web", "SecurityContextHolderAwareRequestWrapper"};
    private static final String CLASS_OPTIONAL = "java.util.Optional";

    @Override
    public void resolve(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String policyName, String from, String to) {
        if (!isReturnObjectFiltered(returnObject)) {
            return;
        }

        Map<String, Object> fromMap = PolicyUtils.getFromPositionObject(from, parameters, returnObject, thisObject);
        if (fromMap == null || fromMap.isEmpty()) {
            return;
        }

        if (SOURCE_BEAN.equals(policyName)) {
            resolveBeanHook(className, method, desc, returnObject, fromMap);
            return;
        }
        int returnIdentityHashCode = System.identityHashCode(returnObject);
        //如果已经存在同一污点,不继续加入该污染源
        if (TAINT_GRAPH_THREAD_LOCAL.get().isTaint(returnIdentityHashCode)) {
            return;
        }
        TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);
        searchAndFillSourceFromReturnObject(returnObject, taintData);
        taintData.setFromValue(getSourceFromName(fromMap));
        //加入原始对象的hashcode
        taintData.setToObjectHashCode(returnIdentityHashCode);
        TaintUtils.buildTaint(returnObject, taintData, true);
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

    private void resolveBeanHook(String className, String method, String desc, Object returnObject, Map<String, Object> fromMap) {
        TaintGraph taintGraph = TAINT_GRAPH_THREAD_LOCAL.get();
        Map.Entry<String, Object> objectEntry = fromMap.entrySet().iterator().next();
        TaintNode parentNode = PolicyUtils.searchParentNode(objectEntry.getValue(), taintGraph);
        if (parentNode == null) {
            return;
        }
        TaintData taintData = new TaintData(className, method, desc, PolicyTypeEnum.SOURCE);
        taintGraph.addEdge(parentNode.getTaintData(), taintData, objectEntry.getKey());

        searchAndFillSourceFromReturnObject(returnObject, taintData);
        taintData.setToObject(returnObject);
        TaintUtils.buildTaint(returnObject, taintData, true);

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
        Set<String> toBeTransformedClass = new HashSet<>();
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
            toBeTransformedClass.add(taintClassName);
        }
        for (String taintClassName : toBeTransformedClass) {
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

                if (CommonUtils.isStartsWithElementInArray(taintClassName, USER_PACKAGE_PREFIX)) {
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

    /**
     * @param returnObject 返回污染源
     * @return 判断源是否可以视为污染源，false表示无法作为污染源，true表示可以作为污染源
     */
    private boolean isReturnObjectFiltered(Object returnObject) {
        if (returnObject == null || returnObject.equals("")) {
            return false;
        } else if (returnObject instanceof Collection) {
            if (((Collection<?>) returnObject).isEmpty()) {
                return false;
            }
        } else if (returnObject instanceof Map) {
            if (((Map<?, ?>) returnObject).isEmpty()) {
                return false;
            }
        }
        /*
          过滤HandlerMethodArgumentResolverComposite初始包装request对象
         */
        for (String blackReturnObject : BLACK_SPRINGFRAMEWORK_RETURN_OBJECT) {
            if (returnObject.toString().startsWith(blackReturnObject)) {
                return false;
            }
        }
        return true;
    }

    public void searchAndFillSourceFromReturnObject(Object returnObject, TaintData taintData) {
        if (returnObject instanceof Iterator) {
            parseIteratorObject((Iterator<?>) returnObject, taintData);
        } else if (returnObject instanceof Map.Entry) {
            parseMapEntry((Map.Entry<?, ?>) returnObject, taintData);
        } else if (returnObject instanceof Map) {
            parseMap((Map<?, ?>) returnObject, taintData);
        } else if (returnObject.getClass().isArray() && !returnObject.getClass().getComponentType().isPrimitive()) {
            parseArrayObject(returnObject, taintData);
        } else if (returnObject instanceof Collection) {
            if (returnObject instanceof List) {
                parseList((List<?>) returnObject, taintData);
            } else {
                parseIteratorObject(((Collection<?>) returnObject).iterator(), taintData);
            }
        } else if (CLASS_OPTIONAL.equals(returnObject.getClass().getName())) {
            parseOptional(returnObject, taintData);
        } else if (!CommonUtils.isStartsWithElementInArray(returnObject.getClass().getName(), USER_PACKAGE_PREFIX)) {
            addBeanObjectPolicy(returnObject.getClass());
        } else {
            taintData.setToObject(returnObject);
        }

    }

    private void parseArrayObject(Object toObject, TaintData taintData) {
        int length = Array.getLength(toObject);
        for (int i = 0; i < length; i++) {
            Object data = Array.get(toObject, i);
            if (data == null || data == "") {
                continue;
            }
            searchAndFillSourceFromReturnObject(data, taintData);
        }
    }

    private void parseIteratorObject(Iterator<?> iterator, TaintData taintData) {
        while (iterator.hasNext()) {
            Object data = iterator.next();
            if (data == null || data == "") {
                continue;
            }
            searchAndFillSourceFromReturnObject(data, taintData);
        }
    }

    private void parseMap(Map<?, ?> map, TaintData taintData) {
        for (Object value : map.values()) {
            if (value == null || value == "") {
                continue;
            }
            searchAndFillSourceFromReturnObject(value, taintData);
        }
    }

    private void parseMapEntry(Map.Entry<?, ?> entry, TaintData taintData) {
        Object value = entry.getValue();
        if (value == null || value == "") {
            return;
        }
        searchAndFillSourceFromReturnObject(value, taintData);
    }

    private void parseList(List<?> list, TaintData taintData) {
        for (Object data : list) {
            if (data == null || data == "") {
                continue;
            }
            searchAndFillSourceFromReturnObject(data, taintData);
        }
    }

    private void parseOptional(Object obj, TaintData taintData) {
        try {
            Object value = ((Optional<?>) obj).orElse(null);
            if (value == null || value == "") {
                return;
            }
            searchAndFillSourceFromReturnObject(value, taintData);
        } catch (Exception e) {
            //pass
        }
    }
}
