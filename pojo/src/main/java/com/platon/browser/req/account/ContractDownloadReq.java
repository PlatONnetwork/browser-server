package com.platon.browser.req.account;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
public class ContractDownloadReq {
    @NotBlank(message = "链ID不能为空！")
    private String cid;
    @NotBlank(message = "账户地址不能为空！")
    private String address;
    @NotNull(message = "数据日期不能为空！")
    @Past(message = "数据日期必须小于等于当前日期！")
    private Date date;
}
