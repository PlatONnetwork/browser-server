package com.platon.browser.now.service;

import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.ProposalDetailsResp;
import com.platon.browser.res.proposal.ProposalListResp;

/**
 * Created with IntelliJ IDEA.
 * User: 王章雄
 * Email:wangzhangxiong@juzix.net
 * Date: 2019/8/20
 * Time: 15:34
 * Desc:
 */
public interface ProposalService {
    /**
     * 获取提案列表
     * @param req
     * @return
     */
    RespPage<ProposalListResp> list(PageReq req);

    /**
     * 获取单个提案
     * @param req
     * @return
     */
    BaseResp<ProposalDetailsResp> get(ProposalDetailRequest req);

}
