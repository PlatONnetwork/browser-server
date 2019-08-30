package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/27 11:16
 * @Description: 节点缓存更新器
 */
@Component
public class StakingCacheUpdater {
    private static Logger logger = LoggerFactory.getLogger(StakingCacheUpdater.class);
    @Autowired
    private BlockChain bc;

    String tpl = "节点ID:NODEID,现块号:CUR_NUM,前轮块数:PRE_COUNT,现轮块数:CUR_COUNT";
    /**
     * 更新质押中与区块相关的信息, 每采集到一个区块调用一次
     */
    public void updateStakingPerBlock() {
        CustomBlock curBlock = bc.getCurBlock();
        Node node = bc.getCurValidator().get(curBlock.getNodeId());
        if(node!=null){
            String info = tpl.replace("NODEID",curBlock.getNodeId().substring(0,10))
                    .replace("CUR_NUM",curBlock.getBlockNumber().toString());
            try {
                CustomNode customNode = NODE_CACHE.getNode(curBlock.getNodeId());
                CustomStaking customStaking = customNode.getLatestStaking();
                if(customStaking.getIsConsensus()== CustomStaking.YesNoEnum.YES.code){
                    info = info.replace("PRE_COUNT",customStaking.getPreConsBlockQty().toString());
                    // 当前共识周期出块奖励
                    BigDecimal curConsBlockReward = new BigDecimal(customStaking.getBlockRewardValue()).add(bc.getBlockReward());
                    customStaking.setBlockRewardValue(curConsBlockReward.toString());
                    // 节点出块数加1
                    customStaking.setCurConsBlockQty(customStaking.getCurConsBlockQty()+1);
                    // 把更改后的内容暂存至待更新列表
                    STAGE_DATA.getStakingStage().updateStaking(customStaking);
                    info = info.replace("CUR_COUNT",customStaking.getCurConsBlockQty().toString());
                }
            } catch (NoSuchBeanException e) {
                logger.error("更新出块奖励和共识出块数错误,找不到符合条件的质押信息:{}",e.getMessage());
            }
            logger.debug("出块统计:{}", info);
        }
    }

    /**
     * 更新质押相关的统计信息，在批量采集后批量入库前执行
     *  1.补充统计质押相关数据
     *      a.stat_delegate_has  关联的委托记录中犹豫期金额汇总
     *      b.stat_delegate_locked  关联的委托记录中锁定期金额汇总
     *      c.stat_delegate_reduction   关联的委托记录中退回中金额汇总
     *      d.stat_delegate_qty  关联的委托地址数
     */
    public void updateStakingStatistics () {
        NODE_CACHE.getAllNode().forEach(node -> {
            for (Map.Entry <Long, CustomStaking> customStakingMap : node.getStakings().entrySet()) {
                //只统计不为历史的委托数据
                BigInteger statDelegateHas = BigInteger.ZERO;
                BigInteger statDelegateLocked = BigInteger.ZERO;
                BigInteger statDelegateReduction = BigInteger.ZERO;
                BigInteger statDelegateQty = BigInteger.ZERO;
                for (Map.Entry <String, CustomDelegation> customDelegationMap : customStakingMap.getValue().getDelegations().entrySet()) {
                    if (customDelegationMap.getValue().getIsHistory().equals(CustomDelegation.YesNoEnum.NO.code)) {
                        statDelegateHas = statDelegateHas.add(new BigInteger(customDelegationMap.getValue().getDelegateHas()));
                        statDelegateLocked = statDelegateLocked.add(new BigInteger(customDelegationMap.getValue().getDelegateLocked()));
                        statDelegateReduction = statDelegateReduction.add(new BigInteger(customDelegationMap.getValue().getDelegateReduction()));
                        statDelegateQty = statDelegateQty.add(BigInteger.ONE);
                    }
                }
                customStakingMap.getValue().setStatDelegateHas(statDelegateHas.toString());
                customStakingMap.getValue().setStatDelegateLocked(statDelegateLocked.toString());
                customStakingMap.getValue().setStatDelegateReduction(statDelegateReduction.toString());
                customStakingMap.getValue().setStatDelegateQty(statDelegateQty.intValue());
            }
        });
    }
}
