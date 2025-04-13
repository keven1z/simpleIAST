package com.keven1z.core.consts;


public class Api {
    private Api() {
    }

    private static final String CLIENT = "/client";
    public static final String AGENT_REGISTER_URL = CLIENT + "/agent/register";
    public static final String AGENT_DEREGISTER_URL = CLIENT + "/agent/deregister";

    /**
     * 发送报告的url
     */
    public static final String SEND_REPORT_URL = CLIENT + "/report/receive";
    /**
     * 获取服务端指令url
     */
    public static final String INSTRUCTION_GET_URL = CLIENT + "/instruction/get";
    /**
     *
     */
    public static final String HEARTBEAT_URL = CLIENT + "/agent/heartbeat";

}
