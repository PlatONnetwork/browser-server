package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.CutsomNodeRankingMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.NodeRankingDto;
import com.platon.browser.service.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2019/1/8
 * Time: 15:10
 */
@Component
public class NodeFilter {

    private static Logger log = LoggerFactory.getLogger(NodeFilter.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private CutsomNodeRankingMapper cutsomNodeRankingMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    @Transactional
    public List <NodeRanking> nodeAnalysis ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ,BigInteger publicKey) throws Exception {

        Date beginTime = new Date();
        log.debug("[into NodeFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildNodeStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
        List <NodeRanking> list = build(nodeInfoList, blockNumber, ethBlock, blockReward, publicKey);
        Date endTime = new Date();
        String time2 = String.valueOf(endTime.getTime()-beginTime.getTime());
        log.info("--------------------------------------nodeAnalysis :" + time2);
        return list;
    }

    public List <NodeRanking> build ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ,BigInteger publicKey) throws Exception {
        if (StringUtils.isNotBlank(nodeInfoList)) {
            //list is cadidate struct on PlatON
            List <CandidateDto> list = JSON.parseArray(nodeInfoList, CandidateDto.class);
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            //find NodeRanking info by condition on database
            Date date5 = new Date();

            List <NodeRanking> dbList = nodeRankingMapper.selectByExample(nodeRankingExample);

            // 把库中记录全部置为无效
            NodeRanking node = new NodeRanking();
            node.setIsValid(0);
            nodeRankingMapper.updateByExampleSelective(node,nodeRankingExample);

            Date date6 = new Date();
            log.info("-------------------------------------- nodeRankingMapper sql :"  + String.valueOf(date6.getTime() - date5.getTime()));

            List <NodeRanking> nodeList = new ArrayList <>();
            int i = 1;

            Date date7 = new Date();
            for (CandidateDto candidateDto : list) {
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
            Date date8 = new Date();
            log.info("-------------------------------------- CandidateDto for :"  + String.valueOf(date8.getTime() - date7.getTime()));
            //this time update database struct
            List <NodeRanking> updateList = new ArrayList <>();
            //data form database and node status is vaild

            Map <String, NodeRanking> dbNodeIdToNodeRankingMap = new HashMap <>();
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
                        dbNode.setEndNumber(blockNumber);
                        dbNode.setIsValid(0);
                        updateList.add(dbNode);
                    }
                }
            }
            Date date10 = new Date();
            log.info("-------------------------------------- date for :"  + String.valueOf(date10.getTime() - date9.getTime()));
            String date = JSONArray.toJSONString(updateList);
            currentBlockOwner(updateList, publicKey);
            dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());
            Date date1 = new Date();
            cutsomNodeRankingMapper.insertOrUpdate(updateList);
            Date date2 = new Date();
            log.info("-------------------------------------- replace into :"  + String.valueOf(date1.getTime() - date2.getTime()));
            return updateList;
        }
        return Collections.emptyList();
    }

    //Increase the number of blocks according to the ownership
    private List <NodeRanking> currentBlockOwner ( List <NodeRanking> list, BigInteger publicKey ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x", ""), 16))) {
                long count = nodeRanking.getBlockCount();
                count = count + 1;
                nodeRanking.setBlockCount(count);
                nodeRanking.getRewardRatio();

            }
        }
        return list;
    }

    private List <NodeRanking> dateStatistics ( List <NodeRanking> list, BigInteger publicKey, String blockReward ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x", ""), 16))) {
                BigDecimal sum = new BigDecimal(nodeRanking.getBlockReward());
                BigDecimal reward = new BigDecimal(blockReward);
                sum = sum.add(reward);
                nodeRanking.setBlockReward(sum.toString());
                BigDecimal rate = new BigDecimal(String.valueOf(1 - nodeRanking.getRewardRatio()));
                nodeRanking.setRewardAmount(sum.multiply(rate).toString());
                BigDecimal fee = new BigDecimal(String.valueOf(nodeRanking.getRewardRatio()));
                nodeRanking.setProfitAmount(sum.multiply(fee).toString());
            }
        }
        return list;
    }



}