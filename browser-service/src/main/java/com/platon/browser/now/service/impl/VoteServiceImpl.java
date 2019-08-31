package com.platon.browser.now.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.entity.VoteExample;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.VoteListResp;
import com.platon.browser.util.BeanConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: 王章雄
 * Email:wangzhangxiong@juzix.net
 * Date: 2019/8/20
 * Time: 20:40
 * Desc:
 */
@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteMapper voteMapper;

    @Override
    public RespPage<VoteListResp> queryByProposal(VoteListRequest voteListRequest) {
        RespPage<VoteListResp> respPage = new RespPage<>();
        respPage.setTotalCount(0);
        respPage.setTotalPages(0);
        Page<?> page = PageHelper.startPage(voteListRequest.getPageNo(), voteListRequest.getPageSize(), true);
        VoteExample voteExample = new VoteExample();
        VoteExample.Criteria criteria = voteExample.createCriteria();
        criteria.andProposalHashEqualTo(voteListRequest.getProposalHash());
        if (StringUtils.isNotBlank(voteListRequest.getOption())) {
            criteria.andOptionEqualTo(voteListRequest.getOption());
        }
        /** 分页根据提案hash查询投票列表 */
        List<Vote> votes = voteMapper.selectByExample(voteExample);
        if (!CollectionUtils.isEmpty(votes)) {
            List<VoteListResp> voteListResps = new ArrayList<>(votes.size());
            votes.forEach(vote -> {
                VoteListResp resp = BeanConvertUtil.beanConvert(vote, VoteListResp.class);
                resp.setVoter(vote.getVerifier());
                resp.setVoterName(vote.getVerifierName());
                resp.setTxHash(voteListRequest.getProposalHash());
                voteListResps.add(resp);
            });
            respPage.setTotalPages(page.getPages());
            respPage.setTotalCount(page.getTotal());
            respPage.setData(voteListResps);
        }
        return respPage;
    }
}
