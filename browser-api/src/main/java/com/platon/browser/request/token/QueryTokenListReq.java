package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import lombok.Data;

/**
 * 查询合约列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenListReq extends PageReq {

    private String type;//查询类型  erc20,erc721
}
