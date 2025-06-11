package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.EngineController;
import com.keven1z.core.consts.PolicyConst;
import com.keven1z.core.consts.HookType;
import com.keven1z.core.consts.SourceType;
import com.keven1z.core.log.ErrorType;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.policy.HookPolicy;
import com.keven1z.core.policy.IastHookManager;
import com.keven1z.core.utils.ClassUtils;
import com.keven1z.core.utils.CommonUtils;
import com.keven1z.core.utils.ReflectionUtils;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.keven1z.core.consts.CommonConst.OFF;
import static com.keven1z.core.consts.CommonConst.ON;

public class SpringParamHandler implements SourceHandler{
    private static final String[] USER_PACKAGE_PREFIX = new String[]{"java", "javax", " org.spring".substring(1), " org.apache".substring(1), " io.undertow".substring(1)};
    private static final String CLASS_HANDLER_METHOD = "org.springframework.web.method.HandlerMethod$HandlerMethodParameter";
    @Override
    public List<TaintData.FlowPath> handle(Object thisObject,
                                           Object[] parameters, Object returnObject) throws Exception {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        if (CommonUtils.isStartsWithElementInArray(returnObject.getClass().getName(), USER_PACKAGE_PREFIX)) {
            flowPaths.add(new TaintData.FlowPath(getSourceFromName(thisObject),returnObject));
        }else {
            addBeanObjectPolicy(returnObject.getClass());
        }
        return flowPaths;
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
            HookPolicy hookPolicy = HookPolicy.builder().className(taintClassName.replace(".", "/"))
                    .method(name)
                    .desc(ClassUtils.classToSmali(method.getReturnType()))
                    .from(PolicyConst.O)
                    .to(PolicyConst.R)
                    .state(ON)
                    .enter(OFF)
                    .exit(ON)
                    .name(SourceType.HTTP_BEAN_PARAM.getName())
                    .type(HookType.SOURCE)
                    .build();
//            TaintSpy.getInstance().getPolicyContainer().addPolicy(hookPolicy);
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
                if(!IastHookManager.getManager().shouldHookClass(taintClassName.replace(".", "/"))){
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
     * 获取spring污染源的入参名
     */
    private String getSourceFromName(Object fromObject) {
        if (CLASS_HANDLER_METHOD.equals(fromObject.getClass().getName())) {
            return ReflectionUtils.invokeStringMethod(fromObject, "getParameterName", new Class[]{});
        } else {
            return fromObject.toString();
        }
    }
}
