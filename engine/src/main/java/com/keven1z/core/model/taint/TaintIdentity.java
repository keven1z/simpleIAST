package com.keven1z.core.model.taint;

/**
 * 污点唯一标识（包含调用链ID、位置索引和哈希指纹）
 * 用于快速定位和校验污点数据
 */
public class TaintIdentity {
    /**
     * 污点类唯一标识,用于查找污点类
     */
    private final int invokeId;
    /**
     * 污点类对应污点的位置
     */
    private final int taintIndex;
    /**
     * 污点hashcode
     */
    private final int taintHashCode;

    public TaintIdentity(int invokeId, int taintIndex, int taintHashCode) {
        this.invokeId = invokeId;
        this.taintIndex = taintIndex;
        this.taintHashCode = taintHashCode;
    }

    public int getInvokeId() {
        return invokeId;
    }

    public int getTaintIndex() {
        return taintIndex;
    }

    public int getTaintHashCode() {
        return taintHashCode;
    }

    @Override
    public String toString() {
        return "TaintIdentity{" +
                "invokeId=" + invokeId +
                ", taintIndex=" + taintIndex +
                ", taintHashCode=" + taintHashCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaintIdentity that = (TaintIdentity) o;
        return taintHashCode == that.taintHashCode;
    }

    @Override
    public int hashCode() {
        return taintHashCode;
    }
}
