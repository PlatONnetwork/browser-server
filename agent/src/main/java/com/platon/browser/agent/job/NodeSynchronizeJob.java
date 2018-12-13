package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.agent.contract.CandidateConstract;
import com.platon.browser.common.dto.agent.CandidateDetailDto;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
public class NodeSynchronizeJob extends AbstractTaskJob {

    private final static String contratAddress = "0x1000000000000000000000000000000000000001";

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    //1.查询预编译票池合约所有节点List列表
    //2.更新入队列
    @Override
    @Transactional
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {

            URL resource = NodeSynchronizeJob.class.getClassLoader().getResource("platonbrowser.json");
            String path = resource.getPath();
            Credentials credentials = WalletUtils.loadCredentials("88888888", path);
            Web3j web3j = Web3jClient.getWeb3jClient();
            List <CandidateDto> candidateDtoList = new ArrayList <>();
            //build contract
            CandidateConstract candidateConstract = CandidateConstract.load("0x1000000000000000000000000000000000000001", web3j,
                    credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
            //call contract function get nodeInfo rangking 1-200 from blockchain
            String nodeInfoList = candidateConstract.CandidateList().send();

            //call contract function get  nodeInfo rangking 1-25 from blockchain
            String verfiersList = candidateConstract.VerifiersList().send();
            if (verfiersList.length() > 0 && null != verfiersList) {
                logger.info("verfiersList info :", verfiersList);
                List <CandidateDto> verfiersDtoList = JSON.parseArray(verfiersList, CandidateDto.class);
                candidateDtoList.addAll(verfiersDtoList);
            }
            if (nodeInfoList.length() > 0 && null != nodeInfoList) {
                logger.info("nodeInfoList info :", verfiersList);
                List <CandidateDto> nodeInfoDtoList = JSON.parseArray(nodeInfoList, CandidateDto.class);
                //add no-repeat element
                for (int i = 0; i < nodeInfoDtoList.size(); i++) {
                    for (int j = 0; j < candidateDtoList.size(); j++) {
                        if (!nodeInfoDtoList.get(i).getCandidateId().equals(candidateDtoList.get(j).getCandidateId())) {
                            candidateDtoList.add(nodeInfoDtoList.get(i));
                        }
                    }
                }
            }
            //bulid NodeRankgin dao
            List <NodeRanking> nodeRankings = buildNodeInfo(candidateDtoList, chainId);
            //delete noderangking table
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId);
            nodeRankingMapper.deleteByExample(nodeRankingExample);
            //insert noderangking table
            for (NodeRanking nodeRanking : nodeRankings) {
                try {
                    //insert table of node_Ranking
                    nodeRankingMapper.insert(nodeRanking);
                    logger.debug("nodeRanking data insert...");
                } catch (DuplicateKeyException e) {
                    logger.debug("nodeRanking data repeat...", e.getMessage(), nodeRanking.getId());
                    continue;
                }
            }
            //bulid Node Dao from NodeRanking
            List <Node> nodeList = new ArrayList <>();
            nodeRankings.forEach(nodeRanking -> {
                Node node = new Node();
                node.setId(nodeRanking.getId());
                node.setIp(nodeRanking.getIp());
                node.setPort(nodeRanking.getPort());
                node.setChainId(nodeRanking.getChainId());
                node.setNodeStatus(ping(nodeRanking.getIp()));
                node.setAddress(nodeRanking.getAddress());
                node.setRewardRatio(nodeRanking.getRewardRatio());
                nodeList.add(node);
                logger.debug("nodeInfo: ", JSON.toJSONString(node));
            });


            //select table of Node
            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(chainId);
            List <Node> nodes = nodeMapper.selectByExample(nodeExample);

            //loop get new NodeInfo
            if (nodes.size() > 0) {
                for (Node nodeids : nodeList) {
                    for (Node node : nodes) {
                        if (!nodeids.getId().equals(node.getId())) {
                            Node newNode = new Node();
                            //copy NodeInfo from nodeids to newNode
                            BeanUtils.copyProperties(nodeids, newNode);
                            //every new nodeInfo Corresponding four StatisticsInfo
                            //build StaticticsInfo
                            List <Statistics> statistics = statisticeInfoInsert(newNode, chainId);
                            nodeMapper.insert(newNode);
                            statisticsMapper.batchInsert(statistics);
                        }
                    }
                }
            }

            nodeList.forEach(node -> {
                List <Statistics> statistics = statisticeInfoInsert(node, chainId);
                nodeMapper.insert(node);
                statisticsMapper.batchInsert(statistics);
            });

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Synchronize node info exception!...", e.getMessage());
        }

    }


    private List <NodeRanking> buildNodeInfo ( List <CandidateDto> list, String chainId ) {
        List <NodeRanking> nodeList = new ArrayList <>();
        for (int i = 0; i < list.size(); i++) {
            NodeRanking nodeRanking = new NodeRanking();
            nodeRanking.setChainId(chainId);
            nodeRanking.setIp(list.get(i).getHost());
            nodeRanking.setId(list.get(i).getCandidateId());
            nodeRanking.setPort(Integer.valueOf(list.get(i).getPort()));
            nodeRanking.setAddress(list.get(i).getOwner());
            nodeRanking.setUpdateTime(new Date());
            nodeRanking.setCreateTime(new Date());
            CandidateDetailDto candidateDetailDto = null;
            if (list.get(i).getExtra().length() > 0 && null != list.get(i).getExtra()) {
                candidateDetailDto = buildDetail(list.get(i).getExtra());
            }
            nodeRanking.setName(candidateDetailDto.getNodeName());
            nodeRanking.setDeposit(list.get(i).getDeposit().toString());
            nodeRanking.setIntro(candidateDetailDto.getNodeDiscription());
            nodeRanking.setJoinTime(new Date(candidateDetailDto.getTime()));
            nodeRanking.setOrgName(candidateDetailDto.getNodeDepartment());
            nodeRanking.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
            nodeRanking.setRewardRatio((double) list.get(i).getFee() / 10000);
            nodeRanking.setUrl(candidateDetailDto.getNodePortrait() != null ? candidateDetailDto.getNodePortrait() : "test");
            nodeRanking.setRanking(i + 1);
            nodeRanking.setType(1);
            // 根据排名设置节点竞选状态
            // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
            /**
             * 前25：验证节点
             * 前26-100：候选节点
             * 101-200：备选节点
             * **/
            int electionStatus = 1;
            // 前25：验证节点
            if (i < 25) electionStatus = 3;
            // 前26-100：候选节点
            if (25 <= i && i < 100) electionStatus = 1;
            // 101-200：备选节点
            if (i >= 100) electionStatus = 4;
            nodeRanking.setElectionStatus(electionStatus);
            nodeList.add(nodeRanking);
        }

        return nodeList;
    }

    private CandidateDetailDto buildDetail ( String extra ) {
        String data = hexStringToString(extra.substring(2, extra.length()));
        CandidateDetailDto candidateDetailDto = JSONObject.parseObject(data, CandidateDetailDto.class);
        return candidateDetailDto;
    }

    public static String hexStringToString ( String s ) {
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

    private int ping ( String ip ) {
        InetAddress address = null;
        int timeOut = 3000;
        try {
            address = InetAddress.getByName(ip);
            if (address.isReachable(timeOut)) {
                //success
                return 1;
            } else {
                //fail
                return 2;
            }
        } catch (Exception e) {
            logger.error("Link node exception!...", e.getMessage());
            return 2;
        }
    }

    private List <Statistics> statisticeInfoInsert ( Node node, String chainId ) {
        List <Statistics> list = new ArrayList <>();
        try {
            Statistics blockReawrd = new Statistics();
            blockReawrd.setChainId(chainId);
            blockReawrd.setCreateTime(new Date());
            blockReawrd.setUpdateTime(new Date());
            blockReawrd.setNodeId(node.getId());
            blockReawrd.setValue("");
            blockReawrd.setAddress(node.getAddress());
            blockReawrd.setType(StatisticsEnum.block_reward.name());
            list.add(blockReawrd);
            logger.info("blockReawrd :", JSON.toJSONString(blockReawrd));
            Statistics blockCount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, blockCount);
            blockCount.setType(StatisticsEnum.block_count.name());
            list.add(blockCount);
            logger.info("blockCount :", JSON.toJSONString(blockCount));
            Statistics rewardAmount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, rewardAmount);
            rewardAmount.setType(StatisticsEnum.reward_amount.name());
            list.add(rewardAmount);
            logger.info("rewardAmount :", JSON.toJSONString(rewardAmount));
            Statistics profitAmount = new Statistics();
            BeanUtils.copyProperties(blockReawrd, profitAmount);
            profitAmount.setType(StatisticsEnum.profit_amount.name());
            list.add(profitAmount);
            logger.info("profitAmount :", JSON.toJSONString(profitAmount));
        } catch (DuplicateKeyException e) {
            logger.error("build nodeStatistice exception!...", e.getMessage());
            return Collections.emptyList();
        }
        return list;
    }

}