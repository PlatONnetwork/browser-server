package com.platon.browser.schedule;

import com.github.pagehelper.PageHelper;
import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.NodeType;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.GeoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 从数据库定时加载最新数据更新缓存
 */
//@Component
public class CacheUpdateTask {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AddressCountMapper addressCountMapper;
    @Autowired
    private AvgTransactionViewMapper avgTransactionViewMapper;
    @Autowired
    private BlockStatisticViewMapper blockStatisticViewMapper;

    @Autowired
    private CacheService cacheService;

    /**
     * 更新节点信息缓存
     */
    @Scheduled(fixedRate = 10000)
    @Transactional
    void updateNodeInfoList(){
        NodeExample example = new NodeExample();
        List<Node> nodeList = nodeMapper.selectByExample(example);
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo ni = new NodeInfo();
            nodeInfoList.add(ni);
            BeanUtils.copyProperties(node,ni);
            Location location = GeoUtil.getLocation(node.getIp());
            ni.setLongitude(String.valueOf(location.longitude));
            ni.setLatitude(String.valueOf(location.latitude));
        });
        cacheService.updateNodeInfoList(true, nodeInfoList);
    }

    /**
     * 更新指标信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateIndexInfo(){
        IndexInfo indexInfo = new IndexInfo();

        // 取当前高度和出块节点
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo("1");
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        if(blockList.size()==0){
            indexInfo.setNode("");
            indexInfo.setCurrentHeight(0);
        }
        indexInfo.setCurrentHeight(0);
        Block block = blockList.get(0);
        indexInfo.setCurrentHeight(block.getNumber());
        indexInfo.setNode(block.getMiner());

        // 取当前交易笔数
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo("1");
        long transactionCount = transactionMapper.countByExample(transactionExample);
        indexInfo.setCurrentTransaction(transactionCount);


        // 取共识节点数
        NodeExample nodeExample = new NodeExample();
        nodeExample.createCriteria().andChainIdEqualTo("1")
                .andNodeTypeEqualTo(NodeType.CONSENSUS.code);
        long nodeCount = nodeMapper.countByExample(nodeExample);
        indexInfo.setConsensusNodeAmount(nodeCount);

        // 取地址数
        AddressCountParam param = new AddressCountParam();
        param.setChainId("1");
        long addressCount = addressCountMapper.countByParam(param);
        indexInfo.setAddressAmount(addressCount);

        // 未知如何获取相关数据，暂时设置为0 -- 2018/10/30
        indexInfo.setProportion(0);
        indexInfo.setTicketPrice(0);
        indexInfo.setVoteAmount(0);
        cacheService.updateIndexInfo(indexInfo);
    }

    /**
     * 更新交易统计信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateStatisticInfo(){

        StatisticInfo statisticInfo = new StatisticInfo();

        // 平均出块时长 = (最高块 - 第一个块)/最高块
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo("1");
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> topList = blockMapper.selectByExample(blockExample);
        if(topList.size()==1){
            // 先从最高块向前回溯3600个块
            blockExample = new BlockExample();
            blockExample.setOrderByClause("number desc");
            PageHelper.startPage(3600,1);
            List<Block> bottomList = blockMapper.selectByExample(blockExample);
            if(bottomList.size()==0){
                // 从后向前累计不足3600个块，则取链上第一个块
                blockExample = new BlockExample();
                blockExample.setOrderByClause("number asc");
                PageHelper.startPage(1,1);
                bottomList = blockMapper.selectByExample(blockExample);
            }
            Block top = topList.get(0);
            Block bottom = bottomList.get(0);
            long avgTime = (top.getTimestamp().getTime()-bottom.getTimestamp().getTime())/top.getNumber();
            statisticInfo.setAvgTime(avgTime);

        }else{
            statisticInfo.setAvgTime(0);
        }

        // 计算当前交易TPS


        // 计算平均区块交易数
        AvgTransactionViewExample avgTransactionViewExample = new AvgTransactionViewExample();
        List<AvgTransactionView> avgTransactionViews = avgTransactionViewMapper.selectByExample(avgTransactionViewExample);
        AvgTransactionView avgTransactionView = avgTransactionViews.get(0);
        statisticInfo.setAvgTransaction(avgTransactionView.getAvgtransaction());

        // 获取最近3600区块
        BlockStatisticViewExample blockStatisticViewExample = new BlockStatisticViewExample();
        List<BlockStatisticView> blockStatisticViews = blockStatisticViewMapper.selectByExample(blockStatisticViewExample);
        List<StatisticItem> statisticList = new ArrayList<>();
        blockStatisticViews.forEach(statistic->{
            StatisticItem bean = new StatisticItem();
            BeanUtils.copyProperties(statistic,bean);
            statisticList.add(bean);
        });
        statisticInfo.setBlockStatisticList(statisticList);

        cacheService.updateStatisticInfo(statisticInfo);
    }

    /**
     * 更新区块列表信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateBlockInfoList(){
        BlockExample condition = new BlockExample();
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1,10);
        List<Block> blockList = blockMapper.selectByExample(condition);
        List<BlockInfo> blockInfos = new ArrayList<>();
        blockList.forEach(block -> {
            BlockInfo bean = new BlockInfo();
            BeanUtils.copyProperties(block,bean);
            blockInfos.add(bean);
        });
        cacheService.updateBlockInfoList(blockInfos);
    }

    /**
     * 更新交易列表信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateTransactionInfoList(){
        TransactionExample condition = new TransactionExample();
        condition.setOrderByClause("timestamp desc");
        PageHelper.startPage(1,10);
        List<Transaction> transactions = transactionMapper.selectByExample(condition);
        List<TransactionInfo> transactionInfos = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionInfo bean = new TransactionInfo();
            BeanUtils.copyProperties(transaction,bean);
            transactionInfos.add(bean);
        });
        cacheService.updateTransactionInfoList(transactionInfos);
    }
}
