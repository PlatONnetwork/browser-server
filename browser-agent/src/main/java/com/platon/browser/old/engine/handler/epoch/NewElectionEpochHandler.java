package com.platon.browser.old.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.old.engine.BlockChain;
import com.platon.browser.old.engine.bean.AnnualizedRateInfo;
import com.platon.browser.old.engine.bean.SlashInfo;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.cache.NodeCache;
import com.platon.browser.old.engine.handler.EventContext;
import com.platon.browser.old.engine.handler.EventHandler;
import com.platon.browser.old.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 选举事件处理类
 */
//@Component
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
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        List<CustomStaking> stakingList = nodeCache.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        for (CustomStaking staking:stakingList){
            // 当前验证人是否在上一轮共识周期
            Node exist = bc.getPreValidator().get(staking.getNodeId());
            if(exist==null) continue;
            // 当前验证人在上一轮共识中出块数为0，则移除候选列表
            boolean isKickOut = staking.getPreConsBlockQty()==0;
            if(isKickOut){
                // 把节点置为退出中
                staking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
                staking.setIsConsensus(CustomStaking.YesNoEnum.NO.getCode());
                staking.setIsSetting(CustomStaking.YesNoEnum.NO.getCode());
                stakingStage.updateStaking(staking);

                // **************记录日志**************
                // 记录处罚信息至年化率信息里面
                String annualizedRateInfo = staking.getAnnualizedRateInfo();
                AnnualizedRateInfo ari = new AnnualizedRateInfo();
                if(StringUtils.isNotBlank(annualizedRateInfo)){
                    ari = JSON.parseObject(annualizedRateInfo,AnnualizedRateInfo.class);
                }
                SlashInfo si = new SlashInfo();
                si.setBlockNumber(bc.getCurBlock().getBlockNumber());
                si.setBlockCount(BigInteger.valueOf(staking.getPreConsBlockQty()));
                si.setKickOut(true);
                si.setSlashBlockCount(BigInteger.ZERO);
                si.setSlashAmount(BigDecimal.ZERO);
                si.setSlashTime(bc.getCurBlock().getTimestamp());
                ari.getSlash().add(si);
                staking.setAnnualizedRateInfo(JSON.toJSONString(ari));

                // 把更新暂存到待入库列表, 记录出块率低处罚
                stakingStage.updateStaking(staking);
                // 记录操作日志
                CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE);
                nodeOpt.updateWithCustomBlock(bc.getCurBlock());
                // BLOCK_COUNT|SLASH_BLOCK_COUNT|AMOUNT|KICK_OUT
                String desc = CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE.getTpl()
                        .replace("SLASH_BLOCK_COUNT",BigInteger.ZERO.toString())
                        .replace("BLOCK_COUNT",staking.getPreConsBlockQty().toString())
                        .replace("AMOUNT",BigInteger.ZERO.toString())
                        .replace("KICK_OUT",isKickOut?"1":"0");
                nodeOpt.setDesc(desc);
                stakingStage.insertNodeOpt(nodeOpt);

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
