package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 提案信息更新任务
 */
@Slf4j
@Component
public class ProposalInfoTask {
    @Autowired
    private PlatOnClient client;
    @Autowired
    private SpecialApi sca;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private CustomProposalMapper customProposalMapper;

    /**
     *
     */
    @Scheduled(cron = "0/15  * * * * ?")
    public void cron () {
        // 只有程序正常运行才执行任务
        if(AppStatusUtil.isRunning()) start();
    }

    protected void start()  {

        //数据库获取信息未完成同步信息的提案
        ProposalExample proposalExample = new ProposalExample();
        //针对提案信息只需要更新状态为
        //1.投票中
        //2.预升级
        //3.已通过
        List <Integer> statusList = new ArrayList <>();
        statusList.add(CustomProposal.StatusEnum.VOTING.getCode());
        statusList.add(CustomProposal.StatusEnum.PRE_UPGRADE.getCode());
        statusList.add(CustomProposal.StatusEnum.PASS.getCode());
        proposalExample.createCriteria().andStatusIn(statusList);
        List <Proposal> proposals = proposalMapper.selectByExample(proposalExample);
        //如果已经补充则无需补充
        if (proposals.isEmpty()) return;

        for (Proposal proposal : proposals) {

            try {
                //发送rpc请求查询提案结果
                ProposalParticiantStat pps = getProposalParticipantStat(proposal.getHash(), networkStatCache.getNetworkStat().getCurBlockHash());
                //设置参与人数
                if (pps.getVoterCount() != null && !pps.getVoterCount().equals(proposal.getAccuVerifiers())) {
                    // 有变更
                    proposal.setAccuVerifiers(pps.getVoterCount());
                }
                //设置赞成票
                if (pps.getSupportCount() != null && !pps.getSupportCount().equals(proposal.getYeas())) {
                    // 有变更
                    proposal.setYeas(pps.getSupportCount());
                }

                //设置反对票
                if (pps.getOpposeCount() != null && !pps.getOpposeCount().equals(proposal.getNays())) {
                    // 有变更
                    proposal.setNays(pps.getOpposeCount());
                }

                //设置弃权票
                if (pps.getAbstainCount() != null && !pps.getAbstainCount().equals(proposal.getAbstentions())) {
                    // 有变更
                    proposal.setAbstentions(pps.getAbstainCount());
                }

                //只有在结束快高之后才有返回提案结果
                if (networkStatCache.getNetworkStat().getCurNumber() >= proposal.getEndVotingBlock()) {
                    //设置状态
                    int status = getTallyResult(proposal.getHash()).getStatus();
                    if (status != proposal.getStatus()) {
                        // 有变更
                        proposal.setStatus(status);
                    }
                }
            }catch (Exception e){
                log.error("提案投票信息更新出错:",e);
            }
            customProposalMapper.updateProposalInfoList(proposals);
        }
    }



    /**
     * 取提案参与者统计信息
     *
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    private ProposalParticiantStat getProposalParticipantStat ( String proposalHash, String blockHash ) throws ContractInvokeException, BlankResponseException {
        return sca.getProposalParticipants(client.getWeb3jWrapper().getWeb3j(), proposalHash, blockHash);
    }

    /**
     * 根据提案hash取提案投票结果
     *
     * @param proposalHash
     * @return
     * @throws Exception
     */
    private TallyResult getTallyResult ( String proposalHash ) throws Exception {
        BaseResponse <TallyResult> result = client.getProposalContract().getTallyResult(proposalHash).send();
        if (result.isStatusOk()) {
            return result.data;
        }
        throw new ContractInvokeException("查询不到提案[proposalHash=" + proposalHash + "]对应的投票结果!");
    }

}
