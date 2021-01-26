package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * 查询合约内部交易转账列表
 * 条件：
 * 1、从合约维度查询；
 * 2、从地址维度查询
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTokenTransferRecordListReq extends PageReq {

    @ApiModelProperty(value = "合约地址")
    //@Size(min = 42, max = 42)
    private String contract;

    @ApiModelProperty(value = "用户地址（from/to）")
    //@Size(min = 42, max = 42)
    private String address;

    @ApiModelProperty(value = "交易哈希")
    private String txHash;
}
