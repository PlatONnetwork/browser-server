package com.platon.browser.res.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Erc20 合约持有人信息
 *
 * @author AgentRJ
 * @create 2020-09-23 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTokenHolderListResp {

    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "持有百分比")
    private String percent;
    @ApiModelProperty(value = "代币转账金额（单位：1）")
    private BigDecimal balance;


}
