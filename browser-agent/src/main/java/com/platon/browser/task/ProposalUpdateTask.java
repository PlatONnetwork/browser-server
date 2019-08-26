package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.util.MarkDownParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

import java.util.HashSet;
import java.util.Set;

import static com.platon.browser.engine.BlockChain.PROPOSALS_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 11:04
 */
@Component
public class ProposalUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpdateTask.class);
    @Autowired
    private PlatonClient platonClient;
    public static final String QUERY_FLAG = "inquiry";

    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/1 * * * * ?")
    protected void start () {
        //获取全量数据
        ProposalCache proposalCache = PROPOSALS_CACHE;
        Set <Proposal> updateSet = new HashSet <>();
        for (CustomProposal proposal : proposalCache.getAllProposal()) {//如果已经补充则无需补充
            if (QUERY_FLAG.equals(proposal.getTopic())||QUERY_FLAG.equals(proposal.getDescription())) {
                //只需要补充两种治理类型 1.升级提案 2.取消提案
                try {
                    String proposalMarkString = MarkDownParserUtil.parserMD(proposal.getUrl());
                    ProposalMarkDownDto proposalMarkDownDto = JSON.parseObject(proposalMarkString, ProposalMarkDownDto.class);
                    if (CustomProposal.TypeEnum.UPGRADE.code.equals(proposal.getType())) {
                        proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                    }
                    if (CustomProposal.TypeEnum.CANCEL.code.equals(proposal.getType())) {
                        proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                        //若是取消提案，则需要补充被取消提案相关信息
                        String cancelProposalString = MarkDownParserUtil.parserMD(proposalCache.getProposal(proposal.getHash()).getUrl());
                        ProposalMarkDownDto cancelProp = JSON.parseObject(cancelProposalString, ProposalMarkDownDto.class);
                        proposal.setCanceledTopic(cancelProp.getDescription());
                    }
                    // 添加至全量缓存
                    PROPOSALS_CACHE.addProposal(proposal);
                    // 暂存至待入库列表
                    STAGE_DATA.getProposalStage().updateProposal(proposal);
                } catch (Exception e) {
                    logger.error("更新提案({})的主题和描述出错:{}", proposal.getPipId(),e.getMessage());
                }
            }
            //需要更新的提案结果，查询类型1.投票中 2.预升级
            if (proposal.getStatus().equals(CustomProposal.StatusEnum.VOTING.code) || proposal.getStatus().equals(CustomProposal.StatusEnum.PRE_UPGRADE.code)) {
                //发送rpc请求查询提案结果
                try {
                    BaseResponse<TallyResult> result = platonClient.getProposalContract().getTallyResult(proposal.getHash()).send();
                    //设置赞成票
                    proposal.setYeas(result.data.getYeas().longValue());
                    //设置反对票
                    proposal.setNays(result.data.getNays().longValue());
                    //设置弃权票
                    proposal.setAbstentions(result.data.getAbstentions().longValue());
                    //设置参与人数
                    proposal.setAccuVerifiers(result.data.getAccuVerifiers().longValue());
                    //设置状态
                    proposal.setStatus(result.data.getStatus());
                    // 添加至全量缓存
                    PROPOSALS_CACHE.addProposal(proposal);
                    // 暂存至待入库列表
                    STAGE_DATA.getProposalStage().updateProposal(proposal);
                } catch (Exception e) {
                    logger.error("更新提案({})的结果出错:{}", proposal.getPipId(), e.getMessage());
                }
            }
        }
    }

    public static void main ( String[] args ) {

    }
}
