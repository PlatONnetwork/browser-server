package com.platon.browser.response.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Erc20 持有人对应token列表信息
 *
 * @author AgentRJ
 * @create 2020-09-23 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryHolderTokenListResp {

    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "Token合约")
    private String contract;
    @ApiModelProperty(value = "代币转账金额（单位：1）")
    private BigDecimal balance;
    @ApiModelProperty(value = "代币精度")
    private Integer decimal;
    @ApiModelProperty(value = "代币符号")
    private String symbol;
    @ApiModelProperty(value = "代币名称")
    private String name;
    @ApiModelProperty(value = "地址对应合约交易总数")
    private Integer txCount;

}
