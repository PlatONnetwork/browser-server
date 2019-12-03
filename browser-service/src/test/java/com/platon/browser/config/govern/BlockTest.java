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
public class BlockTest {
    @Test
    public void setBlock(){
        Block block = new Block(BigDecimal.TEN);
        log.debug("staking : {}", JSON.toJSONString(block));
    }

    @Test
    public void getBlock(){
        Block block = new Block(BigDecimal.TEN);
        block.getMaxBlockGasLimit();
        log.debug("value : {} ",block.getMaxBlockGasLimit());

    }
}