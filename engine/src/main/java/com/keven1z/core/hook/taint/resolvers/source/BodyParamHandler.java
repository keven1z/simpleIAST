package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;

import java.util.ArrayList;
import java.util.List;

public class BodyParamHandler implements SourceHandler{
    @Override
    public List<TaintData.FlowPath> handle(FlowObject fromObject, Object returnObject) {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        flowPaths.add(new TaintData.FlowPath(fromObject.getPathObject().toString(),returnObject));
        return flowPaths;
    }
}
