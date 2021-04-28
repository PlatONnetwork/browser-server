package com.platon.browser.task;

import com.platon.browser.bean.ProposalParticipantStat;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.bean.CustomProposal;
import com.platon.contracts.ppos.dto.resp.TallyResult;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    @Resource
    private NetworkStatCache networkStatCache;
    @Resource
    private ProposalMapper proposalMapper;
    @Resource
    private CustomProposalMapper customProposalMapper;
    @Resource
    private ProposalService proposalService;
    @Resource
	private NetworkStatMapper networkStatMapper;
    @Resource
	private BlockChainConfig blockChainConfig;

    /**
     *
     */
    @Scheduled(cron = "0/15  * * * * ?")
    public void proposalInfo () {
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

                /**
            	 * 当同步区块号小于结束区块且相应的取消提案未成功时候则跳过更新状态，防止追块时候提案提前结束造成数据错误
            	 */
                List<NetworkStat> networkStat = networkStatMapper.selectByExample(null);
                ProposalExample pe = new ProposalExample();
                proposalExample.createCriteria().andCanceledPipIdEqualTo(proposal.getHash());
                proposalExample.createCriteria().andStatusEqualTo(CustomProposal.StatusEnum.PASS.getCode());
                List <Proposal> ppsList = proposalMapper.selectByExample(pe);
                
//                BigDecimal total = new BigDecimal(proposal.getYeas())
//                		.add(new BigDecimal(proposal.getAbstentions())).add(new BigDecimal(proposal.getNays()));
//                BigDecimal yes = new BigDecimal(proposal.getYeas()).divide(total, 2, RoundingMode.CEILING);
//                BigDecimal acc = total.divide(new BigDecimal(proposal.getAccuVerifiers()), 2, RoundingMode.CEILING);
            	if(networkStat.get(0).getCurNumber() < proposal.getEndVotingBlock() && ppsList.size() == 0) {
//            		if(proposal.getType().intValue() == CustomProposal.TypeEnum.TEXT.getCode()) {
//            			if(yes.compareTo(blockChainConfig.getMinProposalTextSupportRate()) < 0 
//            					||acc.compareTo(blockChainConfig.getMinProposalTextParticipationRate()) < 0) {
//            				continue;
//            			}
//            		} else if(proposal.getType().intValue() == CustomProposal.TypeEnum.UPGRADE.getCode()) {
//            			if(acc.compareTo(blockChainConfig.getMinProposalUpgradePassRate()) < 0) {
//            				continue;
//            			}
//            		} else if(proposal.getType().intValue() == CustomProposal.TypeEnum.CANCEL.getCode()) {
//            			if(yes.compareTo(blockChainConfig.getMinProposalCancelSupportRate()) < 0 
//            					||acc.compareTo(blockChainConfig.getMinProposalCancelParticipationRate()) < 0) {
//            				continue;
//            			}
//            		} else if(proposal.getType().intValue() == CustomProposal.TypeEnum.PARAMETER.getCode()) {
//            			if(yes.compareTo(blockChainConfig.getParamProposalSupportRate()) < 0 
//            					||acc.compareTo(blockChainConfig.getParamProposalVoteRate()) < 0) {
//            				continue;
//            			}
//            		} 
            		continue;
            	}
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
