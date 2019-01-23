package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.CutsomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.NodeRankingDto;
import com.platon.browser.job.DataCollectorJob;
import com.platon.browser.service.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.platon.browser.filter.BlockCorrelationFlow.EXECUTOR_SERVICE;

/**
 * User: dongqile
 * Date: 2019/1/8
 * Time: 15:10
 */
@Component
public class NodeFilter {

    private static Logger logger = LoggerFactory.getLogger(NodeFilter.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private CutsomNodeRankingMapper cutsomNodeRankingMapper;
    @Autowired
    private Web3jClient web3jClient;

    private final static ReadWriteLock LOCK = new ReentrantReadWriteLock();
    @Autowired
    private RedisCacheService redisCacheService;

    @Transactional
    public void analysis ( DataCollectorJob.AnalysisParam param, Block block) throws Exception {

        EthBlock ethBlock = param.ethBlock;
        BigInteger publicKey = param.publicKey;
        String blockReward = block.getBlockReward();
        Long blockNumber = block.getNumber();

        LOCK.writeLock().lock();
        try {
            String nodeInfo=null;
            String verifiersInfo="[]";
            long startTime = System.currentTimeMillis();
            try{
                CandidateContract candidateContract = web3jClient.getCandidateContract();
                //verifiersInfo=candidateContract.VerifiersList(ethBlock.getBlock().getNumber()).send();
                nodeInfo=candidateContract.CandidateList(ethBlock.getBlock().getNumber()).send();
            }catch (Exception e){
                logger.debug("nodeInfoList is null !!!...",e.getMessage());
            }
            logger.debug("CandidateContract.CandidateList()         :--->{}",System.currentTimeMillis()-startTime);
            List<CandidateDto> verifiers = JSON.parseArray(verifiersInfo,CandidateDto.class);

            if (StringUtils.isBlank(nodeInfo)){
                flush(Collections.EMPTY_LIST,block,verifiers.size());
                return;
            }
            List <CandidateDto> nodes = JSON.parseArray(nodeInfo, CandidateDto.class);
            if (nodes.size()==0) {
                flush(Collections.EMPTY_LIST,block,verifiers.size());
                return;
            }

            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            //find NodeRanking info by condition on database

            startTime = System.currentTimeMillis();
            List <NodeRanking> dbList = nodeRankingMapper.selectByExample(nodeRankingExample);
            // 把库中记录全部置为无效
            NodeRanking node = new NodeRanking();
            node.setIsValid(0);
            nodeRankingMapper.updateByExampleSelective(node,nodeRankingExample);
            logger.debug("-------------------------------------- nodeRankingMapper sql :{}",System.currentTimeMillis()-startTime);

            List <NodeRanking> nodeList = new ArrayList <>();
            int i = 1;

            startTime = System.currentTimeMillis();
            for (CandidateDto candidateDto : nodes) {
                NodeRanking nodeRanking = new NodeRanking();
                NodeRankingDto nrd = new NodeRankingDto();
                nrd.init(candidateDto);
                BeanUtils.copyProperties(nrd,nodeRanking);
                BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
                nodeRanking.setChainId(chainId);
                nodeRanking.setJoinTime(new Date(ethBlock.getBlock().getTimestamp().longValue()));
                nodeRanking.setBlockReward(blockReward);
                nodeRanking.setProfitAmount(new BigDecimal(blockReward).multiply(rate).toString());
                nodeRanking.setRewardAmount(new BigDecimal(blockReward).multiply(BigDecimal.ONE.subtract(rate)).toString());
                nodeRanking.setRanking(i);
                nodeRanking.setType(1);
                // Set the node election status according to the ranking
                // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
                /**
                 * The first 100：candidate nodes
                 * After 100：alternative nodes
                 * **/
                int electionStatus = 1;
                if( 1 <= i && i < 25) electionStatus = 3;
                if (26 <= i && i < 100) electionStatus = 1;
                if (i >= 100) electionStatus = 4;
                nodeRanking.setElectionStatus(electionStatus);
                nodeRanking.setIsValid(1);
                nodeRanking.setBeginNumber(blockNumber);
                nodeList.add(nodeRanking);
                i = i + 1;
            }
            logger.debug("-------------------------------------- CandidateDto for :{}",System.currentTimeMillis()-startTime);
            //this time update database struct
            List <NodeRanking> updateList = new ArrayList <>();
            //data form database and node status is vaild

            Map <String, NodeRanking> dbNodeIdToNodeRankingMap = new HashMap <>();
            nodeList.forEach(e -> {
                dbNodeIdToNodeRankingMap.put(e.getNodeId(), e);
                updateList.add(e);
            });

            startTime = System.currentTimeMillis();
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
                        dbNode.setEndNumber(blockNumber);
                        dbNode.setIsValid(0);
                        updateList.add(dbNode);
                    }
                }
            }
            logger.debug("-------------------------------------- date for :{}",System.currentTimeMillis()-startTime);
            String date = JSONArray.toJSONString(updateList);
            FilterTool.currentBlockOwner(updateList, publicKey);
            FilterTool.dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());

            startTime = System.currentTimeMillis();
            cutsomNodeRankingMapper.insertOrUpdate(updateList);
            logger.debug("-------------------------------------- replace into :{}",System.currentTimeMillis()-startTime);

            flush(nodeList,block,verifiers.size());
        }finally {
            LOCK.writeLock().unlock();
        }
    }


    public void flush(List<NodeRanking> nodeRankings,Block currentBlock,int nodeNumber){
        EXECUTOR_SERVICE.submit(()->{
            redisCacheService.updateNodePushCache(chainId, new HashSet<>(nodeRankings));
            redisCacheService.updateStatisticsCache(chainId, currentBlock, nodeNumber);
        });
    }
}