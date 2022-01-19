package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Block;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Data
public class CollectionBlock extends Block {

    private CollectionBlock() {
    }

    public static CollectionBlock newInstance() {
        CollectionBlock block = new CollectionBlock();
        Date date = new Date();
        block.setCreTime(date)
             .setUpdTime(date)
             .setDQty(0)
             .setPQty(0)
             .setSQty(0)
             .setTranQty(0)
             .setTxQty(0)
             .setReward(BigDecimal.ZERO.toString())
             .setGasLimit(BigDecimal.ZERO.toString())
             .setGasUsed(BigDecimal.ZERO.toString())
             .setTxFee(BigDecimal.ZERO.toString())
             .setSize(0)
             .setTxGasLimit(BigDecimal.ZERO.toString());
        return block;
    }

}
