package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.StakingExecuteResult;
import com.platon.browser.exception.ConsensusEpochChangeException;
import com.platon.browser.exception.ElectionEpochChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

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
    private StakingExecuteResult executeResult;
    private BlockChain bc;

    @Override
    public void handle(EventContext context) throws ElectionEpochChangeException {
        nodeCache = context.getNodeCache();
        executeResult = context.getExecuteResult();
        bc = context.getBlockChain();

        List<CustomStaking> stakings = nodeCache.getStakingByStatus(Collections.singletonList(CustomStaking.StatusEnum.CANDIDATE));
        for (CustomStaking staking:stakings){
            // 根据前一个共识周期的出块数判断是否触发最低处罚
            boolean isSlash = staking.getPreConsBlockQty()<=bc.getChainConfig().getPackAmountAbnormal().longValue();
            if(isSlash){
                BigDecimal stakingHas = new BigDecimal(staking.getStakingHas());
                BigDecimal stakingLocked = new BigDecimal(staking.getStakingLocked());
                // 判断是否触发最高处罚
                boolean isHighSlash = staking.getPreConsBlockQty()<=bc.getChainConfig().getPackAmountHighAbnormal().longValue();
                // 确定处理比例
                BigDecimal slashRate = isHighSlash?bc.getChainConfig().getPackAmountHighSlashRate():bc.getChainConfig().getPackAmountLowSlashRate();
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
                executeResult.stageUpdateStaking(staking,bc);
            }
        }
    }
}
