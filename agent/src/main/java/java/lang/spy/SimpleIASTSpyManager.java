package java.lang.spy;

public class SimpleIASTSpyManager {
    public static SimpleIASTSpy spy;
    public static boolean isInit() {
        return spy != null;
    }
    public static void init(
            final SimpleIASTSpy scheduler) {
        SimpleIASTSpyManager.spy = scheduler;
    }
    public static void spyMethod(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isEnter){
        try {
            spy.doSpy(returnObject, thisObject, parameters, className, method, desc, type, policyName, isEnter);
        } catch (Throwable cause) {
//            handleException(cause);
        }
    }
}
