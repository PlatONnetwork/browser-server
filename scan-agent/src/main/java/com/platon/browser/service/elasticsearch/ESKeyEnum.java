package com.platon.browser.service.elasticsearch;

/**
 * es 索引 方便日志打印
 *
 * @date 2021/5/21
 */
public enum ESKeyEnum {

    Block("Block"),
    Transaction("Transaction"),
    NodeOpt("NodeOpt"),
    DelegateReward("DelegateReward"),
    Erc20Tx("Erc20Tx"),
    Erc721Tx("Erc721Tx");

    private String key;

    ESKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
