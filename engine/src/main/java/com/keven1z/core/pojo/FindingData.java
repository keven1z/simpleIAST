package com.keven1z.core.pojo;

import com.keven1z.core.model.graph.TaintData;

import java.util.LinkedList;

public class FindingData {
    private String vulnerableType;
    public FindingData(){

    }


    public String getVulnerableType() {
        return vulnerableType;
    }

    public void setVulnerableType(String vulnerableType) {
        this.vulnerableType = vulnerableType;
    }
}
