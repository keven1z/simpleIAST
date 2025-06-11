package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.taint.TaintData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipartParamHandler implements SourceHandler{
    @Override
    public List<TaintData.FlowPath> handle(Object thisObject,
                                           Object[] parameters, Object returnObject) throws Exception {
        ArrayList<TaintData.FlowPath> flowPaths = new ArrayList<>();
        if (returnObject instanceof Collection<?>){
            for (Object partObject : (Collection<?>) returnObject){
                flowPaths.add(handlePart(thisObject,partObject));
            }
        }else {
            flowPaths.add(handlePart(thisObject,returnObject));
        }
        return flowPaths;
    }
    private TaintData.FlowPath handlePart(Object fromObject, Object returnObject){
        return new TaintData.FlowPath(fromObject,returnObject);
    }
}
