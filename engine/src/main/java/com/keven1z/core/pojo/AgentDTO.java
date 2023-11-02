package com.keven1z.core.pojo;

public class AgentDTO {
    private String agentId;
    private String os;
    private String hostName;
    private String serverPath;
    private String webClass;

    public AgentDTO(String agentId, String hostName, String os,String serverPath,String webClass) {
        this.agentId = agentId;
        this.hostName = hostName;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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
}
