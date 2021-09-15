package com.platon.browser.service.epoch;

import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.BlockNode;
import com.platon.browser.dao.custommapper.CustomBlockNodeMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 此类非线程安全
 * 周期切换服务
 *
 * 1、根据区块号计算周期切换相关值： 名称/含义 变量名称 当前区块号 currentBlockNumber 当前所处共识周期轮数 consensusEpochRound 当前所处结算周期轮数 settleEpochRound
 * 当前所处结算周期轮数 issueEpochRound
 */
@Slf4j
@Service
public class EpochService {
    @Resource
    private BlockChainConfig chainConfig;
    @Resource
    private EpochRetryService epochRetryService;
    @Resource
    private CustomBlockNodeMapper customBlockNodeMapper;
    @Resource
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
        currentBlockNumber = BigInteger.valueOf(blockNumber);
        BigInteger prevBlockNumber = currentBlockNumber.subtract(BigInteger.ONE);

        // 为防止在增发、结算、共识三个周期重叠时奖励金额计算错误，规定执行顺序为：增发周期变更->结算周期变更->共识周期变更
        issueEpochRound = EpochUtil.getEpoch(currentBlockNumber, chainConfig.getAddIssuePeriodBlockCount());
        if (prevBlockNumber.longValue() % chainConfig.getAddIssuePeriodBlockCount().longValue() == 0) {
            // 增发周期变更
            try {
                epochRetryService.issueChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("增发周期变更执行失败:", e);
            }
        }

        settleEpochRound = EpochUtil.getEpoch(currentBlockNumber, chainConfig.getSettlePeriodBlockCount());
        if (prevBlockNumber.longValue() % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            // 结算周期变更
            try {
                epochRetryService.settlementChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("结算周期变更执行失败:", e);
            }
        }

        consensusEpochRound = EpochUtil.getEpoch(currentBlockNumber, chainConfig.getConsensusPeriodBlockCount());
        if (prevBlockNumber.longValue() % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            // 共识周期变更
            try {
                epochRetryService.consensusChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("共识周期变更执行失败:", e);
            }
            /**
             * 存储每一个共识轮的节点数据
             */
            List<Node> nodes = epochRetryService.getCurValidators();
            if (!nodes.isEmpty()) {
                if (max == -1) {
                    max = customBlockNodeMapper.selectMaxNum();
                }
                /**
                 * 防止25个共识节点重复插入
                 */
                if (prevBlockNumber.longValue() / chainConfig.getConsensusPeriodBlockCount().longValue() >= max) {
                    max++;
                    List<BlockNode> blockNodes = new ArrayList<>(32);
                    Date date = new Date();
                    nodes.forEach(node -> {
                        BlockNode blockNode = new BlockNode();
                        blockNode.setNodeId(node.getNodeId());
                        blockNode.setNodeName("");
                        com.platon.browser.dao.entity.Node nodeT = nodeMapper.selectByPrimaryKey(node.getNodeId());
                        if (nodeT == null) {
                            blockNode.setNodeName("");
                        } else {
                            blockNode.setNodeName(nodeT.getNodeName());
                        }
                        blockNode.setStakingConsensusEpoch(max);
                        blockNode.setCreateTime(date);
                        blockNode.setUpdateTime(date);
                        blockNodes.add(blockNode);
                    });
                    customBlockNodeMapper.batchInsert(blockNodes);
                }
            }
        }
        // 使用EpochRetryService里面的状态信息及EpochService的状态信息构造周期信息
        return EpochMessage.newInstance()
            .updateWithEpochService(this)
            .updateWithEpochRetryService(epochRetryService);
    }

}