package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.AnnualizedRateInfo;
import com.platon.browser.engine.bean.SlashInfo;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 选举事件处理类
 */
@Component
public class NewElectionEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewElectionEpochHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle(EventContext context) {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        BlockChainStage stageData = cacheHolder.getStageData();
        StakingStage stakingStage = context.getStakingStage();
        List<CustomStaking> stakingList = nodeCache.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        for (CustomStaking staking:stakingList){
            // 需要判断被处罚质押是否在上一轮共识周期验证人
            Node exist = bc.getPreValidator().get(staking.getNodeId());
            if(exist==null) continue;
            // 根据前一个共识周期的出块数判断是否触发最低处罚
            boolean isSlash = staking.getPreConsBlockQty()<chainConfig.getSlashBlockThreshold().longValue();
            if(isSlash){
                // 统一罚指定个数的出块奖励
                BigDecimal stakingHas = staking.decimalStakingHas();
                BigDecimal stakingLocked = staking.decimalStakingLocked();
                // 总的质押金：（犹豫+锁定）
                BigDecimal totalAmount = stakingHas.add(stakingLocked);
                // 处罚金额: 固定的区块奖励
                BigDecimal slashAmount = bc.getBlockReward().multiply(chainConfig.getSlashBlockCount());
                // 判断是否需要踢出验证人列表：
                // 1、出块数为零；
                // 2、扣除罚金后，剩余质押金小于最低质押门槛
                // 扣除罚款后剩余的锁定金额 = 原锁定金额+犹豫期金额-罚款
                BigDecimal remainAmount = totalAmount.subtract(slashAmount);
                BigDecimal stakeThresholdInVon = Convert.toVon(bc.getChainConfig().getStakeThreshold(), Convert.Unit.LAT);
                boolean isKickOut = remainAmount.subtract(slashAmount).compareTo(stakeThresholdInVon)<0||staking.getPreConsBlockQty()==0;
                if(isKickOut){
                    // 犹豫期金额置零
                    staking.setStakingHas(BigInteger.ZERO.toString());
                    // 设置离开时间
                    staking.setLeaveTime(bc.getCurBlock().getTimestamp());
                    if(remainAmount.compareTo(BigDecimal.ZERO)>0){ // 剩余金额大于零，则进入退回质押金流程
                        // 锁定金额全部置为退回
                        staking.setStakingReduction(remainAmount.setScale(0,RoundingMode.FLOOR).toString());
                        // 锁定金额置0
                        staking.setStakingLocked(BigInteger.ZERO.toString());
                        // 设定退回时所在结算周期
                        staking.setStakingReductionEpoch(bc.getCurSettingEpoch().intValue());
                        // 节点状态设为退出中
                        staking.setStatus(CustomStaking.StatusEnum.EXITING.code);
                    }else{// 剩余金额等于零
                        // 锁定金额置零
                        staking.setStakingLocked(BigInteger.ZERO.toString());
                        // 节点状态设为已退出
                        staking.setStatus(CustomStaking.StatusEnum.EXITED.code);
                    }
                    staking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
                    staking.setIsSetting(CustomStaking.YesNoEnum.NO.code);
                } else {
                    // 普通出块率低，则扣点钱完事
                    if(stakingHas.compareTo(slashAmount)>=0){
                        // 如果犹豫期金额>=处罚金额,则直接从犹豫期中扣除罚款
                        staking.setStakingHas(stakingHas.subtract(slashAmount).setScale(0,RoundingMode.CEILING).toString());
                    }else{
                        staking.setStakingLocked(remainAmount.setScale(0,RoundingMode.FLOOR).toString());
                        staking.setStakingHas(BigInteger.ZERO.toString());
                    }
                }

                // 记录处罚信息至年化率信息里面
                String annualizedRateInfo = staking.getAnnualizedRateInfo();
                AnnualizedRateInfo ari = new AnnualizedRateInfo();
                if(StringUtils.isNotBlank(annualizedRateInfo)){
                    ari = JSON.parseObject(annualizedRateInfo,AnnualizedRateInfo.class);
                }
                SlashInfo si = new SlashInfo();
                si.setBlockNumber(bc.getCurBlock().getBlockNumber());
                si.setBlockCount(BigInteger.valueOf(staking.getPreConsBlockQty()));
                si.setKickOut(isKickOut);
                si.setSlashBlockCount(BigInteger.valueOf(chainConfig.getSlashBlockCount().longValue()));
                si.setSlashAmount(slashAmount);
                si.setSlashTime(bc.getCurBlock().getTimestamp());
                ari.getSlash().add(si);
                staking.setAnnualizedRateInfo(JSON.toJSONString(ari));

                // 把更新暂存到待入库列表, 记录出块率低处罚
                stakingStage.updateStaking(staking);
                // 记录操作日志
                CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE);
                nodeOpt.updateWithCustomBlock(bc.getCurBlock());
                // BLOCK_COUNT|SLASH_BLOCK_COUNT|AMOUNT|KICK_OUT
                String desc = CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE.tpl
                        .replace("BLOCK_COUNT",staking.getPreConsBlockQty().toString())
                        .replace("SLASH_BLOCK_COUNT",chainConfig.getSlashBlockCount().toString())
                        .replace("AMOUNT",slashAmount.setScale(0,RoundingMode.CEILING).toString())
                        .replace("KICK_OUT",isKickOut?"1":"0");
                nodeOpt.setDesc(desc);
                stageData.getStakingStage().insertNodeOpt(nodeOpt);

                // 更新被处罚节点统计信息（如果存在）
                try {
                    CustomNode node = nodeCache.getNode(staking.getNodeId());
                    node.setStatSlashLowQty(node.getStatSlashLowQty()+1);
                    stakingStage.updateNode(node);
                } catch (NoSuchBeanException e) {
                    logger.error("更新被处罚节点统计信息出错：{}",e.getMessage());
                }
            }
        }
    }
}
