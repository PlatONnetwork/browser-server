package com.platon.browser.now.service;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;

import java.util.List;

/**
 * Token 服务接口定义
 *
 * @Author: AgentRJ
 * @Date: 2020/9/23
 * @Version 1.0
 */
public interface Erc20TokenService {

    /**
     * 查询Token代币列表
     *
     * @param req 检索参数
     * @return 满足查询参数的代币列表
     */
    RespPage<QueryTokenListResp> queryTokenList(QueryTokenListReq req);

    /**
     * 根据合约地址查询详细的代币信息
     *
     * @param req 检索参数
     * @return 代币详情数据
     */
    QueryTokenDetailResp queryTokenDetail(QueryTokenDetailReq req);

    /**
     * 保存Token记录
     *
     * @param token token 记录
     * @return
     */
    int save(Erc20Token token);

    /**
     * 批量保存token记录
     *
     * @param list 批量数据
     * @return
     */
    int batchSave(List<Erc20Token> list);
}
