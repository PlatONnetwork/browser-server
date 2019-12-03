package com.platon.browser.config.govern;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

/**
 * @Auther: dongqile
 * @Date: 2019/12/3
 * @Description:
 */
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingTest {

    @Test
    public void setStaking(){
        Staking staking = new Staking(BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN);
        log.debug("staking : {}", JSON.toJSONString(staking));
    }

    @Test
    public void getSatking(){
        Staking staking = new Staking(BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN);
        staking.getMaxValidators();
        staking.getOperatingThreshold();
        staking.getStakeThreshold();
        staking.getUnStakeFreezeDuration();
        log.debug("value : {} ,{} ,{} ,{}",staking.getMaxValidators(),staking.getOperatingThreshold(),staking.getStakeThreshold(),staking.getUnStakeFreezeDuration());

    }
}