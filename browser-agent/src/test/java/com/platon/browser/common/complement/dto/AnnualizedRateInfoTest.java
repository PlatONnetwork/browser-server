package com.platon.browser.common.complement.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/5 15:09
 * @Description: 年化率信息bean单元测试
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AnnualizedRateInfoTest {

    @Test
    public void test(){
        PeriodValueElement  profit = PeriodValueElement.builder()
                .period(100L)
                .value(BigDecimal.TEN)
                .build();
        List<PeriodValueElement> profitList = new ArrayList <>();
        profitList.add(profit);
        PeriodValueElement cost = PeriodValueElement.builder()
                .period(120L)
                .value(BigDecimal.TEN)
                .build();
        List<PeriodValueElement> costList = new ArrayList <>();
        costList.add(cost);
        SlashInfo slash = SlashInfo.builder()
                .slashTime(new Date())
                .blockCount(BigInteger.ONE)
                .blockNumber(BigInteger.ONE)
                .kickOut(false)
                .slashAmount(BigDecimal.TEN)
                .slashBlockCount(BigInteger.ONE)
                .build();
        List<SlashInfo> slashList = new ArrayList <>();
        slashList.add(slash);
        AnnualizedRateInfo annualizedRateInfo = AnnualizedRateInfo.builder()
                .cost(profitList)
                .profit(costList)
                .slash(slashList)
                .build();
        String annuaString = annualizedRateInfo.toJSONString();
    }


}
