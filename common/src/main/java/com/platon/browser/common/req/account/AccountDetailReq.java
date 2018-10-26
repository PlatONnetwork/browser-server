package com.platon.browser.common.req.account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class AccountDetailReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "账户地址不能为空！")
    private String address;
    private String txType;
}
