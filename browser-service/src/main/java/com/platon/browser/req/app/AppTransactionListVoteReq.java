package com.platon.browser.req.app;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/13 09:45
 * @Description:
 */
@Data
public class AppTransactionListVoteReq {
    @NotBlank(message = "{node.id.notnull}")
    private String nodeId;
     // 记录起始序号
     private long beginSequence;
     // 列表大小
     private int listSize;
     private List<String> walletAddrs;
}
