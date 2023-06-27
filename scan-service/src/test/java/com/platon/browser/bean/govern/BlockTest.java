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
@RunWith(MockitoJUnitRunner.Silent.class)
@Slf4j
public class BlockTest {

    @Test
    public void blockTest(){
        Block block = Block.builder()
                .maxBlockGasLimit(BigDecimal.TEN)
                .build();
        block.setMaxBlockGasLimit(BigDecimal.ONE);
        log.debug("staking : {}", JSON.toJSONString(block));
        block.getMaxBlockGasLimit();
        log.debug("value : {} ",block.getMaxBlockGasLimit());

    }
}
