package com.platon.browser.common.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:35
 */
public class AnalysisResult {

    private String type;

    private String functionName;

    private Map <String, Object> parameters = new HashMap <>();

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getFunctionName () {
        return functionName;
    }

    public void setFunctionName ( String functionName ) {
        this.functionName = functionName;
    }

    public Map <String, Object> getParameters () {
        return parameters;
    }

    public void setParameters ( Map <String, Object> parameters ) {
        this.parameters = parameters;
    }
}