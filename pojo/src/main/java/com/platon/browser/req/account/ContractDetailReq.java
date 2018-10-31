package com.platon.browser.req.account;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContractDetailReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "合约地址不能为空！")
    private String address;
    private String txType;
}
