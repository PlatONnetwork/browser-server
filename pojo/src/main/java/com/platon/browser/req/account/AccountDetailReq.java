package com.platon.browser.req.account;

import com.platon.browser.common.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 账户详情查询请求对象
 * 账户有两种类型：
 * 1、外部账户：钱包地址
 * 2、内部账户：合约地址
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountDetailReq extends PageReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{address.notnull}")
    private String address;
    private String txType;
    // 数据开始日期
    private Date startDate;
    // 数据结束日期
    private Date endDate;
}
