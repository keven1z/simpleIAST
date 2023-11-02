package com.keven1z.core.consts;

public class VulnConst {
    /**
     * 服务端请求访问
     */
    public static final String SSRF = "ssrf";
    /**
     * sql注入
     */
    public static final String SQL_INJECTION = "sqli";
    /**
     * 命令注入
     */
    public static final String CMD_INJECTION = "cmdi";

    /**
     * 反序列化漏洞
     */
    public static final String DESERIALIZATION = "deserialization";
    /**
     * XXE漏洞
     */
    public static final String XXE = "xxe";
    /**
     * URL跳转
     */
    public static final String URL_REDIRECT = "url_redirect";
    /**
     * 文件上传
     */
    public static final String FILE_UPLOAD = "file_upload";
    /**
     * XSS
     */
    public static final String XSS = "xss";
    /**
     * 目录遍历
     */
    public static final String Path_Traversal = "path_traversal";
    /**
     * Spring EL表达式
     */
    public static final String Sp_EL = "spring_expression_injection";
    /**
     * 数据库连接弱密码
     */
    public static final String WEAK_PASSWORD_IN_SQL = "weak_password_in_sql";
    /**
     * POST请求表单弱密码
     */
    public static final String WEAK_PASSWORD_IN_POST = "Spring_Expression_injection";

}
