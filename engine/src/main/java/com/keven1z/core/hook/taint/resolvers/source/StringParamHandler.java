package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.taint.TaintData;
import com.keven1z.core.utils.CommonUtils;
import java.lang.reflect.Array;
import java.util.*;

public class StringParamHandler implements SourceHandler {
    @Override
    public List<TaintData.FlowPath> handle(Object fromObject,Object returnObject) throws Exception {

        return analyzeSource(fromObject, returnObject);
    }

    public List<TaintData.FlowPath> analyzeSource(Object fromObject, Object returnObject) {
        if (fromObject == null || returnObject == null) {
            return Collections.emptyList();
        }
        List<TaintData.FlowPath> flowPaths = new ArrayList<>();
        if (returnObject instanceof Iterator) {
            flowPaths.addAll(parseIteratorObject(fromObject, (Iterator<?>) returnObject));
        } else if (returnObject instanceof Map.Entry) {
            flowPaths.addAll(parseMapEntry(fromObject, (Map.Entry<?, ?>) returnObject));
        } else if (returnObject instanceof Map) {
            flowPaths.addAll(parseMap((Map<?, ?>) returnObject));
        } else if (returnObject.getClass().isArray() && !returnObject.getClass().getComponentType().isPrimitive()) {
            flowPaths.addAll(parseArrayObject(fromObject, returnObject));
        } else if (returnObject instanceof Collection) {
            if (returnObject instanceof List) {
                flowPaths.addAll(parseList(fromObject, (List<?>) returnObject));
            } else {
                flowPaths.addAll(parseIteratorObject(fromObject, ((Collection<?>) returnObject).iterator()));
            }
        } else if (returnObject instanceof Optional) {
            flowPaths.addAll(parseOptional(fromObject, returnObject));
        }
        else {
            TaintData.FlowPath flowPath = new TaintData.FlowPath(fromObject, returnObject);
            flowPaths.add(flowPath);
        }
        return flowPaths;
    }

    private Collection<? extends TaintData.FlowPath> parseArrayObject(Object fromObject, Object toObject) {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        int length = Array.getLength(toObject);
        for (int i = 0; i < length; i++) {
            Object data = Array.get(toObject, i);
            if (!CommonUtils.isEmpty(data)) {
                flowPaths.addAll(analyzeSource(fromObject, data));
            }
        }
        return flowPaths;
    }

    private List<TaintData.FlowPath> parseIteratorObject(Object fromObject, Iterator<?> iterator) {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        while (iterator.hasNext()) {
            Object data = iterator.next();
            if (!CommonUtils.isEmpty(data)) {
                flowPaths.addAll(analyzeSource(fromObject, data));
            }
        }
        return flowPaths;
    }

    private List<TaintData.FlowPath> parseMap(Map<?, ?> map) {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (!CommonUtils.isEmpty(key)  && !CommonUtils.isEmpty(value)) {
                flowPaths.addAll(analyzeSource(key, value));
            }
        }
        return flowPaths;
    }

    private List<TaintData.FlowPath> parseMapEntry(Object fromObject, Map.Entry<?, ?> entry) {
        Object value = entry.getValue();
        if (CommonUtils.isEmpty(value)) {
            return new ArrayList<>();
        }
        return analyzeSource(fromObject, value);
    }

    private List<TaintData.FlowPath> parseList(Object fromObject, List<?> list) {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        for (Object data : list) {
            if (!CommonUtils.isEmpty(data)) {
                flowPaths.addAll(analyzeSource(fromObject, data));
            }
        }
        return flowPaths;
    }

    private List<TaintData.FlowPath> parseOptional(Object fromObject, Object toObject) {
        try {
            Object value = ((Optional<?>) toObject).orElse(null);
            if (CommonUtils.isEmpty(value)) {
                return new ArrayList<>();
            }
            return analyzeSource(fromObject, value);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
