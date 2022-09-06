package com.platon.browser.request.token;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 查询toknid列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenIdDetailReq {

    @NotBlank(message = "合约不能为空")
    private String contract;

    @NotBlank(message = "tokenID不能为空")
    private String tokenId;

}
