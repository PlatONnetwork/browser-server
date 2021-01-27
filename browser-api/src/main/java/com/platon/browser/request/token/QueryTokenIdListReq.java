package com.platon.browser.request.token;

import com.platon.browser.request.PageReq;
import lombok.Data;


/**
 * 查询toknid列表请求参数
 *
 * @author AgentRJ
 * @create 2020-09-23 14:36
 */
@Data
public class QueryTokenIdListReq extends PageReq {

    private String contract;//

    private String address;//

    private String tokenId;//
}
