package java.lang.spy;

/**
 * 存在java/lang中的Scheduler接口，防止影响上线应用，名称复杂化
 */
public interface SimpleIASTSpy {
      void $_taint(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc, String type, String policyName);

      void $_single(Object returnObject, Object thisObject, Object[] parameters, String className, String method, String desc,String type, String policyName,boolean isRequireHttp);

      void $_requestStarted(Object requestObject, Object responseObject) ;

     
      void $_requestEnded(Object requestObject, Object responseObject) ;

     
      void $_setRequestBody(Object body) ;

     
      void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes, int off, int len) ;

     
      void $_onReadInvoked(Integer length, Object inputStream, byte[] bytes) ;

     
      void $_onReadInvoked(Integer b, Object inputStream) ;
}
