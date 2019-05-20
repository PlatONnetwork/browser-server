package com.platon.browser.dto.app.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/20 10:41
 * @Description:
 */
@Data
public class AppTransactionSummaryDto {
    private String nodeId;
    private String hashes;
    private long voteCountSum;
    private String lastVoteTime;
    private long validCountSum;
    private BigDecimal earnings = BigDecimal.ZERO;
    private BigInteger locked = BigInteger.ZERO;
}
