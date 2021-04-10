package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User: chendongming
 * Date: 2020/01/02
 * Time: 14:58
 * tyType=5000 领取委托奖励
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class Erc20Param extends TxParam{
    private String innerFrom; //内部交易from
    private String innerTo; //内部交易to
    private String innerValue; //内部交易value
    private String innerContractAddr; //内部交易对应地址
    private String innerContractName; //内部交易对应名称
    private String innerSymbol; //内部交易对应单位
    private String innerDecimal;    // 合约精度
}
