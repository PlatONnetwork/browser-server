package com.platon.browser.service.redis;

/**
 * redis key 方便日志打印
 *
 * @date 2021/5/21
 */
public enum RedisKeyEnum {

    Block("Block"),
    Transaction("Transaction"),
    Statistic("Statistic"),
    Erc20Tx("Erc20Tx"),
    Erc721Tx("Erc721Tx");

    private String key;

    RedisKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
