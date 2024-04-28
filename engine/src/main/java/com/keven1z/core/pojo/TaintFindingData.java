package com.keven1z.core.pojo;

import com.keven1z.core.model.graph.TaintData;
import java.util.List;

public class TaintFindingData extends FindingData{
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
