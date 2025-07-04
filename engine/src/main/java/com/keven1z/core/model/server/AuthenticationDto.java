package com.keven1z.core.model.server;

public class AuthenticationDto {
    private String token;
    private String agentId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "AuthenticationDTO{" +
                "agentId='" + agentId + '\'' +
                '}';
    }
}