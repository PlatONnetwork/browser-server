package com.platon.browser.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.exception.UnknownLocationException;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.NodeService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeServiceImpl implements NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Value("${platon.image.server.url}")
    private String imageServerUrl;

    @Autowired
    private RedisCacheServiceImpl redisCacheService;

    @Override
    public RespPage<NodeListItem> getPage(NodePageReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        NodeRankingExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid());
        if(StringUtils.isNotBlank(req.getKeyword())){
            if(req.getKeyword().startsWith("0x")){
                // 根据节点ID查询
                criteria.andNodeIdEqualTo(req.getKeyword());
            }else{
                // 根据账户名称查询
                criteria.andNameLike("%"+req.getKeyword()+"%");
            }
        }
        if(req.getIsValid()!=null){
            // 根据是否有效属性查询
            criteria.andIsValidEqualTo(req.getIsValid());
        }
        if(req.getNodeType()!=null){
            // 根据节点类型查询
            criteria.andTypeEqualTo(req.getNodeType());
        }
        condition.setOrderByClause("ranking asc");

        Page<NodeListItem> page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        List<NodeListItem> data = new LinkedList<>();

        RespPage<NodeListItem> returnData = PageUtil.getRespPage(page,data);
        if(nodes.size()==0) return returnData;

        // 批量查询节点的统计信息
        List<String> nodeIds = new ArrayList<>();
        nodes.forEach(node->nodeIds.add(node.getNodeId()));
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid())
                .andTypeEqualTo(StatisticsEnum.block_count.name())
                .andNodeIdIn(nodeIds);
        List<Statistics> statisticsList = statisticsMapper.selectByExample(statisticsExample);
        Map<StatisticsEnum,Map<String,String>> statisticsMap = classifyStatistic(statisticsList);
        Map<String,String> blockCountMap = statisticsMap.get(StatisticsEnum.block_count);

        nodes.forEach(initData -> {
            NodeListItem bean = new NodeListItem();
            try {
                bean.init(initData);
            } catch (UnknownLocationException e) {
                bean.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
            }
            // 设置统计信息
            String blockCountStr = blockCountMap.get(initData.getId());
            int blockCount = 0;
            if(StringUtils.isNotBlank(blockCountStr)){
                blockCount = Integer.valueOf(blockCountStr);
            }
            bean.setBlockCount(blockCount);
            // 设置logo url
            bean.setLogo(imageServerUrl+initData.getUrl());
            data.add(bean);
        });
        return returnData;
    }

    @Override
    public void updatePushData(String chainId,Set<NodeRanking> data) {
        redisCacheService.updateNodePushCache(chainId,data);
    }

    @Override
    public List<NodePushItem> getPushData(String chainId) {
        List<NodePushItem> returnData = redisCacheService.getNodePushData(chainId);
        return returnData;
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
    public NodeDetail getDetail(NodeDetailReq req) {
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andIdEqualTo(req.getId());
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(condition);
        if (nodes.size()>1){
            logger.error("duplicate node: node {} {}",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_DUPLICATE));
        }
        if(nodes.size()==0){
            logger.error("invalid node {} {}",req.getId());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.NODE_ERROR_NOT_EXIST));
        }

        NodeDetail returnData = new NodeDetail();
        NodeRanking initData = nodes.get(0);
        try {
            returnData.init(initData);
        } catch (UnknownLocationException e) {
            returnData.setLocation(i18n.i(I18nEnum.UNKNOWN_LOCATION));
        }

        // 设置统计信息
        // 批量查询节点的统计信息
        StatisticsExample statisticsExample = new StatisticsExample();
        statisticsExample.createCriteria().andChainIdEqualTo(req.getCid()).andNodeIdEqualTo(initData.getNodeId());
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

        String blockCountStr = blockCountMap.get(initData.getId());
        if(StringUtils.isNotBlank(blockCountStr)){
            returnData.setBlockCount(Long.valueOf(blockCountStr));
        }else{
            returnData.setBlockCount(0l);
        }

        String rewardAmountStr = rewardAmountMap.get(initData.getId());
        returnData.setRewardAmount(StringUtils.isBlank(rewardAmountStr)?"0":rewardAmountStr);
        String profitAmountStr = profitAmountMap.get(initData.getId());
        returnData.setProfitAmount(StringUtils.isBlank(profitAmountStr)?"0":profitAmountStr);
        String verifyCountStr = verifyCountMap.get(initData.getId());
        if(StringUtils.isNotBlank(verifyCountStr)){
            returnData.setVerifyCount(Integer.valueOf(verifyCountStr));
        }else{
            returnData.setVerifyCount(0);
        }
        // 设置logo url
        returnData.setLogo(imageServerUrl+initData.getUrl());
        return returnData;
    }

    @Override
    public List<BlockListItem> getBlockList(BlockListReq req) {
        BlockDownloadReq downloadReq = new BlockDownloadReq();
        BeanUtils.copyProperties(req,downloadReq);
        List<Block> blocks = blockService.getList(downloadReq);
        List<BlockListItem> returnData = new LinkedList<>();
        blocks.forEach(initData -> {
            BlockListItem bean = new BlockListItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

}
