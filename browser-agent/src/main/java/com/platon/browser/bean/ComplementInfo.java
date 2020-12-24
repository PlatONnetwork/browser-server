package com.platon.browser.bean;

public class ComplementInfo {
    // 交易类型
    Integer type = null;
    Integer toType = null;
    // 合约代码
    String binCode = null;
    // 合约方法
    String method = null;
    // 合约类型
    Integer contractType = null;
    // tx info信息
    String info = "{}";

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getToType() {
        return this.toType;
    }

    public void setToType(Integer toType) {
        this.toType = toType;
    }

    public String getBinCode() {
        return this.binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getContractType() {
        return this.contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}