package com.iast.astbenchmark.analyser.factory.stategy.simpleiast;

import com.iast.astbenchmark.analyser.bean.BaseOriginalDataBean;
import lombok.Data;

@Data
public class SimpleIASTTaintItemBean extends BaseOriginalDataBean {
    private String id;
    private String url;
    private String uri;
    private String request_method;
    private String vulnerableType;

}
