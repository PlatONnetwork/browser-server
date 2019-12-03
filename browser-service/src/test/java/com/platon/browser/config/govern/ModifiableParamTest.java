package com.platon.browser.config.govern;

import com.platon.browser.dao.entity.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/3
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ModifiableParamTest{


    @Test
    public void init(){
        List <Config> configList = new ArrayList <>();
        Config config = new Config();
        config.setInitValue("100000000");
        config.setActiveBlock(1L);
        config.setId(1);
        config.setModule("testModule");
        config.setName("stakeThreshold");
        config.setRangeDesc("testDec");
        config.setStaleValue("10000000");
        config.setValue("10000000");
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        configList.add(config);
        Block b  = Block.builder()
                .maxBlockGasLimit(BigDecimal.ZERO)
                .build();
        Slashing s = Slashing.builder()
                .maxEvidenceAge(BigDecimal.ZERO)
                .duplicateSignReportReward(BigDecimal.ZERO)
                .slashBlocksReward(BigDecimal.ZERO)
                .slashFractionDuplicateSign(BigDecimal.ZERO)
                .build();
        Staking st = Staking.builder()
                .maxValidators(BigDecimal.ZERO)
                .operatingThreshold(BigDecimal.ZERO)
                .stakeThreshold(BigDecimal.ZERO)
                .unStakeFreezeDuration(BigDecimal.ZERO)
                .build();
        ModifiableParam target = new ModifiableParam(st,s,b);
        target.init(configList);
    }
}