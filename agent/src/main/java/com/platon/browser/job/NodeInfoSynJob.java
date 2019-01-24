package com.platon.browser.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CutsomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.NodeRankingDto;
import com.platon.browser.filter.FilterTool;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2019/1/22
 * Time: 11:55
 */
@Component
public class NodeInfoSynJob {

    private static Logger log = LoggerFactory.getLogger(NodeInfoSynJob.class);

    @Value("${chain.id}")
    private String chainId;

    private static Long maxNubmer = 0L;

    @Autowired
    private BlockMapper blockMapper;


    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private CutsomNodeRankingMapper cutsomNodeRankingMapper;

    @Autowired
    private RedisCacheService redisCacheService;


    @Autowired
    private ChainsConfig chainsConfig;

    @Autowired
    private Web3jClient web3jClient;

    public final static Map<String,String> NODE_ID_TO_NAME = new HashMap<>();

    @PostConstruct
    public void init () {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if (blocks.size() == 0) {
            maxNubmer = 1L;
        } else {
            maxNubmer = blocks.get(0).getNumber() + 1;
        }
    }


    @Scheduled(cron = "0/1 * * * * ?")
    protected void doJob () {
        try {

            // 从数据库查询有效节点信息，放入本地缓存
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria()
                    .andChainIdEqualTo(chainId)
                    .andIsValidEqualTo(1);
            List <NodeRanking> dbNodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            dbNodes.forEach(n->NODE_ID_TO_NAME.put(n.getNodeId(),n.getName()));


            EthBlock ethBlock = null;
            Web3j web3j = chainsConfig.getWeb3j(chainId);
            BigInteger endNumber = web3j.ethBlockNumber().send().getBlockNumber();
            while (maxNubmer <= endNumber.longValue()) {
                long startTime = System.currentTimeMillis();
                ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(maxNubmer)), true).send();
                maxNubmer++;
                log.debug("getBlockNumber---------------------------------->{}", System.currentTimeMillis()-startTime);
                BigInteger publicKey = CalculatePublicKey.testBlock(ethBlock);
                CandidateContract candidateContract = web3jClient.getCandidateContract();
                //String verifiers = candidateContract.VerifiersList(BigInteger.valueOf(maxNubmer)).send();
                String nodeInfo = candidateContract.CandidateList(BigInteger.valueOf(maxNubmer)).send();
                //List <CandidateDto> verifiernode = JSON.parseArray(verifiers, CandidateDto.class);
                log.debug("candidate---------------------------------->{}", System.currentTimeMillis()-startTime);
                List <CandidateDto> nodes = JSON.parseArray(nodeInfo, CandidateDto.class);
                if (null == nodeInfo);
                if (null == nodes && nodes.size() < 0);
                nodeRankingExample = new NodeRankingExample();
                nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
                //find NodeRanking info by condition on database
                List <NodeRanking> dbList = nodeRankingMapper.selectByExample(nodeRankingExample);
                log.debug("find db list---------------------------------->{}", System.currentTimeMillis()-startTime);
                // 把库中记录全部置为无效
              /*  NodeRanking node = new NodeRanking();
                node.setIsValid(0);
                nodeRankingMapper.updateByExampleSelective(node, nodeRankingExample);*/
                log.debug("update db list---------------------------------->{}", System.currentTimeMillis()-startTime);

                List <NodeRanking> nodeList = new ArrayList <>();
                int i = 1;

                for (CandidateDto candidateDto : nodes) {
                    NodeRanking nodeRanking = new NodeRanking();
                    NodeRankingDto nrd = new NodeRankingDto();
                    nrd.init(candidateDto);
                    BeanUtils.copyProperties(nrd, nodeRanking);
                    BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
                    nodeRanking.setChainId(chainId);
                    nodeRanking.setJoinTime(new Date(ethBlock.getBlock().getTimestamp().longValue()));
                    nodeRanking.setBlockReward(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString()));
                    nodeRanking.setProfitAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).multiply(rate).toString());
                    nodeRanking.setRewardAmount(new BigDecimal(FilterTool.getBlockReward(ethBlock.getBlock().getNumber().toString())).multiply(BigDecimal.ONE.subtract(rate)).toString());
                    nodeRanking.setRanking(i);
                    nodeRanking.setType(1);
                    // Set the node election status according to the ranking
                    // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
                    /**
                     * The first 100：candidate nodes
                     * After 100：alternative nodes
                     * **/
                    int electionStatus = 1;
                    if (1 <= i && i < 25) electionStatus = 3;
                    if (26 <= i && i < 100) electionStatus = 1;
                    if (i >= 100) electionStatus = 4;
                    nodeRanking.setElectionStatus(electionStatus);
                    nodeRanking.setIsValid(1);
                    nodeRanking.setBeginNumber(maxNubmer);
                    nodeList.add(nodeRanking);
                    i = i + 1;
                }
                //this time update database struct
                List <NodeRanking> updateList = new ArrayList <>();
                //data form database and node status is vaild

                Map<String, NodeRanking> dbNodeIdToNodeRankingMap = new HashMap<>();
                nodeList.forEach(e -> {
                    dbNodeIdToNodeRankingMap.put(e.getNodeId(), e);
                    updateList.add(e);
                });

                Date date9 = new Date();
                if (dbList.size() > 0 && dbList != null) {
                    for (int j = 0; j < dbList.size(); j++) {
                        NodeRanking dbNode = dbList.get(j);
                        NodeRanking chainNode = dbNodeIdToNodeRankingMap.get(dbNode.getNodeId());
                        if (chainNode != null) {
                            // 库里有效属性保留
                            chainNode.setBlockCount(dbNode.getBlockCount());
                            chainNode.setJoinTime(dbNode.getJoinTime());
                            chainNode.setBeginNumber(dbNode.getBeginNumber());
                            chainNode.setId(dbNode.getId());
                        } else {
                            dbNode.setEndNumber(maxNubmer);
                            dbNode.setIsValid(0);
                            updateList.add(dbNode);
                        }
                    }
                }
                String date = JSONArray.toJSONString(updateList);
                //TODO:publickey 0.4.0解析存在问题
                FilterTool.currentBlockOwner(updateList, publicKey);
                FilterTool.dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());
                cutsomNodeRankingMapper.insertOrUpdate(updateList);
                log.debug("insertOrUpdate---------------------------------->{}", System.currentTimeMillis()-startTime);

                //TODO:verifierList存在问题，目前错误解决办法，待底层链修复完毕后在进行修正
                int consensusCount = 0;
                for (NodeRanking nodeRanking : updateList) {
                    if (nodeRanking.getIsValid()==1) {
                        consensusCount++;
                        NODE_ID_TO_NAME.put(nodeRanking.getNodeId(),nodeRanking.getName());
                    }
                }

                Set <NodeRanking> redisNode = new HashSet<>(updateList);
                redisCacheService.updateNodePushCache(chainId, redisNode);
                log.debug("NodeInfoSynJob---------------------------------->{}", System.currentTimeMillis()-startTime);
            }


        } catch (Exception e) {
            log.error("PendingTxSynchronizeJob Exception", e.getMessage());
        }

    }


}