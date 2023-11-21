package com.keven1z.core.consts;

import com.keven1z.core.Config;

public class Api {
    private Api() {
    }

    private static final String CLIENT = "/client";
    public static final String AGENT_REGISTER_URL = Config.IAST_SERVER + CLIENT + "/agent/register";
    public static final String AGENT_DEREGISTER_URL = Config.IAST_SERVER + CLIENT + "/agent/deregister";

    /**
     * 发送报告的url
     */
    public static final String SEND_REPORT_URL = Config.IAST_SERVER + CLIENT + "/report/receive";
    /**
     * 获取服务端指令url
     */
    public static final String INSTRUCTION_GET_URL = Config.IAST_SERVER + CLIENT + "/instruction/get";

}
