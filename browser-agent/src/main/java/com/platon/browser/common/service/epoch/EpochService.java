package com.platon.browser.common.service.epoch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.BlockNode;
import com.platon.browser.dao.mapper.CustomBlockNodeMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import com.platon.sdk.contracts.ppos.dto.resp.Node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 周期切换服务
 *
 * 1、根据区块号计算周期切换相关值： 名称/含义 变量名称 当前区块号 currentBlockNumber 当前所处共识周期轮数 consensusEpochRound 当前所处结算周期轮数 settleEpochRound
 * 当前所处结算周期轮数 issueEpochRound
 */
@Slf4j
@Service
public class EpochService {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochRetryService epochRetryService;
    @Autowired
    private CustomBlockNodeMapper customBlockNodeMapper;
    @Autowired
    private NodeMapper nodeMapper;

    @Getter
    private BigInteger currentBlockNumber; // 当前区块号
    @Getter
    private BigInteger consensusEpochRound = BigInteger.ZERO; // 当前所处共识周期轮数
    @Getter
    private BigInteger settleEpochRound = BigInteger.ZERO; // 当前所处结算周期轮数
    @Getter
    private BigInteger issueEpochRound = BigInteger.ZERO; // 当前所处结算周期轮数

    private volatile static int max = -1;

    /**
     * 使用区块号更新服务内部状态
     * 
     * @param blockNumber
     */
    public EpochMessage getEpochMessage(Long blockNumber) throws BlockNumberException {
        // 每个周期第一个块计算下一个周期的相关数据
        this.currentBlockNumber = BigInteger.valueOf(blockNumber);
        // 计算共识周期轮数

        BigInteger prevBlockNumber = this.currentBlockNumber.subtract(BigInteger.ONE);

        // 为防止在增发、结算、共识三个周期重叠时奖励金额计算错误，规定执行顺序为：增发周期变更->结算周期变更->共识周期变更
        this.issueEpochRound =
            EpochUtil.getEpoch(this.currentBlockNumber, this.chainConfig.getAddIssuePeriodBlockCount());
        if (prevBlockNumber.longValue() % this.chainConfig.getAddIssuePeriodBlockCount().longValue() == 0) {
            // 增发周期变更
            try {
                this.epochRetryService.issueChange(this.currentBlockNumber);
            } catch (Exception e) {
                log.error("增发周期变更执行失败:", e);
            }
        }

        this.settleEpochRound =
            EpochUtil.getEpoch(this.currentBlockNumber, this.chainConfig.getSettlePeriodBlockCount());
        if (prevBlockNumber.longValue() % this.chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            // 结算周期变更
            try {
                this.epochRetryService.settlementChange(this.currentBlockNumber);
            } catch (Exception e) {
                log.error("结算周期变更执行失败:", e);
            }
        }

        this.consensusEpochRound =
            EpochUtil.getEpoch(this.currentBlockNumber, this.chainConfig.getConsensusPeriodBlockCount());
        if (prevBlockNumber.longValue() % this.chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            // 共识周期变更
            try {
                this.epochRetryService.consensusChange(this.currentBlockNumber);
            } catch (Exception e) {
                log.error("共识周期变更执行失败:", e);
            }
            /**
             * 存储每一个共识轮的节点数据
             */
            List<Node> nodes = this.epochRetryService.getCurValidators();
            if (!nodes.isEmpty()) {
                if (max == -1) {
                    max = this.customBlockNodeMapper.selectMaxNum();
                }
                /**
                 * 防止25个共识节点重复插入
                 */
                if (prevBlockNumber.longValue() / this.chainConfig.getConsensusPeriodBlockCount().longValue() >= max) {
                    max = max + 1;
                    List<BlockNode> blockNodes = new ArrayList<>(32);
                    nodes.forEach(node -> {
                        BlockNode blockNode = new BlockNode();
                        blockNode.setNodeId(node.getNodeId());
                        com.platon.browser.dao.entity.Node nodeT = this.nodeMapper.selectByPrimaryKey(node.getNodeId());
                        blockNode.setNodeName(nodeT.getNodeName());
                        blockNode.setStakingConsensusEpoch(max);
                        blockNodes.add(blockNode);
                    });
                    this.customBlockNodeMapper.batchInsert(blockNodes);
                }
            }
        }

        return EpochMessage.newInstance().updateWithEpochService(this)
            .updateWithEpochRetryService(this.epochRetryService);
    }

}
