package com.keven1z.core.pojo;




public class SingleFindingData extends FindingData{
    private String className;
    private String method;
    private String desc;
    private Object detail;

    public SingleFindingData(String className, String method, String desc, Object detail) {
        this.className = className;
        this.method = method;
        this.desc = desc;
        this.detail = detail;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }
}
