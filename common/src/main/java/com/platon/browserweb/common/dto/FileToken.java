package com.platon.browserweb.common.dto;

/**
 * User: dongqile
 * Date: 2018/6/27
 * Time: 20:31
 */
public class FileToken {
    private String token;
    private Long ts;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}