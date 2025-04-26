package com.keven1z.core.model.finding;

import com.keven1z.core.model.taint.TaintData;

import java.util.List;

public class TaintFindingData extends FindingData {
    private List<TaintData> flowData;
    public TaintFindingData(){

    }

    public List<TaintData> getFlowData() {
        return flowData;
    }

    public void setFlowData(List<TaintData> flowData) {
        this.flowData = flowData;
    }

}
