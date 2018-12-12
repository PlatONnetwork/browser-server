package com.platon.browser.service.impl;

import com.maxmind.geoip.Location;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.node.NodeItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeServiceImpl implements NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public List<NodeInfo> getNodeInfoList() {
        List<Node> nodeList = nodeMapper.selectByExample(new NodeExample());
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfoList.add(nodeInfo);
            BeanUtils.copyProperties(node,nodeInfo);
        });
        return nodeInfoList;
    }

    /**
     * 对统计信息按StatisticsEnum分类
     * @param statisticsList
     * @return
     */
    private Map<StatisticsEnum,Map<String,String>> classifyStatistic(List<Statistics> statisticsList){
        Map<StatisticsEnum,Map<String,String>> map = new HashMap<>();
        //   |-- 按节点分类统计指标信息
        // 累计分红
        Map<String,String> rewardAmountMap = new HashMap<>();
        map.put(StatisticsEnum.reward_amount,rewardAmountMap);
        // 累计收益
        Map<String,String> profitAmountMap = new HashMap<>();
        map.put(StatisticsEnum.profit_amount,profitAmountMap);
        // 节点验证次数
        Map<String,String> verifyCountMap = new HashMap<>();
        map.put(StatisticsEnum.verify_count,verifyCountMap);
        // 已出块数
        Map<String,String> blockCountMap = new HashMap<>();
        map.put(StatisticsEnum.block_count,blockCountMap);
        // 区块累计奖励
        Map<String,String> blockRewardMap = new HashMap<>();
        map.put(StatisticsEnum.block_reward,blockRewardMap);
        statisticsList.forEach(statistics -> {
            try {
                StatisticsEnum senum = StatisticsEnum.getEnum(statistics.getType());
                switch (senum){
                    case reward_amount:
                        rewardAmountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case profit_amount:
                        profitAmountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case verify_count:
                        verifyCountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case block_count:
                        blockCountMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                    case block_reward:
                        blockRewardMap.put(statistics.getNodeId(),statistics.getValue());
                        break;
                }
            }catch (Exception e){
                // 枚举获取出现异常
                logger.error("统计类型异常：{}",statistics.getType());
                return;
            }
        });
        return map;
    }

    @Override
    public List<NodeItem> getNodeItemList(NodeListReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getKeyword())){
            // 根据账户名称查询
            criteria.andNameEqualTo(req.getKeyword());
        }
        condition.setOrderByClause("ranking asc");
        List<NodeRanking> list = nodeRankingMapper.selectByExample(condition);

        // 批量查询节点的统计信息
        List<String> nodeIdList = new ArrayList<>();
        list.forEach(node->nodeIdList.add(node.getId()));
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid())
                .andTypeEqualTo(StatisticsEnum.block_count.name())
                .andNodeIdIn(nodeIdList);
        List<Statistics> statisticsList = statisticsMapper.selectByExample(statisticsExample);
        Map<StatisticsEnum,Map<String,String>> statisticsMap = classifyStatistic(statisticsList);
        Map<String,String> blockCountMap = statisticsMap.get(StatisticsEnum.block_count);

        List<NodeItem> itemList = new LinkedList<>();
        list.forEach(node -> {
            NodeItem bean = new NodeItem();
            BeanUtils.copyProperties(node,bean);
            try {
                Location location=GeoUtil.getLocation(node.getIp());
                bean.setCountryCode(location.countryCode);
                if(StringUtils.isNotBlank(location.countryName)){
                    bean.setLocation(location.countryName);
                }
                if(StringUtils.isNotBlank(location.city)){
                    bean.setLocation(bean.getLocation()+" "+location.city);
                }
            }catch (Exception e){
                bean.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
            }

            // 根据排名设置节点竞选状态
            // 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名
            /**
             * 前25：验证节点
             * 前26-100：候选节点
             * 101-200：备选节点
             * **/
            int electionStatus = 1;
            int ranking = node.getRanking();
            // 前25：验证节点
            if(ranking<25) electionStatus = 3;
            // 前26-100：候选节点
            if(25<=ranking && ranking<100) electionStatus = 1;
            // 101-200：备选节点
            if(ranking>=100) electionStatus = 4;

            bean.setElectionStatus(electionStatus);

            // 设置统计信息
            String blockCountStr = blockCountMap.get(node.getId());
            int blockCount = 0;
            if(StringUtils.isNotBlank(blockCountStr)){
                blockCount = Integer.valueOf(blockCountStr);
            }
            bean.setBlockCount(blockCount);

            itemList.add(bean);
        });
        return itemList;
    }

    @Override
    public NodeDetail getNodeDetail(NodeDetailReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid())
            .andAddressEqualTo(req.getAddress());
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        if (nodes.size()>1){
            logger.error("duplicate node: node address {}",req.getAddress());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_DUPLICATE));
        }
        if(nodes.size()==0){
            logger.error("invalid node address {}",req.getAddress());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }

        NodeDetail nodeDetail = new NodeDetail();
        NodeRanking currentNode = nodes.get(0);
        BeanUtils.copyProperties(currentNode,nodeDetail);
        nodeDetail.setJoinTime(currentNode.getJoinTime().getTime());
        nodeDetail.setNodeUrl("http://"+currentNode.getIp()+":"+currentNode.getPort());

        // 设置统计信息
        // 批量查询节点的统计信息
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid()).andNodeIdEqualTo(currentNode.getId());
        List<Statistics> statisticsList = statisticsMapper.selectByExample(statisticsExample);
        Map<StatisticsEnum,Map<String,String>> statisticsMap = classifyStatistic(statisticsList);
        // 累计分红
        Map<String,String> rewardAmountMap = statisticsMap.get(StatisticsEnum.reward_amount);
        // 累计收益
        Map<String,String> profitAmountMap = statisticsMap.get(StatisticsEnum.profit_amount);
        // 节点验证次数
        Map<String,String> verifyCountMap = statisticsMap.get(StatisticsEnum.verify_count);
        // 已出块数
        Map<String,String> blockCountMap = statisticsMap.get(StatisticsEnum.block_count);

        String blockCountStr = blockCountMap.get(currentNode.getId());
        if(StringUtils.isNotBlank(blockCountStr)){
            nodeDetail.setBlockCount(Integer.valueOf(blockCountStr));
        }else{
            nodeDetail.setBlockCount(0);
        }

        String rewardAmountStr = rewardAmountMap.get(currentNode.getId());
        nodeDetail.setRewardAmount(rewardAmountStr);
        String profitAmountStr = profitAmountMap.get(currentNode.getId());
        nodeDetail.setProfitAmount(profitAmountStr);
        String verifyCountStr = verifyCountMap.get(currentNode.getId());
        if(StringUtils.isNotBlank(verifyCountStr)){
            nodeDetail.setVerifyCount(Integer.valueOf(verifyCountStr));
        }else{
            nodeDetail.setVerifyCount(0);
        }


        // 设置地理位置信息
        try {
            Location location = GeoUtil.getLocation(currentNode.getIp());
            if(StringUtils.isNotBlank(location.countryName)){
                nodeDetail.setLocation(location.countryName);
            }
            if(StringUtils.isNotBlank(location.city)){
                nodeDetail.setLocation(nodeDetail.getLocation()+" "+location.city);
            }
        }catch (Exception e){
            nodeDetail.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
        }

        return nodeDetail;
    }

    @Override
    public List<BlockItem> getBlockList(BlockListReq req) {
        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        List<Block> blocks = blockService.getBlockList(downloadReq);
        List<BlockItem> blockItemList = new LinkedList<>();
        blocks.forEach(block -> {
            BlockItem bean = new BlockItem();
            BeanUtils.copyProperties(block,bean);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setHeight(block.getNumber());
            bean.setTransaction(block.getTransactionNumber());
            blockItemList.add(bean);
        });
        return blockItemList;
    }


}
