package com.keven1z.core.consts;

public enum VulnEnum {
    SSRF("ssrf", 2),
    SQL_INJECTION("sqli", 2),
    CMD_INJECTION("cmdi", 1),
    DESERIALIZATION("deserialization", 2),
    XXE("xxe", 2),
    URL_REDIRECT("url_redirect", 3),
    FILE_UPLOAD("file_upload", 2),
    XSS("xss", 3),
    Path_Traversal("path_traversal", 1),
    Sp_EL("spring_expression_injection", 1),
    WEAK_PASSWORD_IN_SQL("weak_password_in_sql", 3),
    WEAK_PASSWORD_IN_POST("weak_password_in_post", 3);
    private final String name;
    /**
     * 漏洞等级
     */
    private final int level;

    VulnEnum(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
