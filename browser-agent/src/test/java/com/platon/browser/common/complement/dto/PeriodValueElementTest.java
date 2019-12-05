package com.platon.browser.common.complement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

/**
 * @Auther: dongqile
 * @Date: 2019/12/5 14:05
 * @Description: 利润和成本bean单元测试
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class PeriodValueElementTest {

    @Test
    public void test(){
        PeriodValueElement  profit = PeriodValueElement.builder()
                .period(100L)
                .value(BigDecimal.TEN)
                .build();
    }
}
