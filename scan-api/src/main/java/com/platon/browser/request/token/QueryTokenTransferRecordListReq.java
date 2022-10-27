package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String contract;

    private String address;

    private String txHash;

    /**
     * token_id
     */
    private String tokenId;

}
