package com.platon.browser.req.account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * 账户详情下载请求对象
 * 账户有两种类型：
 * 1、外部账户：钱包地址
 * 2、内部账户：合约地址
 */
@Data
public class AccountDownloadReq {
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
    @NotBlank(message = "{account.address.notnull}")
    private String address;
    private Date startDate;
    @NotNull(message = "{download.date.start.notnull}")
    @Past(message = "{download.date.start.must.less.than.now}")
    private Date endDate;
}
