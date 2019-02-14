package com.platon.browser.req.ticket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class TicketListReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{transaction.hash.notnull}")
    private String txHash;
}
