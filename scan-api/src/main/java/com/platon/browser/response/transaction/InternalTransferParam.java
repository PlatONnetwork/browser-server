package com.platon.browser.response.transaction;

import lombok.Data;

@Data
public class InternalTransferParam {

    private String hash;

    /**
     * 内部交易from
     */
    private String from;

    /**
     * 内部交易from
     */
    private Integer fromType;


    /**
     * 内部交易from类型
     */
    private String to;

    /**
     * 内部交易to类型
     */
    private Integer toType;

    /**
     * 内部交易value
     */
    private String value;

}
