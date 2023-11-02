/*
 * Copyright 2017-2021 Baidu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keven1z.core.log;

public enum ErrorType {
    FSWATCH_ERROR(20001, "File Watch Error"),
    RUNTIME_ERROR(20002, "Common Error"),
    CONFIG_ERROR(20004, "Config Error"),
    PLUGIN_ERROR(20005, "Plugin Error"),
    REQUEST_ERROR(20006, "Request Error"),
    HOOK_ERROR(20007, "Hook Error"),
    REGISTER_ERROR(20008, "Cloud Control Registered Failed"),
    HEARTBEAT_ERROR(20009, "Cloud Control Send HeartBeat Failed"),
    DETECT_SERVER_ERROR(20012, "Detect Server Error"),
    REFLECTION_ERROR(20013, "Reflex Failed"),
    CPU_ERROR(20014, "Count Cpu Usage Failed"),
    DEPENDENCY_ERROR(20015, "Find Dependency Information Failed"),
    REPORT_ERROR(20016, "Report Failed"),
    POLICY_ERROR(200017, "Policy Load Error"),

    RESOLVE_ERROR(200018, "Resolve Error"),
    TRANSFORM_ERROR(20019, "Transform Error"),
    MEMORY_ERROR(20020, "Count Memory Usage Failed"),
    DETECT_VULNERABILITY_ERROR(20020, "Detect Vulnerability Error"),
    REFLECT_ERROR(20021,"Reflect Method Error"),
    SHUTDOWN_ERROR(20022,"Shutdown Error"),
    REQUEST_START_ERROR(20023, "Request start error");

    private final int code;
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + ":" + message;
    }
}
