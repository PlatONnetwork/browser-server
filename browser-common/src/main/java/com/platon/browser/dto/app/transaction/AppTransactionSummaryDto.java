package com.platon.browser.dto.app.transaction;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/20 10:41
 * @Description:
 */
@Data
public class AppTransactionSummaryDto {
    private String nodeId;
    private long voteCountSum;
    private String lastVoteTime;
}
