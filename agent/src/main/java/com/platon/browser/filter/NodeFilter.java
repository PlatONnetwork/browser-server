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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;

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

    public List <NodeRanking> NodeFilter ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ) throws Exception {
        log.debug("[into NodeFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        log.debug("[buildNodeStruct blockNumber is ]: " + ethBlock.getBlock().getNumber());
        List <NodeRanking> list = buid(nodeInfoList, blockNumber, ethBlock, blockReward);
        return list;
    }

    @Transactional
    public List <NodeRanking> buid ( String nodeInfoList, long blockNumber, EthBlock ethBlock, String blockReward ) throws Exception {
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
                //todo:测试兼容
                if (candidateDto.getExtra().length() > 0 && null != candidateDto.getExtra()) {
                    if (isNo16(candidateDto.getExtra())) {
                        candidateDetailDto = buildDetail(candidateDto.getExtra());
                    } else {
                        candidateDetailDto = JSONObject.parseObject(candidateDto.getExtra(), CandidateDetailDto.class);
                    }
                }
                nodeRanking.setName(candidateDetailDto.getNodeName());
                nodeRanking.setDeposit(candidateDto.getDeposit().toString());
                nodeRanking.setIntro(candidateDetailDto.getNodeDiscription());
                nodeRanking.setJoinTime(new Date(candidateDetailDto.getTime()));
                nodeRanking.setOrgName(candidateDetailDto.getNodeDepartment());
                nodeRanking.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
                nodeRanking.setBlockReward(blockReward);
                nodeRanking.setRewardRatio((double) candidateDto.getFee() / 10000);
                nodeRanking.setUrl(candidateDetailDto.getNodePortrait() != null ? candidateDetailDto.getNodePortrait() : "test");
                nodeRanking.setRanking(i);
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

    public static String hexStringToString ( String s ) throws Exception {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private CandidateDetailDto buildDetail ( String extra ) throws Exception {
        String data = hexStringToString(extra.substring(2, extra.length()));
        CandidateDetailDto candidateDetailDto = JSONObject.parseObject(data, CandidateDetailDto.class);
        return candidateDetailDto;
    }

    private boolean isNo16 ( String a ) throws Exception {
        String regex = "^[A-Fa-f0-9]+$";
        if (a.matches(regex)) {
            System.out.println(a.toUpperCase() + "是16进制数");
            return true;
        } else {
            System.out.println(a.toUpperCase() + "不是16进制数");
            return false;
        }
    }


    //Increase the number of blocks according to the ownership
    private List <NodeRanking> currentBlockOwner ( List <NodeRanking> list, BigInteger publicKey ) throws Exception {
        for (NodeRanking nodeRanking : list) {
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId()))) {
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
            if (publicKey.equals(new BigInteger(nodeRanking.getNodeId()))) {
                BigInteger sum = new BigInteger(nodeRanking.getBlockReward());
                BigInteger reward = new BigInteger(blockReward);
                sum = sum.add(reward);
                nodeRanking.setBlockReward(sum.toString());
                BigInteger rate = new BigInteger(String.valueOf( 1 - nodeRanking.getRewardRatio()));
                nodeRanking.setRewardRatio(sum.multiply(rate).doubleValue());
                BigInteger fee = new BigInteger(String.valueOf(nodeRanking.getRewardRatio()));
                nodeRanking.setProfitAmount(sum.multiply(fee).toString());
            }
        }
        return list;
    }


}