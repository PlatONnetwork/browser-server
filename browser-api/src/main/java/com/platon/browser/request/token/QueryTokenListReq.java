package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 查询合约列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenListReq extends PageReq {

    @ApiModelProperty(allowEmptyValue = false, required = true, value = "查询类型")
    @NotBlank(message = "{type required}")
    @Size(min = 0, max = 10)
    private String type;//查询类型  erc20,erc721
}
