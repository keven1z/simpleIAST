package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipartParamHandler implements SourceHandler{
    @Override
    public List<TaintData.FlowPath> handle(FlowObject fromObject, Object returnObject) throws Exception {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        if (returnObject instanceof Collection<?>){
            for (Object partObject : (Collection<?>) returnObject){
                flowPaths.add(handlePart(fromObject,partObject));
            }
        }else {
            flowPaths.add(handlePart(fromObject,returnObject));
        }
        return flowPaths;
    }
    private TaintData.FlowPath handlePart(FlowObject fromObject, Object returnObject){
        return new TaintData.FlowPath(fromObject.getPathObject().toString(),returnObject);
    }
}
