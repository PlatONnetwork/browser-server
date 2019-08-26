package com.platon.browser.engine.handler.slash;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomSlash;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.EvidencesParam;
import com.platon.browser.param.ReportValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 举报多签(举报验证人)事件处理类
 */
@Component
public class ReportValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ReportValidatorHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        NodeCache nodeCache = context.getNodeCache();
        StakingStage stakingStage = context.getStakingStage();
        BlockChain bc = context.getBlockChain();
        // 获取交易入参
        //通过nodeId获取多签举报的质押信息列表，因为举报Data可以举报多个节点
        ReportValidatorParam param = tx.getTxParam(ReportValidatorParam.class);
        List <EvidencesParam> evidencesParams = param.getData();
        //通过结果获取，证据中的举报人的nodeId
        evidencesParams.forEach(evidencesParam -> {
            try {
                CustomStaking latestStaking = nodeCache.getNode(evidencesParam.getVerify()).getLatestStaking();
                logger.debug("多签举报信息:{}", JSON.toJSONString(param));

                //交易数据回填
                evidencesParam.setNodeName(latestStaking.getStakingName());
                evidencesParam.setStakingBlockNum(latestStaking.getStakingBlockNum().toString());

                //多签举报，惩罚金额
                Double slashValue = Double.parseDouble(latestStaking.getStakingLocked()) * bc.getChainConfig().getDuplicateSignLowSlashRate().doubleValue();
                //质押节点扣除惩罚后的锁定期金额 = 未惩罚前的锁定期金额 + 犹豫期的金额 - 惩罚金额
                latestStaking.setStakingLocked(new BigDecimal(latestStaking.getStakingLocked()).add(new BigDecimal(latestStaking.getStakingHas())).subtract(new BigDecimal(slashValue.toString())).toString());
                //设置离开时间
                latestStaking.setLeaveTime(new Date());
                //判断现在的锁定期金额是否大于零
                if (new BigInteger(latestStaking.getStakingLocked()).compareTo(BigInteger.ZERO) == 1) {
                    latestStaking.setStakingReduction(latestStaking.getStakingLocked());
                    latestStaking.setStakingLocked("0");
                    Integer reduction = bc.getCurSettingEpoch().intValue();
                    latestStaking.setStakingReductionEpoch(reduction);
                    latestStaking.setStatus(CustomStaking.StatusEnum.EXITING.code);
                }else {
                    latestStaking.setStakingLocked("0");
                    latestStaking.setStatus(CustomStaking.StatusEnum.EXITED.code);
                }
                latestStaking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
                latestStaking.setIsSetting(CustomStaking.YesNoEnum.NO.code);
                //更新分析质押结果
                stakingStage.updateStaking(latestStaking ,tx);

                //新增举报交易结构
                CustomSlash newCustomSlash = new CustomSlash();
                newCustomSlash.updateWithSlash(tx,evidencesParam);
                newCustomSlash.setReward(slashValue.toString());
                newCustomSlash.setSlashRate(bc.getChainConfig().getDuplicateSignLowSlashRate().toString());

                //新增分析多重签名结果
                stakingStage.insertSlash(newCustomSlash);
            } catch (NoSuchBeanException e) {
                logger.error("[ReportValidatorHandler] exception {}", e.getMessage());
            }

            tx.setTxInfo(JSON.toJSONString(param));
        });
        logger.debug("举报多签(举报验证人)");

    }
}
