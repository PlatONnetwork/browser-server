package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.ElectionEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 选举事件处理类
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description:
 */
@Component
public class NewElectionEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewElectionEpochHandler.class);
    private NodeCache nodeCache;
    private StakingStage stakingStage;
    private BlockChain bc;

    @Override
    public void handle(EventContext context) throws ElectionEpochChangeException {
        nodeCache = context.getNodeCache();
        stakingStage = context.getStakingStage();
        bc = context.getBlockChain();

        if(bc.getCurConsensusEpoch().longValue()==1){
            // 因为第一轮共识没有前一轮，所以不处理
            return;
        }

        List<CustomStaking> stakings = nodeCache.getStakingByStatus(Collections.singletonList(CustomStaking.StatusEnum.CANDIDATE));
        for (CustomStaking staking:stakings){
            // 根据前一个共识周期的出块数判断是否触发最低处罚
            // 计算出块率
            BigDecimal blockRate = new BigDecimal(staking.getPreConsBlockQty())
                    .divide(BigDecimal.valueOf(bc.getChainConfig().getExpectBlockCount().longValue()),2,RoundingMode.FLOOR);
            // 判断当前出块率是否小于等于普通处罚百分比
            boolean isSlash = blockRate.compareTo(bc.getChainConfig().getBlockRate4LowSlash())<=0;
            if(isSlash){
                BigDecimal stakingHas = new BigDecimal(staking.getStakingHas());
                BigDecimal stakingLocked = new BigDecimal(staking.getStakingLocked());
                // 判断当前出块率是否小于等于最高处罚百分比
                boolean isHighSlash = blockRate.compareTo(bc.getChainConfig().getBlockRate4HighSlash())<=0;
                // 确定处罚比例
                BigDecimal slashRate = isHighSlash?bc.getChainConfig().getBlockHighSlashRate():bc.getChainConfig().getBlockLowSlashRate();
                // 总的质押金：（犹豫+锁定）
                BigDecimal totalAmount = stakingHas.add(stakingLocked);
                // 处罚金额
                BigDecimal slashAmount = stakingLocked.multiply(slashRate);
                // 判断是否需要踢出验证人列表
                boolean isDeleteStaking = totalAmount.subtract(slashAmount).compareTo(bc.getChainConfig().getStakeThreshold()) < 0;
                if(isHighSlash||isDeleteStaking){
                    // 此处处理与多签处罚一致
                    BigDecimal lockedAmount = stakingLocked.add(stakingHas).subtract(slashAmount);
                    staking.setStakingHas(BigInteger.ZERO.toString());
                    staking.setLeaveTime(new Date());
                    if(lockedAmount.compareTo(BigDecimal.ZERO)>0){
                        staking.setStakingReduction(lockedAmount.toString());
                        staking.setStakingLocked(BigInteger.ZERO.toString());
                        staking.setStakingReductionEpoch(bc.getCurSettingEpoch().intValue());
                        staking.setStatus(CustomStaking.StatusEnum.EXITING.code);
                    }else{
                        staking.setStakingLocked(BigInteger.ZERO.toString());
                        staking.setStatus(CustomStaking.StatusEnum.EXITED.code);
                    }
                    staking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
                    staking.setIsSetting(CustomStaking.YesNoEnum.NO.code);
                } else {
                    if(stakingHas.compareTo(slashAmount)>=0){
                        // 如果犹豫期金额大于等于处罚金额
                        staking.setStakingHas(stakingHas.subtract(slashAmount).toString());
                    }else{
                        staking.setStakingLocked(stakingLocked.add(stakingHas).subtract(slashAmount).toString());
                        staking.setStakingHas(BigInteger.ZERO.toString());
                    }
                }
                // 把更新暂存到待入库列表
                stakingStage.updateStaking(staking);


                // 更新被处罚节点统计信息（如果存在）
                try {
                    CustomNode customNode = nodeCache.getNode(staking.getNodeId());
                    customNode.setStatSlashLowQty(customNode.getStatSlashLowQty()+1);
                    stakingStage.updateNode(customNode);
                } catch (NoSuchBeanException e) {
                    logger.error("更新被处罚节点统计信息出错：{}",e.getMessage());
                }
            }
        }
    }
}
