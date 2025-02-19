package com.keven1z.core.pojo;

public class AgentDTO {
    private String agentId;
    private String os;
    private String hostname;
    private String serverPath;
    private String webClass;
    private String jdkVersion;
    private String version;
    /**
     * agent所绑定的项目名
     */
    private String projectName;
    private String process;
    public AgentDTO(String agentId, String hostname, String os,String serverPath,String webClass) {
        this.agentId = agentId;
        this.hostname = hostname;
        this.os = os;
        this.serverPath = serverPath;
        this.webClass = webClass;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getWebClass() {
        return webClass;
    }

    public void setWebClass(String webClass) {
        this.webClass = webClass;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
