package com.platon.browser.reqest.token;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询合约详情请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
public class QueryTokenDetailReq {

    @ApiModelProperty(allowEmptyValue = false, required = true, value = "合约地址")
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
