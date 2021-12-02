package com.platon.browser.service.proposal;

import com.platon.browser.bean.CustomProposal;
import com.platon.browser.bean.CustomVote.OptionEnum;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.entity.VoteExample;
import com.platon.browser.dao.entity.VoteExample.Criteria;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.dao.param.ppos.ProposalSlash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 惩罚之后更新提案参数服务
 */
@Slf4j
@Service
public class ProposalParameterService {

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private VoteMapper voteMapper;

    @Resource
    private ProposalBusinessMapper proposalBusinessMapper;

    /**
     * 对相应的节点投票进行判断处理
     *
     * @return
     */
    public void setSlashParameters(String nodeId) {
        long startTime = System.currentTimeMillis();
        /**
         * 根据nodeId且不为无效票查看是否存在待结束的投票数据
         */
        VoteExample voteExample = new VoteExample();
        Criteria credentials = voteExample.createCriteria();
        credentials.andNodeIdEqualTo(nodeId);
        List<Integer> options = new ArrayList<>();
        options.add(Integer.valueOf(OptionEnum.SUPPORT.getCode()));
        options.add(Integer.valueOf(OptionEnum.ABSTENTION.getCode()));
        options.add(Integer.valueOf(OptionEnum.OPPOSITION.getCode()));
        credentials.andOptionIn(options);
        List<Vote> votes = voteMapper.selectByExample(voteExample);
        /**
         * 没有投票情况直接返回
         */
        if (votes == null | votes.size() == 0) {
            log.debug("nodeId:{} not hava a vote", nodeId);
        }
        List<ProposalSlash> proposalSlashs = new ArrayList<>();
        votes.forEach(vote -> {
            /**
             * 如果投票已经为无效了，则直接跳过
             */
            Proposal proposal = proposalMapper.selectByPrimaryKey(vote.getProposalHash());
            if (proposal.getStatus().intValue() == CustomProposal.StatusEnum.VOTING.getCode()) {
                /**
                 * 如果提案在投票中则需要更新该投票为无效票，提案数要减少
                 */
                ProposalSlash proposalSlash = new ProposalSlash();
                proposalSlash.setVoteHash(vote.getHash());
                proposalSlash.setHash(proposal.getHash());
                proposalSlash.setVoteOption(String.valueOf(vote.getOption()));
                proposalSlashs.add(proposalSlash);
            } else {
                log.debug("nodeId:{} ,proposal hash:{}, not voting", nodeId, proposal);
            }
        });
        if (!proposalSlashs.isEmpty()) {
            proposalBusinessMapper.proposalSlashUpdate(proposalSlashs);
        }
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
