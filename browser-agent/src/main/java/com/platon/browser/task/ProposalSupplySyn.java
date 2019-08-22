package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalExecute;
import com.platon.browser.util.MarkDownParserUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;
import org.web3j.protocol.core.RemoteCall;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 11:04
 */
@Component
public class ProposalSupplySyn {
    private static Logger logger = LoggerFactory.getLogger(ProposalSupplySyn.class);

    @Autowired
    private BlockChain bc;

    private static boolean taskBeginflag = false;

    private  boolean infoSupply = false;

    private  boolean statusSupply = false;

    @Autowired
    private PlatonClient platonClient;

    public static final String queryFlag = "inquiry";

    @PostConstruct
    private void waitLoad () {
        try {
            //延迟加载，启动时，等待引擎中全量数据加载完毕
            if (bc.PROPOSALS_CACHE.size() > 0) {
                taskBeginflag = true;
            } else {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            logger.error("[ProposalSupplySyn] waitLoad exception!!!");
            e.printStackTrace();
        }
    }

    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/5 * * * * ?")
    protected void infoSyn () {
        try {
            if (taskBeginflag) {
                //获取全量数据
                Map <String, CustomProposal> proposalMap = bc.PROPOSALS_CACHE;
                Set <Proposal> updateSet = new HashSet <>();
                proposalMap.forEach(( pipid, customProposal ) -> {
                    //如果已经补充则无需补充
                    if (customProposal.getTopic().equals(queryFlag) || customProposal.getDescription().equals(queryFlag)) {
                        //只需要补充两种治理类型 1.升级提案 2.取消提案
                        try {
                            String proposalMarkString = MarkDownParserUtil.parserMD(customProposal.getUrl());
                            ProposalMarkDownDto proposalMarkDownDto = JSON.parseObject(proposalMarkString, ProposalMarkDownDto.class);
                            if (customProposal.getType().equals(CustomProposal.TypeEnum.UPGRADE.code)  ) {
                                customProposal.updateInfoWithProposal(proposalMarkDownDto);
                            }
                            if(customProposal.getType().equals(CustomProposal.TypeEnum.CANCEL.code)){
                                customProposal.updateInfoWithProposal(proposalMarkDownDto);
                                //若是取消提案，则需要补充被取消提案相关信息
                                String cancelProposalString = MarkDownParserUtil.parserMD(proposalMap.get(customProposal.getCanceledPipId()).getUrl());
                                ProposalMarkDownDto cancelProp = JSON.parseObject(cancelProposalString,ProposalMarkDownDto.class);
                                customProposal.setCanceledTopic(cancelProp.getDescription());
                            }
                        }catch (Exception e){
                            logger.error("[ProposalSupplySyn] update proposal info exception {}",e.getMessage());
                        }
                        infoSupply = true;
                        bc.PROPOSALS_CACHE.put(customProposal.getPipId().toString(),customProposal);

                    }
                    //需要更新的提案结果，查询类型1.投票中 2.预升级
                    if (customProposal.getStatus().equals(CustomProposal.StatusEnum.VOTEING.code) || customProposal.getStatus().equals(CustomProposal.StatusEnum.PREUPGRADE.code)) {
                        //发送rpc请求查询提案结果
                        try {
                            BaseResponse <TallyResult> result = platonClient.getProposalContract().getTallyResult(customProposal.getPipId().toString()).send();
                            //设置赞成票
                            customProposal.setYeas(result.data.getYeas().longValue());
                            //设置反对票
                            customProposal.setNays(result.data.getNays().longValue());
                            //设置弃权票
                            customProposal.setAbstentions(result.data.getAbstentions().longValue());
                            //设置参与人数
                            customProposal.setAccuVerifiers(result.data.getAccuVerifiers().longValue());
                            //设置状态
                            customProposal.setStatus(result.data.getStatus());
                        } catch (Exception e) {
                            logger.error("[ProposalSupplySyn] infoSyn send rpc fail {}", e.getMessage());
                        }
                        statusSupply = true;
                        bc.PROPOSALS_CACHE.put(customProposal.getPipId().toString(),customProposal);

                    }
                    if(infoSupply || statusSupply){
                        updateSet.add(customProposal);
                    }
                });
                if (updateSet.size() > 0) {
                    //更新数据
                    updateSet.forEach(c -> {
                        bc.STAGE_BIZ_DATA.getProposalExecuteResult().stageUpdateProposals(c);

                    });
                }
            } else {
                logger.debug("[ProposalSupplySyn]引擎中治理数据为空，跳过治理数据补充任务...");
            }
        } catch (Exception e) {
            logger.error("[ProposalSupplySyn] infoSyn exception : {}", e.getMessage());
        }
    }
}