package com.platon.browser.dao.custommapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.platon.browser.dao.entity.Proposal;

import java.util.List;

public interface CustomProposalMapper {
    int updateProposalBasicInfo(List <Proposal> list);
    int updateProposalTallyInfo(List <Proposal> list);

    List<Proposal> selectVotingProposal(String nodeId);

    /**
     * 列出所有未结束的提案，提案的状态有(参考：x/gov/proposals.go)
     * 	Voting    ProposalStatus = 0x01
     * 	Pass      ProposalStatus = 0x02
     * 	Failed    ProposalStatus = 0x03
     * 	PreActive ProposalStatus = 0x04
     * 	Active    ProposalStatus = 0x05
     * 	Canceled  ProposalStatus = 0x06
     *
     * 	对于升级提案来说，Voting是处于投票状态，其它状态都不可投票；failed/active/canceled是结束状态，可以查询到TallyResult（PreActive状态时，也可以查询到TallyResult）
     * 	对其它类型的提案来说，Voting是处于投票状态；pass/failed处于结束状态，可以查询到TallyResult
     *
     * @return
     */
    List<Proposal> listUnfinished();
}
