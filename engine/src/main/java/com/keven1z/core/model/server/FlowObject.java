package com.keven1z.core.model.server;

/**
 * @author keven1z
 * @created 2025/4/24
 */
public class FlowObject {
    private final String pathFlag;
    private final Object pathObject;

    public FlowObject(String pathFlag, Object pathObject) {
        this.pathFlag = pathFlag;
        this.pathObject = pathObject;
    }

    public String getPathFlag() {
        return pathFlag;
    }

    public Object getPathObject() {
        return pathObject;
    }
}
