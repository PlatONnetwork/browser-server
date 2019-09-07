package com.platon.browser.engine;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomRpPlan;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.stage.RestrictingStage;
import com.platon.browser.param.CreateRestrictingParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/8/31 20:09
 * @Description: 锁仓业务引擎
 */
@Component
public class RestrictingEngine {
    private static Logger logger = LoggerFactory.getLogger(RestrictingEngine.class);

    private RestrictingStage restrictingStage = BlockChain.STAGE_DATA.getRestrictingStage();


    public void execute (CustomTransaction tx,BlockChain bc) {
    	logger.debug("execute RestrictingEngine,{}", tx.getTxInfo());
        CreateRestrictingParam param = JSON.parseObject(tx.getTxInfo(),CreateRestrictingParam.class);
        param.getPlan().forEach(planParam -> {
            CustomRpPlan customRpPlan = new CustomRpPlan();
            customRpPlan.setAddress(param.getAccount());
            customRpPlan.setAmount(planParam.getAmount());
            customRpPlan.setEpoch(planParam.getEpoch().longValue());
            customRpPlan.setNumber(tx.getBlockNumber());
            customRpPlan.setCreateTime(new Date());
            customRpPlan.setUpdateTime(new Date());
            restrictingStage.insertRpPlan(customRpPlan);
        });
    }
}
