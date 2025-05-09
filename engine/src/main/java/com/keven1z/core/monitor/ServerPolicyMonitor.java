package com.keven1z.core.monitor;

import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.policy.PolicyUpdateResult;
import com.keven1z.core.policy.ServerPolicyManager;
import com.keven1z.core.utils.IASTHttpClient;


public class ServerPolicyMonitor extends Monitor {
    private  ServerPolicyManager serverPolicyManager;
    public ServerPolicyMonitor() {
    }
    @Override
    public String getThreadName() {
        return "SimpleIAST-InstructionMonitor-Thread";
    }

    @Override
    public boolean isForServer() {
        return true;
    }

    @Override
    public void doRun() throws Exception {
        if (this.serverPolicyManager == null) {
            this.serverPolicyManager = ServerPolicyManager.getInstance();
        }
        PolicyUpdateResult policyUpdateResult = serverPolicyManager.checkUpdate();
        boolean isUpdated = policyUpdateResult.isUpdated();
        Thread.sleep(30 * 1000);
    }

    private void handlerAgentState(String value) {
        try {
            int state = Integer.parseInt(value);

            switch (state) {
                case CommonConst.ON:
                    handleAgentStart();
                    break;
                case CommonConst.OFF:
                    handleAgentStop();
                    break;
                default:
                    logger.warn(String.format("Received unsupported agent state: %s", state));
            }
        } catch (Exception e) {
            logger.error(String.format("Error processing agent state: %s", value), e);
        }
    }

    private void handleAgentStart() {
        if (ApplicationModel.isRunning()) {
            logger.info("[SimpleIAST] Agent is already running.");
            return;
        }

        ApplicationModel.start();
        try {
            logAgentStatus("turn on agent", true);
        } catch (Exception e) {
            logAgentStatus("turn on agent", false);
            logger.error("Failed to turn on agent", e);
        }
    }

    private void handleAgentStop() {
        if (!ApplicationModel.isRunning()) {
            logger.info("[SimpleIAST] Agent is already stopped.");
            return;
        }

        ApplicationModel.stop();
        boolean deregister = IASTHttpClient.getClient().deregister();
        logAgentStatus("close agent", deregister);
    }

    private void logAgentStatus(String action, boolean success) {
        String statusMessage = success ? "successfully" : "failed";
        String logMessage = String.format("[SimpleIAST] Received instruction: %s %s", action, statusMessage);

        System.out.println(logMessage);
        if (LogTool.isDebugEnabled()) {
            logger.debug(logMessage);
        }
    }
}
