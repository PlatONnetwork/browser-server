package com.platon.browser.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class Arc721Param {
    private String innerFrom; //内部交易from
    private String innerTo; //内部交易to
    private String innerValue; //内部交易value
    private String innerContractAddr; //内部交易对应地址
    private String innerContractName; //内部交易对应名称
    private String innerSymbol; //内部交易对应单位
    private String innerDecimal;    // 合约精度
    private String innerImage; //图片
}
