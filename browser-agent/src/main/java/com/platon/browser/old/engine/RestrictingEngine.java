package com.platon.browser.old.engine;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomRpPlan;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.stage.RestrictingStage;
import com.platon.browser.old.exception.BlockChainException;
import com.platon.browser.param.CreateRestrictingParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/8/31 20:09
 * @Description: 锁仓业务引擎
 */
//@Component
public class RestrictingEngine {
    private static Logger logger = LoggerFactory.getLogger(RestrictingEngine.class);
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private BlockChain bc;

    public void execute (CustomTransaction tx) throws BlockChainException {
        RestrictingStage restrictingStage = cacheHolder.getStageData().getRestrictingStage();
    	logger.debug("execute RestrictingEngine,{}", tx.getTxInfo());
        CreateRestrictingParam param = JSON.parseObject(tx.getTxInfo(),CreateRestrictingParam.class);
        //记录参数中的地址
        bc.createAddress(param.getAccount(), CustomAddress.TypeEnum.ACCOUNT);

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
