package com.platon.browser.req.transaction;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionPageReq extends PageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    private Long height;
}
