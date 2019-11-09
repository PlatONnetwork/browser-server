package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.task.ProposalTaskCache;
import com.platon.browser.common.task.TaskCacheProposal;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.MarkDownParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 提案信息更新任务
 */
@Slf4j
@Component
public class ProposalUpdateTask {
    @Autowired
    private PlatOnClient client;
    @Autowired
    private SpecialContractApi sca;
    @Autowired
    private ProposalTaskCache taskCache;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private ProposalMapper proposalMapper;

    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/5  * * * * ?")
    private void cron(){
        start();
    }

    private void start() {
        //获取全量数据
        ProposalExample proposalExample = new ProposalExample();
        proposalExample.createCriteria().andStatusEqualTo(CustomProposal.StatusEnum.VOTING.getCode());
        List<Proposal> proposals = proposalMapper.selectByExample(proposalExample);
        if (proposals.isEmpty()) return;

        //记录全量缓存中所有的proposalHash，用于后期补充操作记录的查询条件
        List <String> proposalHashes = new ArrayList <>();
        for (Proposal proposal:proposals) {//如果已经补充则无需补充
            proposalHashes.add(proposal.getHash());
            TaskCacheProposal cache = taskCache.get(proposal.getHash());
            if(cache==null) {
                cache = TaskCacheProposal.builder()
                        .hash(proposal.getHash()) // 设置缓存主键
                        .build();
                taskCache.update(cache);
            }

            ProposalMarkDownDto resp;
            try {
                resp = getMarkdownInfo(proposal.getUrl());
            }catch (HttpRequestException e){
                log.error("更新提案(proposal={})出错: {}",proposal.getHash(),e.getMessage());
                continue;
            }

            try {
                // 只有属性有变更才放入入库暂存，防止频繁的数据库更新操作
                if(StringUtils.isNotBlank(resp.getTopic())&&!resp.getTopic().equals(proposal.getTopic())){
                    // topic属性有变更
                    cache.setTopic(resp.getTopic()).setMerged(false);
                    taskCache.update(cache);
                    proposal.setTopic(resp.getTopic());
                }
                if(StringUtils.isNotBlank(resp.getDescription())&&!resp.getDescription().equals(proposal.getDescription())){
                    // description属性有变更
                    cache.setDescription(resp.getDescription()).setMerged(false);
                    taskCache.update(cache);
                    proposal.setDescription(resp.getDescription());
                }

                if (CustomProposal.TypeEnum.CANCEL.getCode()==proposal.getType()) {
                    //补充对应被取消的提案相关信息
                    Proposal cp = proposalMapper.selectByPrimaryKey(proposal.getCanceledPipId());
                    if(StringUtils.isNotBlank(cp.getTopic())&&!cp.getTopic().equals(proposal.getCanceledTopic())){
                        // canceledTopic属性有变更
                        cache.setCanceledTopic(cp.getTopic()).setMerged(false);
                        taskCache.update(cache);
                    }
                }

                //需要更新的提案结果，查询类型1.投票中 2.预升级
                if (
                        CustomProposal.StatusEnum.VOTING.getCode()==proposal.getStatus() //1.投票中
                        || CustomProposal.StatusEnum.PRE_UPGRADE.getCode()==proposal.getStatus() // 2.预升级
                        || CustomProposal.StatusEnum.PASS.getCode()==proposal.getStatus() // 已通过
                ) {
                    //发送rpc请求查询提案结果
                    ProposalParticiantStat pps = getProposalParticipantStat(proposal.getHash(), networkStatCache.getNetworkStat().getCurBlockHash());
                    //设置参与人数
                    if(pps.getVoterCount()!=null&& !pps.getVoterCount().equals(proposal.getAccuVerifiers())){
                        // 有变更
                        cache.setAccuVerifiers(pps.getVoterCount()).setMerged(false);
                        taskCache.update(cache);
                    }

                    //设置赞成票
                    if(pps.getSupportCount()!=null&& !pps.getSupportCount().equals(proposal.getYeas())){
                        // 有变更
                        cache.setYeas(pps.getSupportCount()).setMerged(false);
                        taskCache.update(cache);
                    }

                    //设置反对票
                    if(pps.getOpposeCount()!=null&& !pps.getOpposeCount().equals(proposal.getNays())){
                        // 有变更
                        cache.setNays(pps.getOpposeCount()).setMerged(false);
                        taskCache.update(cache);
                    }

                    //设置弃权票
                    if(pps.getAbstainCount()!=null&& !pps.getAbstainCount().equals(proposal.getAbstentions())){
                        // 有变更
                        cache.setAbstentions(pps.getAbstainCount()).setMerged(false);
                        taskCache.update(cache);
                    }

                    //只有在结束快高之后才有返回提案结果
                    if(networkStatCache.getNetworkStat().getCurNumber()>=proposal.getEndVotingBlock()){
                        //设置状态
                        int status = getTallyResult(proposal.getHash()).getStatus();
                        if(status!=proposal.getStatus()){
                            // 有变更
                            cache.setStatus(status).setMerged(false);
                            taskCache.update(cache);
                        }
                    }
                }
            } catch (Exception e){
                log.error("更新提案(proposal={})出错:", proposal.getHash(), e);
            }
        }
        // 更新操作日志
        //updateNodeOpt(proposalHashes);
        // 清除已合并的任务缓存
        taskCache.sweep();
    }

    /*private void updateNodeOpt(List <String> proposalHashes) {
        if (proposalHashes.isEmpty()) return;
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        //补充操作记录中具体提案描述
        List <CustomNodeOpt> nodeOpts = customNodeOptMapper.selectByTxHashList(proposalHashes);
        nodeOpts.forEach(nodeOpt -> {
            try {
                Proposal proposal = proposalCache.getProposal(nodeOpt.getTxHash());
                String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
                        .replace("ID", proposal.getPipId()==null?"":proposal.getPipId())
                        .replace("TITLE", proposal.getTopic()==null?"":proposal.getTopic())
                        .replace("TYPE", proposal.getType())
                        .replace("VERSION",proposal.getNewVersion()==null?"":proposal.getNewVersion());
                nodeOpt.setDesc(desc);
                // 放入入库暂存区
                stakingStage.updateNodeOpt(nodeOpt);
            } catch (NoSuchBeanException e) {
                logger.error("更新操作记录(TxHash={})出错:{}", nodeOpt.getTxHash(), e.getMessage());
            }
        });
    }*/



    /**
     * 取提案参与者统计信息
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ProposalParticiantStat getProposalParticipantStat(String proposalHash, String blockHash) throws ContractInvokeException, BlankResponseException {
        return sca.getProposalParticipants(client.getWeb3j(), proposalHash, blockHash);
    }

    /**
     * 根据提案hash取提案投票结果
     * @param proposalHash
     * @return
     * @throws Exception
     */
    public TallyResult getTallyResult(String proposalHash) throws Exception {
        BaseResponse <TallyResult> result = client.getProposalContract().getTallyResult(proposalHash).send();
        if(result.isStatusOk()){
            return result.data;
        }
        throw new ContractInvokeException("查询不到提案[proposalHash="+proposalHash+"]对应的投票结果!");
    }

    /**
     * 根据URL获取markdown信息
     * @param url
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    public ProposalMarkDownDto getMarkdownInfo(String url) throws HttpRequestException {
        try {
            String fileUrl = MarkDownParserUtil.acquireMD(url);
            if (fileUrl == null) throw new BusinessException("获取不到" + url);
            String proposalMarkString = MarkDownParserUtil.parserMD(fileUrl);
            return JSON.parseObject(proposalMarkString, ProposalMarkDownDto.class);
        }catch (Exception e){
            throw new HttpRequestException(e.getMessage());
        }
    }

}
