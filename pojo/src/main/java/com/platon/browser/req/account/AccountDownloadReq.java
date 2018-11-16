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
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "账户地址不能为空！")
    private String address;
    private Date startDate;
    @NotNull(message = "数据日期不能为空！")
    @Past(message = "数据日期必须小于等于当前日期！")
    private Date endDate;
}
