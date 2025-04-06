package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.pojo.InstructionDTO;
import com.keven1z.core.utils.IASTHttpClient;
import com.keven1z.core.utils.JsonUtils;

import java.util.List;

public class InstructionMonitor extends Monitor {
    private static final String INSTRUCTION_CHANGE_STATE = "state";

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
        Thread.sleep(30 * 1000);
        String instruction = IASTHttpClient.getClient().getInstruction();
        if (instruction == null) {
            if (ApplicationModel.isRunning()) {
                ApplicationModel.stop();
            }
            //如果获取失败，120s后再请求
            Thread.sleep(120 * 1000);
            return;
        } else {
            if (ApplicationModel.isRunning()) {
                ApplicationModel.start();
            }
        }

        List<InstructionDTO> instructions = JsonUtils.toList(instruction, new TypeReference<List<InstructionDTO>>() {
        });

        if (instructions.isEmpty()) {
            return;
        }
        if (LogTool.isDebugEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (InstructionDTO instructionDTO : instructions) {
                stringBuilder.append(instructionDTO).append(",");
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            logger.info("Received instruction: " + stringBuilder);
        }
        for (InstructionDTO instructionDTO : instructions) {
            String name = instructionDTO.getName();
            if (INSTRUCTION_CHANGE_STATE.equals(name)) {
                handlerAgentState(instructionDTO.getValue());
            }
        }
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
