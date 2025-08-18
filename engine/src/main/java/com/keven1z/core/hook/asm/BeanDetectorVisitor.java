package com.keven1z.core.hook.asm;

import org.objectweb.asm.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author keven1z
 * @date 2025/8/15
 * @description 用于检测类是否为Bean的ASM访问者(无法检测builder模式的bean class)
 */
public class BeanDetectorVisitor extends ClassVisitor {
    private boolean hasDefaultConstructor = false;
    private boolean hasDataAnnotation = false;
    private final Map<String, MethodDesc> getters = new HashMap<>();
    private final Map<String, MethodDesc> setters = new HashMap<>();
    private final Set<String> pairedProperties = new HashSet<>();
    public BeanDetectorVisitor() {
        super(Opcodes.ASM9);
    }
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        // 检测Lombok的@Data注解
        if (descriptor.equals("Llombok/Data;") ||
                descriptor.equals("Llombok/Value;")) {
            hasDataAnnotation = true;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        if (name.equals("<init>") && desc.equals("()V")) {
            hasDefaultConstructor = true;
        } else if (isGetter(name, desc)) {
            String propertyName = getPropertyNameFromGetter(name);
            getters.put(propertyName, new MethodDesc(name, desc));
        } else if (isSetter(name, desc)) {
            String propertyName = getPropertyNameFromSetter(name);
            setters.put(propertyName, new MethodDesc(name, desc));
        }
        return null;
    }

    @Override
    public void visitEnd() {
        // 如果有@Data注解，则自动视为Bean类
        if (!hasDataAnnotation) {
            // 否则检查成对的getter/setter
            getters.keySet().stream().filter(setters::containsKey).forEach(propertyName -> {
                MethodDesc getter = getters.get(propertyName);
                MethodDesc setter = setters.get(propertyName);
                if (getReturnType(getter.desc).equals(getParameterType(setter.desc))) {
                    pairedProperties.add(propertyName);
                }
            });
        }
        super.visitEnd();
    }

    public boolean isBean() {
        // 有@Data注解或有成对的getter/setter
        return hasDataAnnotation || (hasDefaultConstructor && !pairedProperties.isEmpty());
    }

    private boolean isGetter(String name, String desc) {
        if (name.startsWith("get")) {
            return desc.matches("\\(\\).*") && !desc.endsWith(")V");
        } else if (name.startsWith("is")) {
            return desc.endsWith(")Z"); // boolean类型特有
        }
        return false;
    }

    private boolean isSetter(String name, String desc) {
        return name.startsWith("set") &&
                desc.matches("\\([^)]+\\)V") && // 一个参数且返回void
                !desc.endsWith("()V");
    }

    private String getPropertyNameFromGetter(String methodName) {
        String prefix = methodName.startsWith("is") ? "is" : "get";
        return decapitalize(methodName.substring(prefix.length()));
    }

    private String getPropertyNameFromSetter(String methodName) {
        return decapitalize(methodName.substring(3));
    }

    private String getReturnType(String desc) {
        return Type.getReturnType(desc).getDescriptor();
    }

    private String getParameterType(String desc) {
        Type[] argTypes = Type.getArgumentTypes(desc);
        return argTypes.length > 0 ? argTypes[0].getDescriptor() : "";
    }

    private String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    static class MethodDesc {
        final String name;
        final String desc;

        MethodDesc(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }
}
