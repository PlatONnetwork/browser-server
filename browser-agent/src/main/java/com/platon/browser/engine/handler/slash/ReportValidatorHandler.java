package com.platon.browser.engine.handler.slash;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.ReportValidatorParam;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 举报多签(举报验证人)事件处理类
 */
@Component
public class ReportValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ReportValidatorHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle ( EventContext context )throws NoSuchBeanException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        CustomTransaction tx = context.getTransaction();

        // 获取交易入参
        //通过nodeId获取多签举报的质押信息列表，因为举报Data可以举报多个节点
        ReportValidatorParam param = tx.getTxParam(ReportValidatorParam.class);
        //通过结果获取，证据中的举报人的nodeId
        param.getData();
        try {
            CustomNode node = nodeCache.getNode(HexTool.prefix(param.getVerify()));
            CustomStaking latestStaking = node.getLatestStaking();
            String msg = JSON.toJSONString(param);
            logger.debug("多签举报信息:{}", msg);
            //多签举报，惩罚金额
            BigDecimal slashValue = latestStaking.decimalStakingLocked().multiply(chainConfig.getDuplicateSignSlashRate());
            //质押节点扣除惩罚后的锁定期金额 = 未惩罚前的锁定期金额 + 犹豫期的金额 - 惩罚金额
            latestStaking.setStakingLocked(latestStaking.decimalStakingLocked().add(latestStaking.decimalStakingHas()).subtract(slashValue).setScale(0).toString());
            //设置离开时间
            latestStaking.setLeaveTime(bc.getCurBlock().getTimestamp());
            //判断现在的锁定期金额是否大于零
            if (latestStaking.integerStakingLocked().compareTo(BigInteger.ZERO) > 0) {
                latestStaking.setStakingReduction(latestStaking.getStakingLocked());
                latestStaking.setStakingLocked("0");
                Integer reduction = bc.getCurSettingEpoch().intValue();
                latestStaking.setStakingReductionEpoch(reduction);
                latestStaking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
            } else {
                latestStaking.setStakingLocked("0");
                latestStaking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
            }
            latestStaking.setIsConsensus(CustomStaking.YesNoEnum.NO.getCode());
            latestStaking.setIsSetting(CustomStaking.YesNoEnum.NO.getCode());
            //更新分析质押结果
            stakingStage.modifyStaking(latestStaking, tx);

            //新增举报交易结构
            CustomSlash slash = new CustomSlash();
            slash.updateWithSlash(tx, param);
            slash.setReward(slashValue.multiply(chainConfig.getDuplicateSignReportRate()).toString());
            slash.setSlashRate(chainConfig.getDuplicateSignSlashRate().toString());

            //新增分析多重签名结果
            stakingStage.insertSlash(slash);

            // 设置节点统计数据中的多签举报次数
            node.setStatSlashMultiQty(node.getStatSlashMultiQty()+1);
            stakingStage.updateNode(node);

            //交易数据回填
            param.setNodeName(latestStaking.getStakingName());
            param.setStakingBlockNum(latestStaking.getStakingBlockNum().toString());


            // 记录操作日志
            CustomNodeOpt nodeOpt = new CustomNodeOpt(latestStaking.getNodeId(), CustomNodeOpt.TypeEnum.MULTI_SIGN);
            nodeOpt.updateWithCustomBlock(bc.getCurBlock());
            String desc = CustomNodeOpt.TypeEnum.MULTI_SIGN.getTpl()
                    .replace("PERCENT",chainConfig.getDuplicateSignSlashRate().toString())
                    .replace("AMOUNT",slashValue.setScale(0, RoundingMode.CEILING).toString());
            nodeOpt.setDesc(desc);
            stakingStage.insertNodeOpt(nodeOpt);
        } catch (NoSuchBeanException e) {
            logger.error("[ReportValidatorHandler] exception {}", e.getMessage());
            throw new NoSuchBeanException("");
        }
        tx.setTxInfo(JSON.toJSONString(param));
        logger.debug("举报多签(举报验证人)");

    }
}
