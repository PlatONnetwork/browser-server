package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticiantStat;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.NodeOptExample;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.CustomNodeOptMapper;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.ProposalMarkDownDto;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.BlockChainException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.util.MarkDownParserUtil;
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
import java.util.Set;

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
    private CustomProposalMapper customProposalMapper;
    @Autowired
    private CustomNodeOptMapper customNodeOptMapper;

    private ProposalStage proposalStage = new ProposalStage();

    private StakingStage stakingStage = new StakingStage();


    /**
     * 同步任务功能说明：
     * a.http请求查询github治理提案的相关信息并补充
     * b.根据platon底层rpc接口查询提案结果
     */
    @Scheduled(cron = "0/5  * * * * ?")
    private void cron () {
        start();
        logger.info("------------------------------------A-------------------------------------------{}",System.currentTimeMillis());

    }

    public void start () {
        ProposalCache proposalCache = cacheHolder.getProposalCache();

        //获取全量数据
        Collection<CustomProposal> proposals = getAllProposal();
        //记录全量缓存中所有的proposalhash，用于后期补充操作记录的查询条件
        List <String> proposalList = new ArrayList <>();
        if (proposals.isEmpty()) return;
        CustomBlock curBlock = bc.getCurBlock();
        for (CustomProposal proposal : proposals) {//如果已经补充则无需补充
            proposalList.add(proposal.getHash());
            try {
                logger.info("------------------------------------B-------------------------------------------{}",System.currentTimeMillis());
                ProposalMarkDownDto proposalMarkDownDto = getMarkdownInfo(proposal.getUrl());
                logger.info("------------------------------------C-------------------------------------------{}",System.currentTimeMillis());
                proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                if (CustomProposal.TypeEnum.CANCEL.getCode().equals(proposal.getType())) {
                    proposal.updateWithProposalMarkDown(proposalMarkDownDto);
                    //若是取消提案，则需要补充被取消提案相关信息
                    CustomProposal cp = getProposal(proposal.getCanceledPipId());
                    proposal.setCanceledTopic(cp.getTopic());
                }
                // 添加至全量缓存
                proposalCache.addProposal(proposal);
                // 暂存至待入库列表
                proposalStage.updateProposal(proposal);
            } catch (NoSuchBeanException | BusinessException e) {
                logger.error("更新提案({})的主题和描述出错:{}", proposal.getPipId(), e.getMessage());
            } catch (Exception e){
                logger.error("更新提案({})的主题和描述出错:发送http请求异常({},{})", proposal.getPipId(), proposal.getUrl(),e.getMessage());
            }
            //需要更新的提案结果，查询类型1.投票中 2.预升级
            if (proposal.getStatus().equals(CustomProposal.StatusEnum.VOTING.getCode())
                    || proposal.getStatus().equals(CustomProposal.StatusEnum.PRE_UPGRADE.getCode())
                    || proposal.getStatus().equals(CustomProposal.StatusEnum.PASS.getCode())) {
                //发送rpc请求查询提案结果
                try {
                    ProposalParticiantStat pps = getProposalParticipantStat(proposal.getHash(),curBlock.getHash());
                    //设置参与人数
                    proposal.setAccuVerifiers(pps.getVoterCount());
                    //设置赞成票
                    proposal.setYeas(pps.getSupportCount());
                    //设置反对票
                    proposal.setNays(pps.getOpposeCount());
                    //设置弃权票
                    proposal.setAbstentions(pps.getAbstainCount());
                    //只有在结束快高之后才有返回提案结果
                    if (curBlock.getBlockNumber().longValue() >= Long.parseLong(proposal.getEndVotingBlock())) {
                        //设置状态
                        proposal.setStatus(getTallyResult(proposal.getHash()).getStatus());
                    }
                    // 添加至全量缓存
                    proposalCache.addProposal(proposal);
                    // 暂存至待入库列表
                    proposalStage.updateProposal(proposal);
                } catch (Exception e) {
                    logger.error("更新提案({})的结果出错:{}", proposal.getPipId(), e.getMessage());

                }
            }
        }
        //入库
        if(!proposalStage.exportProposal().isEmpty()){
            Set <Proposal> list = proposalStage.exportProposal();
            String s = JSONArray.toJSONString(list);
            logger.info("replace into info {}",s);
            customProposalMapper.batchInsertOrUpdateSelective(proposalStage.exportProposal(),Proposal.Column.values());
            proposalStage.clear();
        }
        updateNodeOptInfo(proposalList);
    }

    private void updateNodeOptInfo ( List <String> proposalList ) {
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        //补充操作记录中具体提案描述
        if (!proposalList.isEmpty()) {
            NodeOptExample nodeOptExample = new NodeOptExample();
            nodeOptExample.createCriteria().andTxHashIn(proposalList);
            List <NodeOpt> nodeOpts = nodeOptMapper.selectByExample(nodeOptExample);
            nodeOpts.forEach(nodeOpt -> {
                Proposal proposal = new Proposal();
                try {
                    proposal = proposalCache.getProposal(nodeOpt.getTxHash());
                } catch (NoSuchBeanException e) {
                    logger.error("更新操作({})的结果出错:{}", proposal.getHash(), e.getMessage());
                }
                if (proposal != null) {
                    String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
                            .replace("ID", proposal.getPipId().toString())
                            .replace("TITLE", proposal.getTopic())
                            .replace("TYPE", CustomProposal.TypeEnum.TEXT.getCode());
                    nodeOpt.setDesc(desc);
                }
                CustomNodeOpt customNodeOpt = new CustomNodeOpt();
                BeanUtils.copyProperties(nodeOpt, customNodeOpt);
                stakingStage.updateNodeOpt(customNodeOpt);
                if(!stakingStage.exportNodeOpt().isEmpty()){
                    customNodeOptMapper.batchInsertOrUpdateSelective(stakingStage.exportNodeOpt(),NodeOpt.Column.values());
                    stakingStage.clear();
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
    public ProposalParticiantStat getProposalParticipantStat(String proposalHash,String blockHash) throws Exception {
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
    public ProposalMarkDownDto getMarkdownInfo(String url) throws BusinessException, HttpRequestException {
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
