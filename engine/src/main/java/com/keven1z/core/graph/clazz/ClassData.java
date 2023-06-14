package com.keven1z.core.graph.clazz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author keven1z
 * @Date 2021/6/11
 * @Description hook类的信息
 */
public class ClassData {
    private String className;
    private int access = -1;
    private final List<String> ancestors;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ClassData(String className) {
        this.className = className;
        this.ancestors = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassData classData = (ClassData) o;
        return Objects.equals(className, classData.className);
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return className;
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    /**
     * 构建当前类的祖先类
     *
     * @param interfaces 接口
     * @param superClass 父类
     */
    public void buildAncestors(String[] interfaces, String superClass) {
        if (superClass != null && !superClass.equals("java/lang/Object")) {
            this.ancestors.add(superClass);
        }
        this.ancestors.addAll(Arrays.asList(interfaces));
    }

    public List<String> getAncestors() {
        return ancestors;
    }
}
