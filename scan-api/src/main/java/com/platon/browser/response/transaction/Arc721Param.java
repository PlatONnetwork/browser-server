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

    /**
     * 内部交易from
     */
    private String innerFrom;

    /**
     * 内部交易from类型
     */
    private Integer fromType;

    /**
     * 内部交易to
     */
    private String innerTo;

    /**
     * 内部交易to类型
     */
    private Integer toType;

    /**
     * 内部交易value
     */
    private String innerValue;

    /**
     * 内部交易对应地址
     */
    private String innerContractAddr;

    /**
     * 内部交易对应名称
     */
    private String innerContractName;

    /**
     * 内部交易对应单位
     */
    private String innerSymbol;

    /**
     * 合约精度
     */
    private String innerDecimal;

    /**
     * 图片
     */
    private String innerImage;

}
