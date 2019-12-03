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
    public void slashingTest(){
        Slashing slashing = new Slashing(BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN,BigDecimal.TEN);
        slashing.setDuplicateSignReportReward(BigDecimal.ONE);
        slashing.setMaxEvidenceAge(BigDecimal.ONE);
        slashing.setSlashBlocksReward(BigDecimal.ONE);
        slashing.setSlashFractionDuplicateSign(BigDecimal.ONE);
        log.debug("slashing : {}", JSON.toJSONString(slashing));
        slashing.getDuplicateSignReportReward();
        slashing.getMaxEvidenceAge();
        slashing.getSlashBlocksReward();
        slashing.getSlashFractionDuplicateSign();
        log.debug("value : {} ,{} ,{} ,{}",slashing.getDuplicateSignReportReward(),slashing.getMaxEvidenceAge(),slashing.getSlashBlocksReward(),slashing.getSlashFractionDuplicateSign());
    }
}