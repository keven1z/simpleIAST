package com.keven1z.core.pojo;

import com.keven1z.core.model.graph.TaintData;

import java.util.LinkedList;

public class TaintFindingData extends FindingData{
    private LinkedList<TaintData> flowData;
    public TaintFindingData(){

    }

    public LinkedList<TaintData> getFlowData() {
        return flowData;
    }

    public void setFlowData(LinkedList<TaintData> flowData) {
        this.flowData = flowData;
    }

}
