package com.platon.browser.request.token;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询合约详情请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
public class QueryTokenDetailReq {

    @NotBlank(message = "{address required}")
    @Size(min = 42, max = 42)
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
