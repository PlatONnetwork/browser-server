package com.platon.browser.req.transaction;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/13 09:45
 * @Description:
 */
@Data
public class TransactionListReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{account.address.notnull}")
    private String address;
     // 记录起始序号
     private long beginSequence;
     // 列表大小
     private int listSize;
}
