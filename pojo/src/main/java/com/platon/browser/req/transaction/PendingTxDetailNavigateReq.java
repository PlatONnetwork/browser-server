package com.platon.browser.req.transaction;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = false)
public class PendingTxDetailNavigateReq extends PageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotNull(message = "{transaction.index.notnull}")
    private Integer index;
    @NotBlank(message = "{navigate.direction.notnull}")
    @Pattern(regexp = "prev|next", message = "{direction.illegal}")
    private String direction;
}
