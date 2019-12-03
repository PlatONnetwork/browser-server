package com.platon.browser.config.govern;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 可修改的区块参数配置
 * @author: chendongming@juzix.net
 * @create: 2019-11-25 18:32:13
 **/
@Data
@Builder
public class Block {
    private BigDecimal maxBlockGasLimit;
}
