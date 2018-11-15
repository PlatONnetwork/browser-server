package com.platon.browser.req.account;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountDetailReq extends PageReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "账户地址不能为空！")
    private String address;
    private String txType;
    // 数据开始日期
    private Date startDate;
    // 数据结束日期
    private Date endDate;
}
