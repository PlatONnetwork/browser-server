package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.common.dto.agent.CandidateDetailDto;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.GeoUtil;
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
    private RedisCacheService redisCacheService;

    public List <NodeRanking> nodeAnalysis ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ) throws Exception {
        log.debug("[into NodeFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildNodeStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
        List <NodeRanking> list = build(nodeInfoList, blockNumber, ethBlock, blockReward);
        return list;
    }

    @Transactional
    public List <NodeRanking> build ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ) throws Exception {
        if (StringUtils.isNotBlank(nodeInfoList)) {
            //list is cadidate struct on PlatON
            List <CandidateDto> list = JSON.parseArray(nodeInfoList, CandidateDto.class);
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            //find NodeRanking info by condition on database
            List <NodeRanking> dbList = nodeRankingMapper.selectByExample(nodeRankingExample);
            List <NodeRanking> nodeList = new ArrayList <>();
            int i = 1;
            for (CandidateDto candidateDto : list) {
                NodeRanking nodeRanking = new NodeRanking();
                nodeRanking.setChainId(chainId);
                nodeRanking.setIp(candidateDto.getHost());
                nodeRanking.setNodeId(candidateDto.getCandidateId());
                nodeRanking.setPort(Integer.valueOf(candidateDto.getPort()));
                nodeRanking.setAddress(candidateDto.getOwner());
                nodeRanking.setUpdateTime(new Date());
                nodeRanking.setCreateTime(new Date());
                CandidateDetailDto candidateDetailDto = null;
                try{
                    if (candidateDto.getExtra().length() > 0 && null != candidateDto.getExtra()) {
                        candidateDetailDto = JSONObject.parseObject(candidateDto.getExtra(), CandidateDetailDto.class);
                        nodeRanking.setName(candidateDetailDto.getNodeName());
                        nodeRanking.setIntro(candidateDetailDto.getNodeDiscription());
                        nodeRanking.setOrgName(candidateDetailDto.getNodeDepartment());
                        nodeRanking.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
                        nodeRanking.setUrl(candidateDetailDto.getNodePortrait() != null ? candidateDetailDto.getNodePortrait() : "test");
                    }
                }catch (Exception e){
                    nodeRanking.setName("");
                    nodeRanking.setIntro("");
                    nodeRanking.setOrgName("");
                    nodeRanking.setOrgWebsite("");
                    nodeRanking.setUrl("");
                    log.error("Extra Date error: " + candidateDto.getExtra() + ",NodeId : " +  nodeRanking.getNodeId() );
                }
                nodeRanking.setJoinTime(new Date(ethBlock.getBlock().getTimestamp().longValue()));
                nodeRanking.setDeposit(candidateDto.getDeposit().toString());
                nodeRanking.setBlockReward(blockReward);
                nodeRanking.setRewardRatio(BigDecimal.valueOf(candidateDto.getFee()).divide(BigDecimal.valueOf(10000),4,BigDecimal.ROUND_FLOOR).doubleValue());

                nodeRanking.setRanking(i);
                nodeRanking.setBlockCount(1L);
                BigDecimal rate = new BigDecimal(nodeRanking.getRewardRatio());
                nodeRanking.setProfitAmount(new BigDecimal(blockReward).multiply(rate).toString());
                nodeRanking.setRewardAmount(new BigDecimal(blockReward).multiply(BigDecimal.ONE.subtract(rate)).toString());

                GeoUtil.IpLocation ipLocation = GeoUtil.getIpLocation(nodeRanking.getIp());
                BeanUtils.copyProperties(ipLocation,nodeRanking);

                i = i++;
                nodeRanking.setType(1);
                // Set the node election status according to the ranking
                // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
                /**
                 * The first 100：candidate nodes
                 * After 100：alternative nodes
                 * **/
                int electionStatus = 1;
                if (1 <= i && i < 100) electionStatus = 1;
                if (i >= 100) electionStatus = 4;
                nodeRanking.setElectionStatus(electionStatus);
                nodeRanking.setIsValid(1);
                nodeRanking.setBeginNumber(blockNumber);
                nodeList.add(nodeRanking);
            }
            //calulate this block publickey
            BigInteger publicKey = CalculatePublicKey.testBlock(ethBlock, BigInteger.valueOf(blockNumber));

            //this time update database struct
            List <NodeRanking> updateList = new ArrayList <>();
            //data form database and node status is vaild
            if (dbList.size() > 0 && dbList != null) {
                for (NodeRanking chainData : nodeList) {
                    for (NodeRanking dbData : dbList) {
                        if (chainData.getNodeId().equals(dbData.getNodeId())) {
                            NodeRanking nodeRanking = new NodeRanking();
                            BeanUtils.copyProperties(chainData, nodeRanking);
                            nodeRanking.setId(dbData.getId());
                            nodeRanking.setBeginNumber(dbData.getBeginNumber());
                            nodeRanking.setJoinTime(dbData.getJoinTime());
                            updateList.add(nodeRanking);
                        }
                        NodeRanking nodeRanking = new NodeRanking();
                        BeanUtils.copyProperties(dbData, nodeRanking);
                        nodeRanking.setEndNumber(blockNumber);
                        nodeRanking.setIsValid(0);
                        updateList.add(nodeRanking);
                        //链上数据同数据库有效数据做对比
                        //数据库数据：如果有重复，将重复的该条重复
                        //更新列表
                    }
                }
                currentBlockOwner(updateList, publicKey);
                dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());
                nodeRankingMapper.batchInsert(updateList);
                Set <NodeRanking> nodes = new HashSet <>(updateList);
                redisCacheService.updateNodePushCache(chainId, nodes);
                return updateList;
            }
            currentBlockOwner(nodeList, publicKey);
            dateStatistics(updateList, publicKey, ethBlock.getBlock().getNumber().toString());
            nodeRankingMapper.batchInsert(nodeList);
            Set <NodeRanking> nodes = new HashSet <>(nodeList);
            redisCacheService.updateNodePushCache(chainId, nodes);

            return nodeList;

        }
        return Collections.emptyList();
    }

    //Increase the number of blocks according to the ownership
    private List <NodeRanking> currentBlockOwner ( List <NodeRanking> list, BigInteger publicKey ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x",""), 16))) {
                long count = nodeRanking.getBlockCount();
                count = count + 1;
                nodeRanking.setBlockCount(count);
                nodeRanking.getRewardRatio();

            }
        }
        return list;
    }

    private List <NodeRanking> dateStatistics( List <NodeRanking> list, BigInteger publicKey, String blockReward ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId().replace("0x",""), 16))) {
                BigDecimal sum = new BigDecimal(nodeRanking.getBlockReward());
                BigDecimal reward = new BigDecimal(blockReward);
                sum = sum.add(reward);
                nodeRanking.setBlockReward(sum.toString());
                BigDecimal rate = new BigDecimal(String.valueOf( 1 - nodeRanking.getRewardRatio()));
                nodeRanking.setRewardRatio(sum.multiply(rate).doubleValue());
                BigDecimal fee = new BigDecimal(String.valueOf(nodeRanking.getRewardRatio()));
                nodeRanking.setProfitAmount(sum.multiply(fee).toString());
            }
        }
        return list;
    }


}