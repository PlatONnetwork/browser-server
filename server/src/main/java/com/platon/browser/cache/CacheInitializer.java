package com.platon.browser.cache;

import com.github.pagehelper.PageHelper;
import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StatisticMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.ChainEnum;
import com.platon.browser.enums.NodeType;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.GeoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Value("${transaction.tps.statistic.interval}")
    private int transactionTpsStatisticInterval;

    @PostConstruct
    public void initCache(){
        initBlockInfoList();
        initIndexInfo();
        initNodeInfoList();
        initStatisticInfo();
        initTransactionInfoList();
    }

    /**
     * 更新节点信息缓存
     */
    private void initNodeInfoList(){
        Arrays.asList(ChainEnum.values()).forEach(chainEnum -> {
            NodeExample condition = new NodeExample();
            condition.createCriteria().andChainIdEqualTo(chainEnum.code);
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
            cacheService.updateNodeInfoList(nodeInfoList,true,chainEnum);
        });
    }

    /**
     * 更新指标信息缓存
     */
    private void initIndexInfo(){
        Arrays.asList(ChainEnum.values()).forEach(chainEnum -> {
            IndexInfo indexInfo = new IndexInfo();
            // 取当前高度和出块节点
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainEnum.code);
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
            transactionExample.createCriteria().andChainIdEqualTo(chainEnum.code);
            long transactionCount = transactionMapper.countByExample(transactionExample);
            indexInfo.setCurrentTransaction(transactionCount);


            // 取共识节点数
            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(chainEnum.code)
                    .andNodeTypeEqualTo(NodeType.CONSENSUS.code);
            long nodeCount = nodeMapper.countByExample(nodeExample);
            indexInfo.setConsensusNodeAmount(nodeCount);

            // 取地址数
            long addressCount = statisticMapper.countAddress(chainEnum.code);
            indexInfo.setAddressAmount(addressCount);

            // 未知如何获取相关数据，暂时设置为0 -- 2018/10/30
            indexInfo.setProportion(0);
            indexInfo.setTicketPrice(0);
            indexInfo.setVoteAmount(0);
            cacheService.updateIndexInfo(indexInfo,true,chainEnum);
        });
    }

    /**
     * 更新交易统计信息缓存
     */
    private void initStatisticInfo(){

        Arrays.asList(ChainEnum.values()).forEach(chainEnum -> {
            StatisticInfo statisticInfo = new StatisticInfo();

            // 平均出块时长 = (最高块 - 第一个块)/最高块
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainEnum.code);
            blockExample.setOrderByClause("number desc");
            PageHelper.startPage(1,1);
            List<Block> topList = blockMapper.selectByExample(blockExample);
            if(topList.size()==1){
                // 先从最高块向前回溯3600个块
                blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainEnum.code);
                blockExample.setOrderByClause("number desc");
                PageHelper.startPage(3600,1);
                List<Block> bottomList = blockMapper.selectByExample(blockExample);
                if(bottomList.size()==0){
                    // 从后向前累计不足3600个块，则取链上第一个块
                    blockExample = new BlockExample();
                    blockExample.createCriteria().andChainIdEqualTo(chainEnum.code);
                    blockExample.setOrderByClause("number asc");
                    PageHelper.startPage(1,1);
                    bottomList = blockMapper.selectByExample(blockExample);
                }
                Block top = topList.get(0);
                statisticInfo.setHighestBlockNumber(top.getNumber());
                Block bottom = bottomList.get(0);
                statisticInfo.setLowestBlockNumber(bottom.getNumber());
                long avgTime = (top.getTimestamp().getTime()-bottom.getTimestamp().getTime())/top.getNumber();
                statisticInfo.setAvgTime(avgTime);

            }else{
                statisticInfo.setAvgTime(0);
            }

            // 当前交易数
            TransactionExample transactionExample = new TransactionExample();
            transactionExample.createCriteria().andChainIdEqualTo(chainEnum.code);
            long currentTransactionCount = transactionMapper.countByExample(transactionExample);
            statisticInfo.setCurrent(currentTransactionCount);

            // 总交易数
            statisticInfo.setTransactionCount(currentTransactionCount);
            // 有交易的所有区块数
            long blockCount = statisticMapper.countTransactionBlock(chainEnum.code);
            statisticInfo.setBlockCount(blockCount);

            // 计算交易TPS - 最近五分钟内的TPS
            TpsCountParam param = new TpsCountParam();
            param.setChainId(chainEnum.code);
            param.setMinute(transactionTpsStatisticInterval);
            long transactionCount = statisticMapper.countTransactionInXMinute(param);
            double tps = transactionCount/(transactionTpsStatisticInterval*3600);
            statisticInfo.setMaxTps(tps);

            // 计算平均区块交易数
            BigDecimal avgTransactionCount = statisticMapper.countAvgTransactionPerBlock(chainEnum.code);
            statisticInfo.setAvgTransaction(avgTransactionCount);

            // 过去24小时交易笔数
            long count = statisticMapper.countTransactionIn24Hours(chainEnum.code);
            statisticInfo.setDayTransaction(count);

            // 获取最近10个区块
            List<HomeBlock> blockList = statisticMapper.blockList(chainEnum.code);
            List<StatisticItem> statisticList = new ArrayList<>();
            blockList.forEach(block->{
                StatisticItem bean = new StatisticItem();
                BeanUtils.copyProperties(block,bean);
                bean.setHeight(block.getNumber());
                bean.setTime(block.getTimestamp().getTime());
                statisticList.add(bean);
            });
            statisticInfo.setBlockStatisticList(statisticList);

            cacheService.updateStatisticInfo(statisticInfo,true,chainEnum);
        });

    }

    /**
     * 更新区块列表信息缓存
     */
    private void initBlockInfoList(){
        Arrays.asList(ChainEnum.values()).forEach(chainEnum -> {
            List<HomeBlock> blockList = statisticMapper.blockList(chainEnum.code);
            List<BlockInfo> blockInfos = new ArrayList<>();
            long serverTime = System.currentTimeMillis();
            blockList.forEach(block -> {
                BlockInfo bean = new BlockInfo();
                bean.setServerTime(serverTime);
                BeanUtils.copyProperties(block,bean);
                bean.setHeight(block.getNumber());
                bean.setTimestamp(block.getTimestamp().getTime());
                bean.setNode(block.getMiner());
                blockInfos.add(bean);
            });
            cacheService.updateBlockInfoList(blockInfos,chainEnum);
        });
    }

    /**
     * 更新交易列表信息缓存
     */
    private void initTransactionInfoList(){
        Arrays.asList(ChainEnum.values()).forEach(chainEnum -> {
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainEnum.code);
            condition.setOrderByClause("timestamp desc");
            PageHelper.startPage(1,10);
            List<Transaction> transactions = transactionMapper.selectByExample(condition);
            List<TransactionInfo> transactionInfos = new ArrayList<>();
            transactions.forEach(transaction -> {
                TransactionInfo bean = new TransactionInfo();
                BeanUtils.copyProperties(transaction,bean);
                bean.setTxHash(transaction.getHash());
                bean.setBlockHeight(transaction.getBlockNumber());
                bean.setTimestamp(transaction.getTimestamp().getTime());
                transactionInfos.add(bean);
            });
            cacheService.updateTransactionInfoList(transactionInfos,chainEnum);
        });
    }
}
