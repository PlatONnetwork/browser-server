package com.platon.browser.cache;

import com.github.pagehelper.PageHelper;
import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StatisticMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.cache.LimitQueue;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 从数据库加载最新数据来初始化缓存
 */
@Component
public class CacheInitializer {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private CacheService cacheService;

    // 交易TPS统计时间间隔, 单位：分钟
    @Value("${platon.transaction.tps.statistic.interval}")
    private int transactionTpsStatisticInterval;

    /**
     * 更新节点信息缓存
     */
    public void initNodeInfoList(String chainId){
        NodeExample condition = new NodeExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        List<Node> nodeList = nodeMapper.selectByExample(condition);
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo bean = new NodeInfo();
            BeanUtils.copyProperties(node,bean);
            Location location = GeoUtil.getLocation(node.getIp());
            bean.setLongitude(location.longitude);
            bean.setLatitude(location.latitude);
            nodeInfoList.add(bean);
        });
        cacheService.updateNodeCache(nodeInfoList,true,chainId);
    }

    /**
     * 更新指标信息缓存
     */
    public void initIndexInfo(String chainId){
        IndexInfo indexInfo = new IndexInfo();
        // 取当前高度和出块节点
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId);
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        if(blockList.size()==0){
            indexInfo.setNode("");
            indexInfo.setCurrentHeight(0);
        }else{
            Block block = blockList.get(0);
            indexInfo.setCurrentHeight(block.getNumber());
            indexInfo.setNode(block.getMiner());
        }

        // 取当前交易笔数
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId);
        long transactionCount = transactionMapper.countByExample(transactionExample);
        indexInfo.setCurrentTransaction(transactionCount);


        // 取共识节点数
        NodeExample nodeExample = new NodeExample();
        nodeExample.createCriteria().andChainIdEqualTo(chainId)
                .andNodeTypeEqualTo(NodeType.CONSENSUS.code);
        long nodeCount = nodeMapper.countByExample(nodeExample);
        indexInfo.setConsensusNodeAmount(nodeCount);

        // 取地址数
        long addressCount = statisticMapper.countAddress(chainId);
        indexInfo.setAddressAmount(addressCount);

        // 未知如何获取相关数据，暂时设置为0 -- 2018/10/30
        indexInfo.setProportion(0);
        indexInfo.setTicketPrice(0);
        indexInfo.setVoteAmount(0);
        cacheService.updateIndexCache(indexInfo,true,chainId);
    }

    /**
     * 更新交易统计信息缓存
     */
    public void initStatisticInfo(String chainId){

        StatisticInfo statisticInfo = new StatisticInfo();

        // 平均出块时长 = (最高块 - 第一个块)/最高块
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId);
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> topList = blockMapper.selectByExample(blockExample);
        if(topList.size()==1){
            // 先从最高块向前回溯3600个块
            blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId);
            blockExample.setOrderByClause("number desc");
            PageHelper.startPage(3600,1);
            List<Block> bottomList = blockMapper.selectByExample(blockExample);
            if(bottomList.size()==0){
                // 从后向前累计不足3600个块，则取链上第一个块
                blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainId);
                blockExample.setOrderByClause("number asc");
                PageHelper.startPage(1,1);
                bottomList = blockMapper.selectByExample(blockExample);
            }
            Block top = topList.get(0);
            statisticInfo.setHighestBlockNumber(top.getNumber());
            statisticInfo.setHighestBlockTimestamp(top.getTimestamp().getTime());
            Block bottom = bottomList.get(0);
            statisticInfo.setLowestBlockNumber(bottom.getNumber());
            statisticInfo.setLowestBlockTimestamp(bottom.getTimestamp().getTime());

            long avgTime = (top.getTimestamp().getTime()-bottom.getTimestamp().getTime())/top.getNumber();
            statisticInfo.setAvgTime(avgTime);

        }else{
            statisticInfo.setAvgTime(0l);
        }

        // 取当前时间回溯五分钟的交易数统计TPS
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime()-transactionTpsStatisticInterval*60*1000);
        // 计算TPS时默认使用设置的间隔的秒数作为除数
        long divisor = transactionTpsStatisticInterval*60;
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId)
                .andTimestampBetween(startDate,endDate);
        List<Transaction> transactionList = transactionMapper.selectByExample(transactionExample);
        int currentCount = transactionList.size();
        statisticInfo.setTransactionCount(Long.valueOf(currentCount));
        // 当前交易数
        statisticInfo.setCurrent(Long.valueOf(currentCount));
        if(divisor!=0){
            BigDecimal transactionTps = BigDecimal.valueOf(currentCount).divide(BigDecimal.valueOf(divisor),4,BigDecimal.ROUND_DOWN);
            statisticInfo.setMaxTps(transactionTps.doubleValue());
        }


        // 总交易数
        transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId);
        long currentTransactionCount = transactionMapper.countByExample(transactionExample);
        statisticInfo.setTransactionCount(currentTransactionCount);
        // 有交易的所有区块数
        long blockCount = statisticMapper.countTransactionBlock(chainId);
        statisticInfo.setBlockCount(blockCount);

        // 计算平均区块交易数
        BigDecimal avgTransactionCount = statisticMapper.countAvgTransactionPerBlock(chainId);
        statisticInfo.setAvgTransaction(avgTransactionCount);

        // 过去24小时交易笔数
        long count = statisticMapper.countTransactionIn24Hours(chainId);
        statisticInfo.setDayTransaction(count);

        // 获取最近100个区块
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1,100);
        List<Block> blockList = blockMapper.selectByExample(condition);
        LimitQueue<StatisticItem> limitQueue = new LimitQueue<>(100);
        blockList.forEach(block->{
            StatisticItem bean = new StatisticItem();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getNumber());
            bean.setTime(block.getTimestamp().getTime());
            limitQueue.offer(bean);
        });
        statisticInfo.setLimitQueue(limitQueue);

        cacheService.updateStatisticCache(statisticInfo,true,chainId);

    }

    /**
     * 更新区块列表信息缓存
     */
    public void initBlockInfoList(String chainId){
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1,10);
        List<Block> blockList = blockMapper.selectByExample(condition);
        List<BlockInfo> blockInfos = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        blockList.forEach(block -> {
            BlockInfo bean = new BlockInfo();
            bean.setServerTime(serverTime);
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getNumber());
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setNode(block.getMiner());
            bean.setTransaction(block.getTransactionNumber());
            blockInfos.add(bean);
        });
        cacheService.updateBlockCache(blockInfos,chainId);
    }

    /**
     * 更新交易列表信息缓存
     */
    public void initTransactionInfoList(String chainId){
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        // 交易记录先根据区块号倒排，再根据交易索引倒排
        condition.setOrderByClause("block_number desc,transaction_index desc");
        PageHelper.startPage(1,10);
        List<Transaction> transactions = transactionMapper.selectByExample(condition);
        List<TransactionInfo> transactionInfos = new LinkedList<>();
        // 由于查数据库的结果是按区块号和交易索引倒排，因此在更新缓存时需要更改为正排
        for (int i=transactions.size()-1;i>0;i--){
            Transaction transaction = transactions.get(i);
            TransactionInfo bean = new TransactionInfo();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setBlockHeight(transaction.getBlockNumber());
            bean.setTimestamp(transaction.getTimestamp().getTime());
            transactionInfos.add(bean);
        }
        cacheService.updateTransactionCache(transactionInfos,chainId);
    }
}
