package com.platon.browser.task;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticipantStat;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.service.proposal.ProposalService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.sdk.contracts.ppos.dto.resp.TallyResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
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
    private NetworkStatCache networkStatCache;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private CustomProposalMapper customProposalMapper;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private PlatOnClient platOnClient;

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

        BigInteger bigInteger = BigInteger.ZERO;
        try {
			bigInteger = platOnClient.getWeb3jWrapper().getWeb3j().platonBlockNumber().send().getBlockNumber();
		} catch (IOException e1) {
			log.error("get blocknumber error");
		}
        for (Proposal proposal : proposals) {
        	/**
        	 * 当区块号小于结束区块则跳过
        	 */
        	if(bigInteger.compareTo(BigInteger.valueOf(proposal.getEndVotingBlock())) >= 0) {
        		continue;
        	}
            try {
//                //发送rpc请求查询提案结果
                ProposalParticipantStat pps = proposalService.getProposalParticipantStat(proposal.getHash(), networkStatCache.getNetworkStat().getCurBlockHash());
                //设置参与人数
                if (pps.getVoterCount() != null && !pps.getVoterCount().equals(proposal.getAccuVerifiers())) {
                    // 有变更
                    proposal.setAccuVerifiers(pps.getVoterCount());
                }
//                //设置赞成票
//                if (pps.getSupportCount() != null && !pps.getSupportCount().equals(proposal.getYeas())) {
//                    // 有变更
//                    proposal.setYeas(pps.getSupportCount());
//                }
//
//                //设置反对票
//                if (pps.getOpposeCount() != null && !pps.getOpposeCount().equals(proposal.getNays())) {
//                    // 有变更
//                    proposal.setNays(pps.getOpposeCount());
//                }
//
//                //设置弃权票
//                if (pps.getAbstainCount() != null && !pps.getAbstainCount().equals(proposal.getAbstentions())) {
//                    // 有变更
//                    proposal.setAbstentions(pps.getAbstainCount());
//                }

                TallyResult tallyResult = proposalService.getTallyResult(proposal.getHash());
                if(tallyResult != null) {
                	//设置状态
                    int status = tallyResult.getStatus();
                    if (status != proposal.getStatus()) {
                        // 有变更
                        proposal.setStatus(status);
                    }
                }
            }catch (Exception e){
                log.error("提案投票信息更新出错:",e);
            }
        }
        customProposalMapper.updateProposalInfoList(proposals);
    }
}
