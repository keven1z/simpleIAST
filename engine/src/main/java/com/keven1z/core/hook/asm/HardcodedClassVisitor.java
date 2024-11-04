package com.keven1z.core.hook.asm;

import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.pojo.ReportData;
import com.keven1z.core.pojo.finding.HardcodedFindingData;
import com.keven1z.core.utils.Base64Utils;
import com.keven1z.core.utils.CommonUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import static com.keven1z.core.consts.VulnEnum.HARD_CODE;
import static com.keven1z.core.hook.HookThreadLocal.REPORT_QUEUE;

/**
 * @author keven1z
 * @date 2024/07/28
 */
public class HardcodedClassVisitor extends ClassVisitor {


    private final Pattern e = Pattern.compile("^[a-zA-Z]+\\.[\\.a-zA-Z]*[a-zA-Z]+$");

    private final Pattern f = Pattern.compile("^[a-zA-Z]+\\_[\\_a-zA-Z]*[a-zA-Z]+$");

    private final String[] keyArray = {"key", "aes", "des", "iv", "secret", "blowfish"};
    private final String[] passArray = {"PASSWORD", "PASSKEY", "PASSPHRASE", "SECRET", "ACCESS_TOKEN",
            "AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY"};
    private final String[] notPrefixes = {"date", "forgot", "form", "encode", "pattern", "prefix", "prop", "suffix",
            "url"};
    private final String className;
    private String source;

    public HardcodedClassVisitor(String className) {
        super(Opcodes.ASM9);
        this.className = className;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldVisitor fieldVisitor = super.visitField(access, name, desc, signature, value);
        if (null != value) {
            if ("[B".equals(desc) && isKeysField(name)) {
                byte[] bytes = (byte[]) value;
                reportFinding(name, Base64Utils.encode(bytes));
            } else if ("Ljava/lang/String;".equals(desc) && isStaticAndFinal(access) && isPassField(name)
                    && !isWrongPrefix(name) && value instanceof String) {
                String fieldValue = (String) value;
                if (!CommonUtils.isEmpty(fieldValue) && !valueMatcher(fieldValue)) {
                    reportFinding(name, fieldValue);
                }
            }
        }
        return fieldVisitor;
    }

    /**
     * 重写父类方法，访问源代码。
     *
     * @param source 源代码
     * @param debug  调试信息
     */
    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
        this.source = source;
    }

    private boolean isStaticAndFinal(int access) {
        return (Modifier.isStatic(access) && Modifier.isFinal(access));
    }

    private boolean isPassField(String name) {
        return containArrayItem(name, passArray);
    }

    private boolean isWrongPrefix(String name) {
        return containArrayItem(name, notPrefixes);
    }

    private boolean isKeysField(String name) {
        return containArrayItem(name, keyArray);
    }

    private boolean valueMatcher(String value) {
        return e.matcher(value).find() || f.matcher(value).find();
    }

    private boolean containArrayItem(String name, String[] arrays) {
        name = name.toUpperCase();
        for (String item : arrays) {
            if (name.equals(item)) {
                return true;
            }
        }
        return false;
    }

    private void reportFinding(String parameterName, String parameterValue) {
        HardcodedFindingData hardcodedFindingData = new HardcodedFindingData(this.className, parameterName, parameterValue, source);
        ReportData reportMessage = new ReportData(ApplicationModel.getAgentId());
        hardcodedFindingData.setVulnerableType(HARD_CODE.getName());
        reportMessage.addFindingDataList(hardcodedFindingData);
        REPORT_QUEUE.offer(reportMessage);
    }

}
