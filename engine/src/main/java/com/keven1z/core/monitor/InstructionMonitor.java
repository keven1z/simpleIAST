package com.keven1z.core.monitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.keven1z.core.EngineController;
import com.keven1z.core.consts.CommonConst;
import com.keven1z.core.log.LogTool;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.pojo.InstructionDTO;
import com.keven1z.core.utils.HttpClientUtils;
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
        String instruction = HttpClientUtils.getInstruction();
        if (instruction == null) {
            //如果获取失败，60s后再请求
            Thread.sleep(60 * 1000);
            return;
        }

        List<InstructionDTO> instructions = JsonUtils.toList(instruction, new TypeReference<List<InstructionDTO>>() {
        });
        if (instructions.isEmpty()) {
            Thread.sleep(1500);
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
        Thread.sleep(1500);
    }

    private void handlerAgentState(String value) throws Exception {
        int state = Integer.parseInt(value);
        if (CommonConst.ON == state) {
            if (ApplicationModel.isRunning()) {
                return;
            }
            ApplicationModel.start();
            boolean isSuccess = EngineController.register();
            if (isSuccess) {
                System.out.println("[SimpleIAST] Received instruction:  turn on agent successfully");
                if (LogTool.isDebugEnabled()) {
                    logger.info("Received instruction: turn on agent successfully");
                }
            } else {
                System.out.println("[SimpleIAST] Received instruction:  turn on agent failed");
                if (LogTool.isDebugEnabled()) {
                    logger.info("Received instruction: turn on agent failed");
                }
            }


        } else if (CommonConst.OFF == state) {
            if (!ApplicationModel.isRunning()) {
                return;
            }
            ApplicationModel.stop();
            boolean deregister = HttpClientUtils.deregister();
            if (deregister) {
                System.out.println("[SimpleIAST] Received instruction: close agent successfully");
                if (LogTool.isDebugEnabled()) {
                    logger.info("Received instruction: close agent successfully");
                }
            } else {
                System.out.println("[SimpleIAST] Received instruction: close agent failed");
                if (LogTool.isDebugEnabled()) {
                    logger.info("Received instruction: close agent failed");
                }
            }

        }
    }
}
