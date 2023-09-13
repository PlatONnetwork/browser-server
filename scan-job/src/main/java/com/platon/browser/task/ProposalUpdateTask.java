package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.bean.ProposalMarkDownDto;
import com.platon.browser.bean.ProposalParticipantStat;
import com.platon.browser.dao.custommapper.CustomProposalMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.MarkDownParserUtil;
import com.platon.browser.utils.TaskUtil;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ProposalUpdateTask {
    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private CustomProposalMapper customProposalMapper;

    @Resource
    private ProposalService proposalService;

    @Resource
    private NetworkStatMapper networkStatMapper;

    @XxlJob("proposalUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void proposalUpdate() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        log.debug("开始执行:更新提案任务");
        StopWatch watch = new StopWatch();
        watch.start("更新提案基本信息任务");
        updateProposalBasicInfo();
        watch.stop();
        watch.start("更新提案投票情况任务");
        updateProposalTallyInfo();
        watch.stop();
        log.debug("结束执行:更新提案任务, 耗时统计：{}", watch.prettyPrint());
    }


    private void updateProposalBasicInfo() {
        try {
            //数据库获取信息未完成同步信息的提案
            ProposalExample proposalExample = new ProposalExample();
            proposalExample.createCriteria().andCompletionFlagEqualTo(CustomProposal.FlagEnum.INCOMPLETE.getCode());
            List<Proposal> proposals = proposalMapper.selectByExample(proposalExample);
            //如果已经补充则无需补充
            if (proposals.isEmpty()) return;

            for (Proposal proposal : proposals) {
                try {
                    ProposalMarkDownDto resp = getMarkdownInfo(proposal.getUrl());
                    proposal.setTopic(resp.getTopic());
                    proposal.setDescription(resp.getDescription());
                    if (CustomProposal.TypeEnum.CANCEL.getCode() == proposal.getType()) {
                        //补充对应被取消的提案相关信息
                        Proposal cp = proposalMapper.selectByPrimaryKey(proposal.getCanceledPipId());
                        proposal.setCanceledTopic(cp.getTopic());
                    }
                } catch (Exception e) {
                    log.error("更新提案(proposal={})出错: {}", proposal.getHash(), e.getMessage());
                    continue;
                }
                //将同步完成的proposal信息修改状态已完成
                proposal.setCompletionFlag(CustomProposal.FlagEnum.COMPLETE.getCode());
            }
            customProposalMapper.updateProposalBasicInfo(proposals);
            XxlJobHelper.handleSuccess("更新提案详情定时任务完成");
        } catch (Exception e) {
            log.error("更新提案详情定时任务异常", e);
            throw e;
        }
    }

    private void updateProposalTallyInfo(){
        List<Proposal> unfinishedList = customProposalMapper.listUnfinished();
        List<NetworkStat> networkStat = networkStatMapper.selectByExample(null);
        if(networkStat.size()==0){
            return ;
        }
        Long currentBlockNumber = networkStat.get(0).getCurNumber();
        String currentBlockHash = networkStat.get(0).getCurBlockHash();
        for (Proposal proposal : unfinishedList) {
            // 投票已经结束，不能获取投票人员情况
            // 在底层，投票人员情况是暂存在localDb，而不存链上
            // 统计投票后，将清理localDb的投票人员情况，并把统计结果存到链上
            // 注意：特殊节点将不会清理localDb
            TallyResult tallyResult = null;
            try {
                tallyResult = proposalService.getTallyResult(proposal.getHash());
            } catch (Exception e) {
                log.error("获取提案结果出错", e);
            }
            if(tallyResult!=null){
                // 投票的结果信息
                proposal.setStatus(tallyResult.getStatus());
                proposal.setYeas(tallyResult.getYeas().longValue());
                proposal.setNays(tallyResult.getNays().longValue());
                proposal.setAbstentions(tallyResult.getAbstentions().longValue());
                if(proposal.getAccuVerifiers()==0){
                    proposal.setAccuVerifiers(proposal.getYeas()+proposal.getNays() + proposal.getAbstentions());
                }
            }else{
                try {
                    ProposalParticipantStat pps = proposalService.getProposalParticipantStat(proposal.getHash(), currentBlockHash);
                    //设置参与人数
                    if (pps.getAccuVerifierAccount() != null && !pps.getAccuVerifierAccount().equals(proposal.getAccuVerifiers())) {
                        TaskUtil.console("当前块高[{}],提案结束块高[{}],提案投票[{}]的验证人总数[{}]->[{}]更新",
                                networkStat.get(0).getCurNumber(),
                                proposal.getEndVotingBlock(),
                                proposal.getHash(),
                                proposal.getAccuVerifiers(),
                                pps.getAccuVerifierAccount());
                        // 投票的动态信息
                        proposal.setYeas(pps.getYeas());
                        proposal.setNays(pps.getNays());
                        proposal.setAbstentions(pps.getAbstentions());
                        proposal.setAccuVerifiers(pps.getAccuVerifierAccount());
                    }
                } catch ( Exception e) {
                    log.error("获取提案可投票人数出错", e);
                }
            }
        }

        customProposalMapper.updateProposalTallyInfo(unfinishedList);
    }

    /**
     * 根据URL获取markdown信息
     *
     * @param url
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    private ProposalMarkDownDto getMarkdownInfo(String url) throws HttpRequestException {
        try {
            String fileUrl = MarkDownParserUtil.acquireMD(url);
            if (fileUrl == null) throw new BusinessException("获取不到" + url);
            String proposalMarkString = MarkDownParserUtil.parserMD(fileUrl);
            return JSON.parseObject(proposalMarkString, ProposalMarkDownDto.class);
        } catch (Exception e) {
            throw new HttpRequestException(e.getMessage());
        }
    }

}
