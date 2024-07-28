package com.keven1z.core.pojo.finding;

public class FindingData {
    private String vulnerableType;
    private int level;

    public FindingData() {

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getVulnerableType() {
        return vulnerableType;
    }

    public void setVulnerableType(String vulnerableType) {
        this.vulnerableType = vulnerableType;
    }
}
