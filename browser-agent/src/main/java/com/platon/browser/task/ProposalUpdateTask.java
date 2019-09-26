package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.NodeOptExample;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.*;
import com.platon.browser.task.bean.TaskProposal;
import com.platon.browser.task.cache.ProposalTaskCache;
import com.platon.browser.util.MarkDownParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 提案信息更新任务
 */
@Component
public class ProposalUpdateTask {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpdateTask.class);
    @Autowired
    private PlatOnClient client;
    @Autowired
    private BlockChain bc;
    @Autowired
    private SpecialContractApi sca;
    @Autowired
    private NodeOptMapper nodeOptMapper;
    @Autowired
    private CacheHolder cacheHolder;

    @Autowired
    private ProposalTaskCache taskCache;


    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/5  * * * * ?")
    private void cron () {
        start();
    }

    public void start () {
        //获取全量数据
        Collection<CustomProposal> proposals = getAllProposal();
        if (proposals.isEmpty()) return;

        //记录全量缓存中所有的proposalHash，用于后期补充操作记录的查询条件
        List <String> proposalHashes = new ArrayList <>();
        CustomBlock curBlock = bc.getCurBlock();
        for (CustomProposal proposal : proposals) {//如果已经补充则无需补充
            proposalHashes.add(proposal.getHash());
            TaskProposal cache = new TaskProposal();
            try {
                // 调用外部URL获取信息，并更新当前提案
                ProposalMarkDownDto resp = getMarkdownInfo(proposal.getUrl());

                /* 只有属性有变更才放入入库暂存，防止频繁的数据库更新操作 */

                if(StringUtils.isNotBlank(resp.getTopic())&&!resp.getTopic().equals(proposal.getTopic())){
                    // topic属性有变更
                    cache.setTopic(resp.getTopic());
                    taskCache.update(cache);
                }
                if(StringUtils.isNotBlank(resp.getDescription())&&!resp.getDescription().equals(proposal.getDescription())){
                    // description属性有变更
                    cache.setDescription(resp.getDescription());
                    taskCache.update(cache);
                }

                proposal.updateWithProposalMarkDown(resp);
                if (CustomProposal.TypeEnum.CANCEL.getCode().equals(proposal.getType())) {
                    //若是取消提案，则需要补充被取消提案相关信息
                    CustomProposal cp = getProposal(proposal.getCanceledPipId());
                    if(StringUtils.isNotBlank(cp.getTopic())&&!cp.getTopic().equals(proposal.getCanceledTopic())){
                        // canceledTopic属性有变更
                        cache.setCanceledTopic(cp.getTopic());
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
                    try {
                        ProposalParticiantStat pps = getProposalParticipantStat(proposal.getHash(),curBlock.getHash());
                        //设置参与人数
                        if(pps.getVoterCount()!=null&& !pps.getVoterCount().equals(proposal.getAccuVerifiers())){
                            // 有变更
                            cache.setAccuVerifiers(pps.getVoterCount());
                            taskCache.update(cache);
                        }

                        //设置赞成票
                        if(pps.getSupportCount()!=null&& !pps.getSupportCount().equals(proposal.getYeas())){
                            // 有变更
                            cache.setYeas(pps.getSupportCount());
                            taskCache.update(cache);
                        }

                        //设置反对票
                        if(pps.getOpposeCount()!=null&& !pps.getOpposeCount().equals(proposal.getNays())){
                            // 有变更
                            cache.setNays(pps.getOpposeCount());
                            taskCache.update(cache);
                        }

                        //设置弃权票
                        if(pps.getAbstainCount()!=null&& !pps.getAbstainCount().equals(proposal.getAbstentions())){
                            // 有变更
                            cache.setAbstentions(pps.getAbstainCount());
                            taskCache.update(cache);
                        }

                        //只有在结束快高之后才有返回提案结果
                        if (curBlock.getBlockNumber().longValue() >= Long.parseLong(proposal.getEndVotingBlock())) {
                            //设置状态
                            int status = getTallyResult(proposal.getHash()).getStatus();
                            if(status!=proposal.getStatus()){
                                // 有变更
                                cache.setStatus(status);
                                taskCache.update(cache);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("更新提案({})的结果出错:{}", proposal.getPipId(), e.getMessage());
                    }
                }
            } catch (NoSuchBeanException e) {
                logger.error("更新提案({})的主题和描述出错:{}", proposal.getPipId(), e.getMessage());
            } catch (Exception e){
                logger.error("更新提案({})的主题和描述出错:发送http请求异常({})", proposal.getPipId(), e.getMessage());
            }
        }
        updateNodeOptInfo(proposalHashes);
        // 清除已合并的任务缓存
        taskCache.sweep();
    }

    private void updateNodeOptInfo ( List <String> proposalHashes ) {
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        //补充操作记录中具体提案描述
        if (!proposalHashes.isEmpty()) {
            NodeOptExample nodeOptExample = new NodeOptExample();
            nodeOptExample.createCriteria().andTxHashIn(proposalHashes);
            List <NodeOpt> nodeOpts = nodeOptMapper.selectByExample(nodeOptExample);
            nodeOpts.forEach(nodeOpt -> {
                try {
                    Proposal proposal = proposalCache.getProposal(nodeOpt.getTxHash());
                    String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
                            .replace("ID", proposal.getPipId().toString())
                            .replace("TITLE", proposal.getTopic())
                            .replace("TYPE", CustomProposal.TypeEnum.TEXT.getCode());
                    nodeOpt.setDesc(desc);

                    CustomNodeOpt customNodeOpt = new CustomNodeOpt();
                    BeanUtils.copyProperties(nodeOpt, customNodeOpt);
                    // 放入入库暂存区
                    stakingStage.insertNodeOpt(customNodeOpt);
                } catch (NoSuchBeanException e) {
                    logger.error("更新操作记录({})出错:{}", nodeOpt.getTxHash(), e.getMessage());
                }
            });
        }
    }

    /**
     * 获取所有提案
     * @return
     */
    public Collection<CustomProposal> getAllProposal(){
        return cacheHolder.getProposalCache().getAllProposal();
    }

    /**
     * 根据hash取提案
     * @param hash
     * @return
     * @throws NoSuchBeanException
     */
    public CustomProposal getProposal(String hash) throws NoSuchBeanException {
        return cacheHolder.getProposalCache().getProposal(hash);
    }

    /**
     * 取提案参与者统计信息
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ProposalParticiantStat getProposalParticipantStat(String proposalHash,String blockHash) throws ContractInvokeException {
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
        throw new BlockChainException("查询不到提案[proposalHash="+proposalHash+"]对应的投票结果!");
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
