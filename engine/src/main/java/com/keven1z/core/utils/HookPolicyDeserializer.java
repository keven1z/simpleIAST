package com.keven1z.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keven1z.core.policy.ClassHookConfig;
import com.keven1z.core.policy.IastHookConfig;
import com.keven1z.core.policy.MethodHookConfig;

import java.io.IOException;
import java.util.*;

/**
 * 自定义的 JSON 反序列化器，用于将 JSON 数据转换为 IastHookConfig 对象。
 * @author keven1z
 * @date 2025/6/8
 */
public class HookPolicyDeserializer extends JsonDeserializer<IastHookConfig> {

    /**
     * 反序列化 JSON 数据，生成 IastHookConfig 对象
     *
     * @param p     JsonParser 对象，用于解析 JSON 数据
     * @param ctxt  DeserializationContext 对象，用于解析 JSON 数据
     * @return      解析后的 IastHookConfig 对象
     * @throws IOException 抛出 IOException 异常
     */
    @Override
    public IastHookConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        IastHookConfig iastHookConfig = new IastHookConfig();
        JsonNode iastHookNode = node.get("iast_hook_config");
        String version = iastHookNode.get("version").asText();
        iastHookConfig.setVersion(version);

        // 处理 hooks 数组
        List<ClassHookConfig> classHookConfigs = new ArrayList<>();
        JsonNode hooksArray = iastHookNode.get("hooks");
        if (hooksArray != null && hooksArray.isArray()) {
            for (JsonNode hookNode : hooksArray) {
                ClassHookConfig classHookConfig = new ClassHookConfig();
                List<MethodHookConfig> methodHookConfigs = new ArrayList<>();
                // 提取类名和是否为接口
                JsonNode classNode = hookNode.get("class");
                String className = classNode.get("name").asText();
                boolean isInterface = classNode.get("is_interface").asBoolean();
                JsonNode excludeClassesNode = classNode.get("exclude_classes");
                ArrayList<String> excludeClasses = new ArrayList<>(10);
                if (excludeClassesNode != null && excludeClassesNode.isArray()) {
                    for (JsonNode excludeClassNode : excludeClassesNode) {
                        excludeClasses.add(excludeClassNode.asText());
                    }
                }
                classHookConfig.setExcludeClasses(excludeClasses);
                classHookConfig.setClassName(className);
                classHookConfig.setInterface(isInterface);

                // 提取 methods
                JsonNode methodsArray = hookNode.get("methods");
                if (methodsArray != null && methodsArray.isArray()) {
                    for (JsonNode methodNode : methodsArray) {
                        MethodHookConfig methodHookConfig = new MethodHookConfig();
                        methodHookConfig.setName(methodNode.get("name").asText());
                        methodHookConfig.setDesc(methodNode.get("desc").asText());
                        methodHookConfig.setState(methodNode.get("state").asBoolean());

                        // HookPosition
                        MethodHookConfig.HookPosition position = new MethodHookConfig.HookPosition();
                        JsonNode hookPos = methodNode.get("hook_positions");
                        position.setEntry(hookPos.get("entry").asBoolean());
                        position.setExit(hookPos.get("exit").asBoolean());
                        methodHookConfig.setHookPositions(position);

                        // TaintTracking
                        JsonNode taint = methodNode.get("taint_tracking");
                        MethodHookConfig.TaintTracking tracking = new MethodHookConfig.TaintTracking();
                        tracking.setSource(taint.get("source").asBoolean());
                        tracking.setPropagator(taint.get("propagator").asBoolean());
                        tracking.setSink(taint.get("sink").asBoolean());
                        JsonNode http = taint.get("http");
                        if (http != null) {
                            tracking.setHttp(http.asBoolean());
                        }
                        // vulnerability_types
                        List<String> vulTypes = new ArrayList<>();
                        JsonNode vulnerabilityTypes = taint.get("vulnerability_types");
                        if (vulnerabilityTypes != null && vulnerabilityTypes.isArray()) {
                            for (JsonNode vt : taint.get("vulnerability_types")) {
                                vulTypes.add(vt.asText());
                            }
                            tracking.setVulnerabilityTypes(vulTypes);
                        }

                        // cleaning_effect
                        Map<String, String> cleanEffect = new HashMap<>();
                        JsonNode cleanEffNode = taint.get("cleaning_effect");
                        if (cleanEffNode != null) {
                            Iterator<Map.Entry<String, JsonNode>> fields = cleanEffNode.fields();
                            while (fields.hasNext()) {
                                Map.Entry<String, JsonNode> field = fields.next();
                                cleanEffect.put(field.getKey(), field.getValue().asText());
                            }
                            tracking.setCleaningEffect(cleanEffect);
                        }

                        // 设置 tracking_direction
                        JsonNode trackingDirectionNode = taint.get("tracking_direction");
                        if (trackingDirectionNode != null) {
                            MethodHookConfig.TrackingDirection trackingDirection = new MethodHookConfig.TrackingDirection(
                                    trackingDirectionNode.get("from") == null ? null : trackingDirectionNode.get("from").asText(),
                                    trackingDirectionNode.get("to") == null ? null : trackingDirectionNode.get("to").asText());
                            tracking.setTrackingDirection(trackingDirection);
                        }
                        // 设置 source_type 字段
                        JsonNode sourceTypeNode = taint.get("source_type");
                        if (sourceTypeNode != null) {
                            tracking.setSourceType(sourceTypeNode.asText());
                        }
                        //设置http_stage
                        JsonNode httpStageNode = taint.get("http_stage");
                        if(httpStageNode != null) {
                            tracking.setHttpStage(httpStageNode.asText());
                        }

                        methodHookConfig.setTaintTracking(tracking);
                        methodHookConfigs.add(methodHookConfig);
                    }
                }
                classHookConfig.setMethods(methodHookConfigs);
                classHookConfigs.add(classHookConfig);
            }
        }
        iastHookConfig.setHooks(classHookConfigs);
        return iastHookConfig;
    }
}
