package com.platon.browser.task;

import com.platon.browser.bean.CustomProposal;
import com.platon.browser.bean.ProposalParticipantStat;
import com.platon.browser.dao.custommapper.CustomProposalMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.service.StatisticCacheService;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 提案信息更新任务
 *
 * @date: 2021/11/30
 */
@Slf4j
@Component
public class ProposalInfoTask {

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private CustomProposalMapper customProposalMapper;

    @Resource
    private ProposalService proposalService;

    @Resource
    private NetworkStatMapper networkStatMapper;

    @Resource
    private StatisticCacheService statisticCacheService;

    @XxlJob("proposalInfoJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void proposalInfo() {
        try {
            if (AppStatusUtil.isRunning()) start();
        } catch (Exception e) {
            log.error("提案投票信息更新异常", e);
            throw e;
        }
    }

    protected void start() {
        //数据库获取信息未完成同步信息的提案
        ProposalExample proposalExample = new ProposalExample();
        //针对提案信息只需要更新状态为
        //1.投票中
        //2.预升级
        //3.已通过
        List<Integer> statusList = new ArrayList<>();
        statusList.add(CustomProposal.StatusEnum.VOTING.getCode());
        statusList.add(CustomProposal.StatusEnum.PRE_UPGRADE.getCode());
        statusList.add(CustomProposal.StatusEnum.PASS.getCode());
        proposalExample.createCriteria().andStatusIn(statusList);
        List<Proposal> proposals = proposalMapper.selectByExample(proposalExample);
        //如果已经补充则无需补充
        if (proposals.isEmpty()) return;
        for (Proposal proposal : proposals) {
            try {
                NetworkStat networkStatCache = statisticCacheService.getNetworkStatCache();
                //发送rpc请求查询提案结果
                ProposalParticipantStat pps = proposalService.getProposalParticipantStat(proposal.getHash(), networkStatCache.getCurBlockHash());
                //设置参与人数
                if (pps.getVoterCount() != null && !pps.getVoterCount().equals(proposal.getAccuVerifiers())) {
                    // 有变更
                    proposal.setAccuVerifiers(pps.getVoterCount());
                }
                /**
                 * 当同步区块号小于结束区块且相应的取消提案未成功时候则跳过更新状态，防止追块时候提案提前结束造成数据错误
                 */
                List<NetworkStat> networkStat = networkStatMapper.selectByExample(null);
                ProposalExample pe = new ProposalExample();
                proposalExample.createCriteria().andCanceledPipIdEqualTo(proposal.getHash());
                proposalExample.createCriteria().andStatusEqualTo(CustomProposal.StatusEnum.PASS.getCode());
                List<Proposal> ppsList = proposalMapper.selectByExample(pe);
                if (networkStat.get(0).getCurNumber() < proposal.getEndVotingBlock() && ppsList.size() == 0) {
                    continue;
                }
                TallyResult tallyResult = proposalService.getTallyResult(proposal.getHash());
                if (tallyResult != null) {
                    //设置状态
                    int status = tallyResult.getStatus();
                    if (status != proposal.getStatus()) {
                        // 有变更
                        proposal.setStatus(status);
                    }
                }
            } catch (Exception e) {
                log.error("提案投票信息更新出错:", e);
            }
        }
        customProposalMapper.updateProposalInfoList(proposals);
        XxlJobHelper.handleSuccess("提案投票信息更新成功");
    }

}
