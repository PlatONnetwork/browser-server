package com.platon.browser.response.transaction;

import lombok.Data;

@Data
public class InternalTransferParam {

    /**
     * 内部交易from
     */
    private String from;

    /**
     * 内部交易from
     */
    private String fromType;


    /**
     * 内部交易from类型
     */
    private Integer to;

    /**
     * 内部交易to类型
     */
    private Integer toType;

    /**
     * 内部交易value
     */
    private String value;

}
