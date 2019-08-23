package com.platon.browser.now.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.proposal.VoteListResp;

/**
 * Created with IntelliJ IDEA.
 * User: 王章雄
 * Email:wangzhangxiong@juzix.net
 * Date: 2019/8/20
 * Time: 20:34
 * Desc:
 */
public interface VoteService {
    /**
     *
     * @param voteListRequest
     * @return
     */
    RespPage<VoteListResp> queryByProposal(VoteListRequest voteListRequest);
}
