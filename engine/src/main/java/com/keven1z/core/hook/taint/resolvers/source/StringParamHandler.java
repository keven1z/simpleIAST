package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.taint.TaintData;

import java.lang.reflect.Array;
import java.util.*;

public class StringParamHandler implements SourceHandler {
    @Override
    public List<TaintData.FlowPath> handle(Object thisObject,
                                           Object[] parameters, Object returnObject) throws Exception {
        return analyzeSource(thisObject, returnObject);
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
            flowPaths.addAll(parseMap(fromObject, (Map<?, ?>) returnObject));
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
        int length = Array.getLength(toObject);
        for (int i = 0; i < length; i++) {
            Object data = Array.get(toObject, i);
            if (data == null || data == "") {
                continue;
            }
            return analyzeSource(fromObject, data);
        }
        return new ArrayList<>();
    }

    private List<TaintData.FlowPath> parseIteratorObject(Object fromObject, Iterator<?> iterator) {
        while (iterator.hasNext()) {
            Object data = iterator.next();
            if (data == null || data == "") {
                continue;
            }
            return analyzeSource(fromObject, data);
        }
        return new ArrayList<>();
    }

    private List<TaintData.FlowPath> parseMap(Object fromObject, Map<?, ?> map) {
        for (Object value : map.values()) {
            if (value == null || value == "") {
                continue;
            }
            return analyzeSource(fromObject, value);
        }

        return new ArrayList<>();
    }

    private List<TaintData.FlowPath> parseMapEntry(Object fromObject, Map.Entry<?, ?> entry) {
        Object value = entry.getValue();
        if (value == null || value == "") {
            return new ArrayList<>();
        }
        return analyzeSource(fromObject, value);
    }

    private List<TaintData.FlowPath> parseList(Object fromObject, List<?> list) {
        for (Object data : list) {
            if (data == null || data == "") {
                continue;
            }
            return analyzeSource(fromObject, data);
        }
        return new ArrayList<>();
    }

    private List<TaintData.FlowPath> parseOptional(Object fromObject, Object toObject) {
        try {
            Object value = ((Optional<?>) toObject).orElse(null);
            if (value == null || value == "") {
                return new ArrayList<>();
            }
            return analyzeSource(fromObject, value);
        } catch (Exception e) {
            //pass
        }
        return new ArrayList<>();
    }
}
