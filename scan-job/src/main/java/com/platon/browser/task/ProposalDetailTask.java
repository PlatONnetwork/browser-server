package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.bean.ProposalMarkDownDto;
import com.platon.browser.dao.custommapper.CustomProposalMapper;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.MarkDownParserUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 提案信息更新任务
 */
@Slf4j
@Component
public class ProposalDetailTask {

    @Resource
    private CustomProposalMapper customProposalMapper;

    @Resource
    private ProposalMapper proposalMapper;

    /**
     * 1.查询数据库未同步完成的提案信息
     * 2.根据proposalId查询keybase上信息
     * 3.查询到的信息更新并修改数据库
     */
    @XxlJob("proposalDetailUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void proposalDetail() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning()) start();
    }

    protected void start() {
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
            customProposalMapper.updateProposalDetailList(proposals);
            XxlJobHelper.handleSuccess("更新提案详情定时任务完成");
        } catch (Exception e) {
            log.error("更新提案详情定时任务异常", e);
            throw e;
        }
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
