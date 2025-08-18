package com.iast.astbenchmark.analyser.factory.stategy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iast.astbenchmark.analyser.bean.BaseOriginalDataBean;
import com.iast.astbenchmark.analyser.bean.consts.VendorEnum;
import com.iast.astbenchmark.analyser.factory.CaseDataTransfer;
import com.iast.astbenchmark.analyser.factory.stategy.openrasp.OpenraspResultBean;
import com.iast.astbenchmark.analyser.factory.stategy.openrasp.OpenraspTaintItemBean;
import com.iast.astbenchmark.analyser.factory.stategy.seeker.SeekerCollectBaseData;
import com.iast.astbenchmark.analyser.factory.stategy.simpleiast.SimpleIASTTaintItemBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SimpleIASTCaseDataTransfer extends CaseDataTransfer {

    public VendorEnum vendor() {
        return VendorEnum.SIMPLEIAST;
    }

    public Map<String, BaseOriginalDataBean> extrapResultMap(String path) {
        /**
         *  Step1 ->获取检出结果并解析；
         *  指定检测结果目录 以及检测标记
         */
        List<SimpleIASTTaintItemBean> taintItemBeans = getReportLog(path);
        /**
         *  Step2 -> 抽取Tag
         *  默认使用MethedName作为Case的tag进行标记
         */
        return convertToTagMap(taintItemBeans);
    }

    private Map<String, BaseOriginalDataBean> convertToTagMap(List<SimpleIASTTaintItemBean> logsBeans) {
        if (CollectionUtils.isEmpty(logsBeans)) {
            return Maps.newHashMap();
        }
        return logsBeans.stream().filter(e -> StrUtil.isNotEmpty(e.getVulnerableType())
                        && StrUtil.isNotEmpty(e.getUrl()))
                .collect(Collectors.toMap(e1 -> getTagKey(e1), e2 -> e2, (k1, k2) -> k1));
    }

    private String getTagKey(SimpleIASTTaintItemBean baseData) {
        String url = baseData.getUri();
        if (url.contains("case00")) {
            String tag = "aTaintCase00" + url.split("case00")[1].split("/")[0];
            if (url.endsWith("/2")) {
                tag = tag + "_2";
            } else if (url.endsWith("/1")) {
                tag = tag + "_1";
            } else if (url.endsWith("/3")) {
                tag = tag + "_3";
            } else if (url.endsWith("/4")) {
                tag = tag + "_4";
            } else if (url.endsWith("/5")) {
                tag = tag + "_5";
            } else if (url.endsWith("/6")) {
                tag = tag + "_6";
            } else if (url.endsWith("/7")) {
                tag = tag + "_7";
            }
            return tag;
        }
        return "";
    }

    private List<SimpleIASTTaintItemBean> getReportLog(String file) {
        JSONArray array = JSONUtil.readJSONArray(FileUtil.file(file), StandardCharsets.UTF_8);
        if (array.isEmpty()) {
            return Lists.newArrayList();
        }
        List<SimpleIASTTaintItemBean> res = Lists.newArrayList();
        for (Object obj : array) {
            res.add(JSONUtil.toBean(JSONUtil.toJsonStr(obj), SimpleIASTTaintItemBean.class));
        }
        return res;
    }
}
