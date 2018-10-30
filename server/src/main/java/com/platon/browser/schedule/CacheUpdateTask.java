package com.platon.browser.schedule;

import com.github.pagehelper.PageHelper;
import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.NodeType;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.GeoUtil;
import org.apache.poi.ss.formula.functions.Index;
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
@Component
public class CacheUpdateTask {

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

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
        long transactionCount = transactionMapper.countByExample(transactionExample);
        indexInfo.setCurrentTransaction(transactionCount);


        // 取共识节点数
        NodeExample nodeExample = new NodeExample();
        nodeExample.createCriteria().andNodeTypeEqualTo(NodeType.CONSENSUS.code);
        long nodeCount = nodeMapper.countByExample(nodeExample);
        indexInfo.setConsensusNodeAmount(nodeCount);

        // 取地址数


    }

    /**
     * 更新交易统计信息缓存
     */
    @Scheduled(fixedRate = 10000)
    void updateStatisticInfo(){

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
