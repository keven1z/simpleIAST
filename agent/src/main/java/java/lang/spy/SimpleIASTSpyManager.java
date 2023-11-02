package java.lang.spy;

public class SimpleIASTSpyManager {
    public static SimpleIASTSpy taintSpy;
    public static SimpleIASTSpy httpSpy;
    public static SimpleIASTSpy singleSpy;

    public static boolean isInit() {
        return taintSpy != null && httpSpy != null;
    }

    public static void init(final SimpleIASTSpy taintSpy,
                            final SimpleIASTSpy httpSpy, final SimpleIASTSpy singleSpy) {
        SimpleIASTSpyManager.taintSpy = taintSpy;
        SimpleIASTSpyManager.httpSpy = httpSpy;
        SimpleIASTSpyManager.singleSpy = singleSpy;
    }

    public static void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName) {
        try {
            taintSpy.$_taint(returnObject, thisObject, parameters, className, method, desc, type, policyName);
        } catch (Throwable ignored) {
        }
    }

    public static void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName, boolean isRequireHttp) {
        try {
            singleSpy.$_single(returnObject, thisObject, parameters, className, method, desc, type, policyName, isRequireHttp);
        } catch (Throwable ignored) {
        }
    }


    public static void $_requestStarted(Object requestObject, Object responseObject) {
        try {
            httpSpy.$_requestStarted(requestObject, responseObject);
        } catch (Throwable ignored) {
        }
    }


    public static void $_requestEnded(Object requestObject, Object responseObject) {
        try {
            httpSpy.$_requestEnded(requestObject, responseObject);
        } catch (Throwable cause) {
        }
    }


    public static void $_setRequestBody(Object body) {
        try {
            httpSpy.$_setRequestBody(body);
        } catch (Throwable cause) {
        }
    }


    public static void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) {
        try {
            httpSpy.$_onReadInvoked(length, inputStream, bytes, off, len);
        } catch (Throwable cause) {
        }
    }


    public static void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) {
        try {
            httpSpy.$_onReadInvoked(length, inputStream, bytes);
        } catch (Throwable cause) {
        }
    }


    public static void $_onReadInvoked(Integer b, Object inputStream) {
        try {
            httpSpy.$_onReadInvoked(b, inputStream);
        } catch (Throwable cause) {
        }
    }
}
