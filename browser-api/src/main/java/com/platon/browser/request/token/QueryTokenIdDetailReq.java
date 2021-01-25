package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询toknid列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenIdDetailReq extends PageReq {

    @ApiModelProperty(allowEmptyValue = true, required = false, value = "合约地址")
    private String contract;//

    @ApiModelProperty(allowEmptyValue = true, required = false, value = "tokenId")
    private String tokenId;//
}
