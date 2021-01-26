package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询toknid列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenIdListReq extends PageReq {

    @ApiModelProperty(allowEmptyValue = true, required = false, value = "合约地址")
    private String contract;//

    @ApiModelProperty(allowEmptyValue = true, required = false, value = "钱包地址")
    private String address;//

    @ApiModelProperty(allowEmptyValue = true, required = false, value = "tokenId")
    private String tokenId;//
}
