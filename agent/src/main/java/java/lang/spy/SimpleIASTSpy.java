package java.lang.spy;

/**
 * 存在java/lang中的Scheduler接口，防止影响上线应用，名称复杂化
 */
public interface SimpleIASTSpy {
     void doSpy(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isEnter);

}
