package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.server.FlowObject;
import com.keven1z.core.model.taint.TaintData;

import java.util.List;

public interface SourceHandler {
    /**
     * 处理特定类型的Source数据
     *
     * @param returnObject hook点返回对象
     * @return 提取的污点数据
     */
    List<TaintData.FlowPath> handle(FlowObject fromObject, Object returnObject) throws Exception;
}
