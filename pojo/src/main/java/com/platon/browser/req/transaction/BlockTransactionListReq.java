package com.platon.browser.req.transaction;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockTransactionListReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotNull(message = "区块高度不能为空！")
    private Long height;
}
