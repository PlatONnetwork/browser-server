package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomBlock;
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

    /**
     * 更新质押中与区块相关的信息
     */
    public void updateStakingPerBlock() {
        CustomBlock curBlock = bc.getCurBlock();
        Node node = bc.getCurValidator().get(curBlock.getNodeId());
        if(node!=null){
            try {
                CustomNode customNode = NODE_CACHE.getNode(curBlock.getNodeId());
                CustomStaking customStaking = customNode.getLatestStaking();
                if(customStaking.getIsConsensus()== CustomStaking.YesNoEnum.YES.code){
                    // 当前共识周期出块奖励
                    BigDecimal curConsBlockReward = new BigDecimal(customStaking.getBlockRewardValue()).add(bc.getBlockReward());
                    customStaking.setBlockRewardValue(curConsBlockReward.toString());
                    // 节点出块数加1
                    customStaking.setCurConsBlockQty(customStaking.getCurConsBlockQty()+1);
                    // 把更改后的内容暂存至待更新列表
                    STAGE_DATA.getStakingStage().updateStaking(customStaking);
                }
            } catch (NoSuchBeanException e) {
                logger.error("找不到符合条件的质押信息:{}",e.getMessage());
            }
        }
    }
}
