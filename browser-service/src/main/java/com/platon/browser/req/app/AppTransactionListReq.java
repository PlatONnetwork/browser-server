package com.platon.browser.req.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/13 09:45
 * @Description:
 */
@Data
public class AppTransactionListReq {
    @NotEmpty(message = "{transaction.address.notnull}")
    private List<String> walletAddrs;
     // 记录起始序号
     private long beginSequence;
     // 列表大小
     private int listSize;
    @NotBlank(message = "{transaction.direction.notnull}")
     private String direction;
}
