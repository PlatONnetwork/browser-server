package com.platon.browser.service.statistic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.analyzer.statistic.StatisticsAddressAnalyzer;
import com.platon.browser.analyzer.statistic.StatisticsNetworkAnalyzer;
import com.platon.browser.bean.*;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计入库参数服务
 *
 * @author chendai
 */
@Slf4j
@Service
public class StatisticService {

    @Resource
    private AddressCache addressCache;

    @Resource
    private StatisticsNetworkAnalyzer statisticsNetworkAnalyzer;

    @Resource
    private StatisticsAddressAnalyzer statisticsAddressAnalyzer;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private CustomNodeMapper customNodeMapper;

    @Resource
    protected NodeCache nodeCache;

    /**
     * 解析区块, 构造业务入库参数信息
     *
     * @return
     */
    public void analyze(CollectionEvent event) throws Exception {
        long startTime = System.currentTimeMillis();
        Block block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();
        // 地址统计
        Collection<Address> addressList = this.addressCache.getAll();
        if (block.getNum() == 0) {
            if (CollUtil.isNotEmpty(addressList)) {
                // 初始化内置地址，比如内置合约等
                this.statisticsAddressAnalyzer.analyze(event, block, epochMessage);
            }
            return;
        }
        // 程序逻辑运行至此处，所有ppos相关业务逻辑已经分析完成，进行地址入库操作
        if (CollUtil.isNotEmpty(addressList)) {
            this.statisticsAddressAnalyzer.analyze(event, block, epochMessage);
        }
        this.statisticsNetworkAnalyzer.analyze(event, block, epochMessage);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

    /**
     * 节点结算周期的出块统计---节点当选出块节点次数
     *
     * @param event
     * @return void
     * @date 2021/6/2
     */
    public void nodeSettleStatisElected(CollectionEvent event) {
        try {
            // 为所有的节点的当选节点次数初始化
            List<Node> nodeList = nodeCache.toNodeSettleStatisInfoList();
            List<Node> updateNodeList = CollUtil.newArrayList();
            if (CollUtil.isNotEmpty(nodeList)) {
                nodeList.forEach(node -> {
                    String info = node.getNodeSettleStatisInfo();
                    NodeSettleStatis nodeSettleStatis;
                    if (StrUtil.isEmpty(info)) {
                        nodeSettleStatis = new NodeSettleStatis();
                        nodeSettleStatis.setNodeId(node.getNodeId());
                        nodeSettleStatis.setBlockNum(0L);
                        NodeSettleStatisBase nodeSettleStatisBase = new NodeSettleStatisBase();
                        nodeSettleStatisBase.setSettleEpochRound(event.getEpochMessage().getSettleEpochRound());
                        nodeSettleStatisBase.setBlockNumGrandTotal(BigInteger.ZERO);
                        if (inCurValidator(event.getEpochMessage().getPreValidatorList(), node.getNodeId())) {
                            nodeSettleStatisBase.setBlockNumElected(BigInteger.ONE);
                        } else {
                            nodeSettleStatisBase.setBlockNumElected(BigInteger.ZERO);
                        }
                        nodeSettleStatis.getNodeSettleStatisQueue().add(nodeSettleStatisBase);
                    } else {
                        nodeSettleStatis = NodeSettleStatis.jsonToBean(info);
                        if (event.getEpochMessage().getCurrentBlockNumber().compareTo(BigInteger.valueOf(nodeSettleStatis.getBlockNum())) > 0) {
                            addNodeSettleStatisElected(event.getEpochMessage().getPreValidatorList(), node.getNodeId(), event.getEpochMessage().getSettleEpochRound(), nodeSettleStatis);
                        }
                    }
                    Node updateNode = new Node();
                    updateNode.setNodeId(node.getNodeId());
                    updateNode.setNodeSettleStatisInfo(JSONUtil.toJsonStr(nodeSettleStatis));
                    updateNodeList.add(updateNode);
                    updateNodeCacheSettleStatis(node.getNodeId(), JSONUtil.toJsonStr(nodeSettleStatis));
                });
                if (CollUtil.isNotEmpty(updateNodeList)) {
                    int res = customNodeMapper.updateNodeSettleStatis(updateNodeList);
                    if (res > 0) {
                        log.info("节点列表({})在共识轮数[{}]块高[{}]当选出块节点,更新数据成功",
                                 updateNodeList.stream().map(Node::getNodeId).collect(Collectors.toList()),
                                 event.getEpochMessage().getConsensusEpochRound(),
                                 event.getEpochMessage().getCurrentBlockNumber());
                    } else {
                        log.error("节点列表({})在共识轮数[{}]块高[{}]当选出块节点,更新数据失败",
                                  updateNodeList.stream().map(Node::getNodeId).collect(Collectors.toList()),
                                  event.getEpochMessage().getConsensusEpochRound(),
                                  event.getEpochMessage().getCurrentBlockNumber());
                    }
                }
            }
        } catch (Exception e) {
            log.error(StrUtil.format("节点在共识轮数[{}]块高[{}]当选出块节点更新异常", event.getEpochMessage().getConsensusEpochRound(), event.getEpochMessage().getCurrentBlockNumber()), e);
        }
    }

    /**
     * 更新缓存中的统计信息
     *
     * @param nodeId
     * @param json
     * @return void
     * @date 2021/6/2
     */
    private void updateNodeCacheSettleStatis(String nodeId, String json) {
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            nodeItem.setNodeSettleStatisInfo(json);
        } catch (Exception e) {
            log.error("更新node缓存统计信息异常", e);
        }
    }

    /**
     * 添加节点区块数统计
     *
     * @param curValidatorList   当前共识周期验证人列表
     * @param nodeId             节点id
     * @param curIssueEpochRound 当前结算周期轮数
     * @param nodeSettleStatis   节点结算周期的出块统计
     * @return void
     * @date 2021/6/1
     */
    private void addNodeSettleStatisElected(List<com.platon.contracts.ppos.dto.resp.Node> curValidatorList, String nodeId, BigInteger curIssueEpochRound, NodeSettleStatis nodeSettleStatis) {
        if (nodeSettleStatis.getNodeSettleStatisQueue().size() > 0) {
            List<NodeSettleStatisBase> list = nodeSettleStatis.getNodeSettleStatisQueue().toList();
            // 已记录的最高结算周期轮数，队列已排序
            BigInteger recordSettleEpochRound = list.get(0).getSettleEpochRound();
            if (recordSettleEpochRound.compareTo(curIssueEpochRound) == 0) {
                if (inCurValidator(curValidatorList, nodeId)) {
                    BigInteger newBlockNumElected = list.get(0).getBlockNumElected().add(BigInteger.ONE);
                    list.get(0).setBlockNumElected(newBlockNumElected);
                }
                nodeSettleStatis.getNodeSettleStatisQueue().clear();
                nodeSettleStatis.getNodeSettleStatisQueue().addAll(list);
            } else {
                // 记录下一个结算周期轮数
                NodeSettleStatisBase nodeSettleStatisBase = new NodeSettleStatisBase();
                nodeSettleStatisBase.setSettleEpochRound(curIssueEpochRound);
                nodeSettleStatisBase.setBlockNumGrandTotal(BigInteger.ZERO);
                if (inCurValidator(curValidatorList, nodeId)) {
                    nodeSettleStatisBase.setBlockNumElected(BigInteger.ONE);
                } else {
                    nodeSettleStatisBase.setBlockNumElected(BigInteger.ZERO);
                }
                nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase);
            }
        } else {
            log.error("节点[{}]统计数据[{}]异常，请校验", nodeId, JSONUtil.toJsonStr(nodeSettleStatis));
        }
    }


    /**
     * 节点结算周期的出块统计---累计出块数
     *
     * @param event
     * @return void
     * @date 2021/5/31
     */
    public void nodeSettleStatisBlockNum(CollectionEvent event) {
        try {
            NodeItem nodeItem = nodeCache.getNode(event.getBlock().getNodeId());
            if (ObjectUtil.isNull(nodeItem)) {
                return;
            }
            String info = nodeItem.getNodeSettleStatisInfo();
            NodeSettleStatis nodeSettleStatis;
            if (StrUtil.isEmpty(info)) {
                nodeSettleStatis = new NodeSettleStatis();
                nodeSettleStatis.setNodeId(nodeItem.getNodeId());
                nodeSettleStatis.setBlockNum(event.getEpochMessage().getCurrentBlockNumber().longValue());
                NodeSettleStatisBase nodeSettleStatisBase = new NodeSettleStatisBase();
                nodeSettleStatisBase.setSettleEpochRound(event.getEpochMessage().getSettleEpochRound());
                nodeSettleStatisBase.setBlockNumGrandTotal(BigInteger.ONE);
                nodeSettleStatisBase.setBlockNumElected(BigInteger.ZERO);
                nodeSettleStatis.getNodeSettleStatisQueue().add(nodeSettleStatisBase);
            } else {
                nodeSettleStatis = NodeSettleStatis.jsonToBean(info);
                if (event.getEpochMessage().getCurrentBlockNumber().compareTo(BigInteger.valueOf(nodeSettleStatis.getBlockNum())) > 0) {
                    addNodeSettleStatisBlockNum(event.getEpochMessage().getCurrentBlockNumber().longValue(),
                                                event.getBlock().getNodeId(),
                                                event.getEpochMessage().getSettleEpochRound(),
                                                nodeSettleStatis);
                }
            }
            updateNodeCacheSettleStatis(nodeItem.getNodeId(), JSONUtil.toJsonStr(nodeSettleStatis));
            updateNodeSettleStatis(nodeItem.getNodeId(), JSONUtil.toJsonStr(nodeSettleStatis));
        } catch (Exception e) {
            log.error(StrUtil.format("节点[{}]结算周期的出块统计异常", event.getBlock().getNodeId()), e);
        }
    }

    /**
     * 添加节点区块数统计
     *
     * @param blockNum           块高
     * @param nodeId             节点id
     * @param curIssueEpochRound 当前结算周期轮数
     * @param nodeSettleStatis   节点结算周期的出块统计
     * @return void
     * @date 2021/6/1
     */
    private void addNodeSettleStatisBlockNum(Long blockNum, String nodeId, BigInteger curIssueEpochRound, NodeSettleStatis nodeSettleStatis) {
        nodeSettleStatis.setBlockNum(blockNum);
        if (nodeSettleStatis.getNodeSettleStatisQueue().size() > 0) {
            List<NodeSettleStatisBase> list = nodeSettleStatis.getNodeSettleStatisQueue().toList();
            // 已记录的最高结算周期轮数，队列已排序
            BigInteger recordSettleEpochRound = list.get(0).getSettleEpochRound();
            if (recordSettleEpochRound.compareTo(curIssueEpochRound) == 0) {
                BigInteger newBlockNumGrandTotal = list.get(0).getBlockNumGrandTotal().add(BigInteger.ONE);
                list.get(0).setBlockNumGrandTotal(newBlockNumGrandTotal);
                nodeSettleStatis.getNodeSettleStatisQueue().clear();
                nodeSettleStatis.getNodeSettleStatisQueue().addAll(list);
            } else {
                // 记录下一个结算周期轮数
                NodeSettleStatisBase nodeSettleStatisBase = new NodeSettleStatisBase();
                nodeSettleStatisBase.setSettleEpochRound(curIssueEpochRound);
                nodeSettleStatisBase.setBlockNumGrandTotal(BigInteger.ONE);
                nodeSettleStatisBase.setBlockNumElected(BigInteger.ZERO);
                nodeSettleStatis.getNodeSettleStatisQueue().offer(nodeSettleStatisBase);
            }
        } else {
            log.error("节点[{}]统计数据[{}]异常，请校验", nodeId, JSONUtil.toJsonStr(nodeSettleStatis));
        }
    }

    /**
     * 更新节点区块数统计信息
     *
     * @param nodeId
     * @param json
     * @return void
     * @date 2021/6/2
     */
    private void updateNodeSettleStatis(String nodeId, String json) {
        Node updateNode = new Node();
        updateNode.setNodeId(nodeId);
        updateNode.setNodeSettleStatisInfo(json);
        int res = nodeMapper.updateByPrimaryKeySelective(updateNode);
        if (res > 0) {
            log.info("节点在最近[{}]个结算周期的出块统计信息[{}]更新成功", CommonConstant.BLOCK_RATE_SETTLE_EPOCH_NUM, json);
        } else {
            log.error("节点在最近[{}]个结算周期的出块统计信息[{}]更新失败", CommonConstant.BLOCK_RATE_SETTLE_EPOCH_NUM, json);
        }
    }

    /**
     * 判断当前节点是否在当前共识周期验证人列表
     *
     * @param curValidatorList 当前共识周期验证人列表
     * @param nodeId           节点
     * @return boolean
     * @date 2021/5/31
     */
    private boolean inCurValidator(List<com.platon.contracts.ppos.dto.resp.Node> curValidatorList, String nodeId) {
        if (CollUtil.isNotEmpty(curValidatorList)) {
            List<com.platon.contracts.ppos.dto.resp.Node> curValidator = curValidatorList.stream().filter(v -> v.getNodeId().equalsIgnoreCase(nodeId)).collect(Collectors.toList());
            if (curValidator.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
