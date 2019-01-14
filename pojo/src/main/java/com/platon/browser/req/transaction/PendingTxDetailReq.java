package com.platon.browser.req.transaction;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class PendingTxDetailReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{transaction.hash.notnull}")
    private String txHash;
}
