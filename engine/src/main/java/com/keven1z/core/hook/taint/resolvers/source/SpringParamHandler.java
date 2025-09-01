package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.utils.CommonUtils;
import com.keven1z.core.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SpringParamHandler extends StringParamHandler {
    private static final String[] USER_PACKAGE_PREFIX = new String[]{"java", "javax", " org.spring".substring(1), " org.apache".substring(1), " io.undertow".substring(1)};
    private static final String CLASS_HANDLER_METHOD = "org.springframework.web.method.HandlerMethod$HandlerMethodParameter";

    @Override
    public List<TaintData.FlowPath> handle(Object thisObject, Object returnObject) throws Exception {
        if (!CommonUtils.isStartsWithElementInArray(returnObject.getClass().getName(), USER_PACKAGE_PREFIX)) {
            ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
            String sourceFromName = getSourceFromName(thisObject);
            flowPaths.add(new TaintData.FlowPath(sourceFromName, returnObject));
            return flowPaths;
        } else {
            return this.analyzeSource(thisObject, returnObject);
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
