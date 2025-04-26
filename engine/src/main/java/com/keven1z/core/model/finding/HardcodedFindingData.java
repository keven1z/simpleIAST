package com.keven1z.core.model.finding;

import com.keven1z.core.consts.VulnerabilityLevel;
/**
 * @author keven1z
 * @since 2024/07/28
 */
public class HardcodedFindingData extends FindingData {
    // 类名
    private String className;
    // 属性名
    private String parameterName;
    // 属性值
    private String parameterValue;
    // 是否mask
    private boolean isMasked;
    // 源文件名
    private String sourceFile;

    public HardcodedFindingData(String className, String parameterName, String parameterValue, String sourceFile) {
        this.className = className;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.sourceFile = sourceFile;
        super.setLevel(VulnerabilityLevel.MIDDLE.getPriority());
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public boolean isMasked() {
        return isMasked;
    }

    public void setMasked(boolean masked) {
        isMasked = masked;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
}
