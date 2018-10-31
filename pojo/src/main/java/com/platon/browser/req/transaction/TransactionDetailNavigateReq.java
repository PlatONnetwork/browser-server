package com.platon.browser.req.transaction;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Data
public class TransactionDetailNavigateReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "交易Hash不能为空！")
    private String txHash;
    @NotBlank(message = "浏览方向不能为空！")
    @Pattern(regexp = "prev|next", message = "方向取值不合法！")
    private String direction;
}
