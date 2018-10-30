package com.platon.browser.req.transaction;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class TransactionListReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    private Long height;
}
