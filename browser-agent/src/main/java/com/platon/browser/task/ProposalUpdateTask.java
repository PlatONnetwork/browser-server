package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.AccuVerifiersCount;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.util.MarkDownParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

import java.io.IOException;

import static com.platon.browser.engine.BlockChain.PROPOSALS_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 提案信息更新任务
 */
@Component
public class ProposalUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpdateTask.class);
    @Autowired
    private PlatonClient platonClient;
    public static final String QUERY_FLAG = "inquiry";
    @Autowired
    private BlockChain blockChain;

    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/1 * * * * ?")
    private void cron(){start();}

    public void start () {
        //获取全量数据
        ProposalCache proposalCache = PROPOSALS_CACHE;
        if(proposalCache.getAllProposal().size() == 0)return;
        for (CustomProposal proposal : proposalCache.getAllProposal()) {//如果已经补充则无需补充
            try {
                String fileUrl = MarkDownParserUtil.acquireMD(proposal.getUrl());
                if(fileUrl==null) throw new BusinessException("获取不到"+proposal.getUrl());
                String proposalMarkString = MarkDownParserUtil.parserMD(fileUrl);
                ProposalMarkDownDto proposalMarkDownDto = JSON.parseObject(proposalMarkString, ProposalMarkDownDto.class);
                proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                if (CustomProposal.TypeEnum.CANCEL.code.equals(proposal.getType())) {
                    proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                    //若是取消提案，则需要补充被取消提案相关信息
/*                    String cancelProposalString = MarkDownParserUtil.parserMD(proposalCache.getProposal(proposal.getHash()).getUrl());
                    ProposalMarkDownDto cancelProp = JSON.parseObject(cancelProposalString, ProposalMarkDownDto.class);*/
                    proposalCache.getProposal(proposal.getCanceledPipId()).getTopic();
                    proposal.setCanceledTopic(proposalCache.getProposal(proposal.getCanceledPipId()).getTopic());
                }
                // 添加至全量缓存
                PROPOSALS_CACHE.addProposal(proposal);
                // 暂存至待入库列表
                STAGE_DATA.getProposalStage().updateProposal(proposal);
            }catch (NoSuchBeanException | BusinessException e){
                logger.error("更新提案({})的主题和描述出错:{}", proposal.getPipId(), e.getMessage());
            } catch (IOException e) {
                logger.error("更新提案({})的主题和描述出错:获取不到{}", proposal.getPipId(), proposal.getUrl());
            }
            //需要更新的提案结果，查询类型1.投票中 2.预升级
            if (proposal.getStatus().equals(CustomProposal.StatusEnum.VOTING.code) || proposal.getStatus().equals(CustomProposal.StatusEnum.PRE_UPGRADE.code)
            || proposal.getStatus().equals(CustomProposal.StatusEnum.PASS.code)) {
                //发送rpc请求查询提案结果
                try {
                    AccuVerifiersCount accuVerifiersCount = new AccuVerifiersCount();
                    BaseResponse voteInfo = SpecialContractApi.getProposalAccuVerifiers(platonClient.getWeb3j(),proposal.getHash(),blockChain.getCurBlock().getHash());
                    if(null!=voteInfo.data){
                        accuVerifiersCount = subString(voteInfo.data.toString());
                    }
                    //设置参与人数
                    proposal.setAccuVerifiers(accuVerifiersCount.getAccuVerifiers().longValue());
                    //设置赞成票
                    proposal.setYeas(accuVerifiersCount.getYeas().longValue());
                    //设置反对票
                    proposal.setNays(accuVerifiersCount.getNays().longValue());
                    //设置弃权票
                    proposal.setAbstentions(accuVerifiersCount.getAbstentions().longValue());
                    //只有在结束快高之后才有返回提案结果
                    if(blockChain.getCurBlock().getBlockNumber().longValue()>=Long.valueOf(proposal.getEndVotingBlock()).longValue()){
                        BaseResponse <TallyResult> result = platonClient.getProposalContract().getTallyResult(proposal.getHash()).send();
                        //设置状态
                        proposal.setStatus(result.data.getStatus());
                    }
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


    private static AccuVerifiersCount subString(String beforeString){
        String afterString = beforeString.substring(1,beforeString.length()-1);
        String[] afterList = afterString.split(",");
        AccuVerifiersCount accuVerifiersCount = new AccuVerifiersCount();
        accuVerifiersCount.init(afterList[0],afterList[1],afterList[2],afterList[3]);
        return accuVerifiersCount;
    }
}
