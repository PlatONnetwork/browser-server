package com.platon.browser.bean.govern;

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
    public void satkingTest(){
        Staking staking = Staking.builder()
                .unStakeFreezeDuration(BigDecimal.TEN)
                .stakeThreshold(BigDecimal.TEN)
                .operatingThreshold(BigDecimal.TEN)
                .maxValidators(BigDecimal.TEN)
                .build();
        staking.setMaxValidators(BigDecimal.ONE);
        staking.setOperatingThreshold(BigDecimal.ONE);
        staking.setStakeThreshold(BigDecimal.ONE);
        staking.setUnStakeFreezeDuration(BigDecimal.ONE);
        log.debug("staking : {}", JSON.toJSONString(staking));
        staking.getMaxValidators();
        staking.getOperatingThreshold();
        staking.getStakeThreshold();
        staking.getUnStakeFreezeDuration();
        log.debug("value : {} ,{} ,{} ,{}",staking.getMaxValidators(),staking.getOperatingThreshold(),staking.getStakeThreshold(),staking.getUnStakeFreezeDuration());

    }
}
