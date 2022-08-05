package com.platon.browser.response.transaction;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Arc1155Param {

    /**
     * 合约地址（也是交易to地址）
     */
    private String contract;


    /**
     * erc1155的标识符
     */
    private String tokenId;

    /**
     * 合约名称
     */
    private String name;

    /**
     * 精度
     */
    private Integer decimal;

    /**
     * 图片
     */
    private String image;

    private String from;

    /**
     * 发送方类型
     */
    private Integer fromType;

    private String to;

    /**
     * 接收方类型
     */
    private Integer toType;

    /**
     * 交易value
     */
    private String value;

}
