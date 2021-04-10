package com.platon.browser.bean;

import java.util.List;

/**
 *特殊节点存储的以块为存储单位的【合约调用PPOS】的输入信息
 * [
 *  {
 *      "TxHash":"合约调用hash",
 *      "From":"调用方地址",
 *      "To":"内置PPOS合约地址",
 *      "Input":[
 *          "PPOS调用输入数据"
 *      ]
 *  }
 * ]
 */
public class PPosInvokeContractInput {
    private List<TransData> transDatas;
    private String txHash;
    private String from;
    private String to;

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
    public String getTxHash() {
        return txHash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransData> getTransDatas() {
        return transDatas;
    }

    public void setTransDatas(List<TransData> transDatas) {
        this.transDatas = transDatas;
    }
}