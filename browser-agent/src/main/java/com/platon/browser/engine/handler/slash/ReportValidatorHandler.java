package com.platon.browser.engine.handler.slash;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomSlash;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
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

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

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

    @Override
    public void handle ( EventContext context )throws NoSuchBeanException {
        CustomTransaction tx = context.getTransaction();
        StakingStage stakingStage = context.getStakingStage();
        // 获取交易入参
        //通过nodeId获取多签举报的质押信息列表，因为举报Data可以举报多个节点
        ReportValidatorParam param = tx.getTxParam(ReportValidatorParam.class);
        //通过结果获取，证据中的举报人的nodeId
        try {
            CustomStaking latestStaking = NODE_CACHE.getNode(HexTool.prefix(param.getVerify())).getLatestStaking();
            logger.debug("多签举报信息:{}", JSON.toJSONString(param));
            //多签举报，惩罚金额
            BigDecimal slashValue = latestStaking.decimalStakingLocked().multiply(chainConfig.getDuplicateSignLowSlashRate());
            //质押节点扣除惩罚后的锁定期金额 = 未惩罚前的锁定期金额 + 犹豫期的金额 - 惩罚金额
            latestStaking.setStakingLocked(latestStaking.decimalStakingLocked().add(latestStaking.decimalStakingHas()).subtract(slashValue).toString());
            //设置离开时间
            latestStaking.setLeaveTime(bc.getCurBlock().getTimestamp());
            //判断现在的锁定期金额是否大于零
            if (latestStaking.integerStakingLocked().compareTo(BigInteger.ZERO) > 0) {
                latestStaking.setStakingReduction(latestStaking.getStakingLocked());
                latestStaking.setStakingLocked("0");
                Integer reduction = bc.getCurSettingEpoch().intValue();
                latestStaking.setStakingReductionEpoch(reduction);
                latestStaking.setStatus(CustomStaking.StatusEnum.EXITING.code);
            } else {
                latestStaking.setStakingLocked("0");
                latestStaking.setStatus(CustomStaking.StatusEnum.EXITED.code);
            }
            latestStaking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
            latestStaking.setIsSetting(CustomStaking.YesNoEnum.NO.code);
            //更新分析质押结果
            stakingStage.modifyStaking(latestStaking, tx);

            //新增举报交易结构
            CustomSlash slash = new CustomSlash();
            slash.updateWithSlash(tx, param);
            slash.setReward(slashValue.toString());
            slash.setSlashRate(chainConfig.getDuplicateSignLowSlashRate().toString());

            //新增分析多重签名结果
            stakingStage.insertSlash(slash);

            //交易数据回填
            param.setNodeName(latestStaking.getStakingName());
            param.setStakingBlockNum(latestStaking.getStakingBlockNum().toString());
        } catch (NoSuchBeanException e) {
            logger.error("[ReportValidatorHandler] exception {}", e.getMessage());
            throw new NoSuchBeanException("");
        }
        tx.setTxInfo(JSON.toJSONString(param));
        logger.debug("举报多签(举报验证人)");

    }
}
