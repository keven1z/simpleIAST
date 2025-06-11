package com.keven1z.core.hook.taint.resolvers.source;

import com.keven1z.core.model.taint.TaintData;

import java.util.List;

public interface SourceHandler {
    /**
     * 处理特定类型的Source数据
     *
     * @param returnObject hook点返回对象
     * @param parameters hook点参数
     * @param object hook对象
     * @return 提取的污点数据
     */
    List<TaintData.FlowPath> handle(Object returnObject, Object[] parameters, Object object) throws Exception;
}
