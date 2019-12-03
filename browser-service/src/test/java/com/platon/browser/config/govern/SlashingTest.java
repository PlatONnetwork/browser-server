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
@RunWith(MockitoJUnitRunner.Silent.class)
@Slf4j
public class SlashingTest {


    @Test
    public void setSlashing(){
        Slashing slashing = new Slashing(BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN);
        log.debug("slashing : {}", JSON.toJSONString(slashing));
    }

    @Test
    public void getSlashing(){
        Slashing slashing = new Slashing(BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN);
        slashing.getDuplicateSignReportReward();
        slashing.getMaxEvidenceAge();
        slashing.getSlashBlocksReward();
        slashing.getSlashFractionDuplicateSign();
        log.debug("value : {} ,{} ,{} ,{}",slashing.getDuplicateSignReportReward(),slashing.getMaxEvidenceAge(),slashing.getSlashBlocksReward(),slashing.getSlashFractionDuplicateSign());
    }
}