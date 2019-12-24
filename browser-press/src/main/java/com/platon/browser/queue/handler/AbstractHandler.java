package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import lombok.Data;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public abstract class AbstractHandler<T> implements EventHandler<T> {
    private Logger logger;
    private long totalCount = 0L;

    protected double printTps(String name,long recordCount,long startTimeMillis,long endTimeMillis){
        totalCount = totalCount+recordCount;
        BigDecimal timeDiffMillis = BigDecimal.valueOf(endTimeMillis-startTimeMillis);
        BigDecimal timeDiffSecond = timeDiffMillis.divide(BigDecimal.valueOf(1000),8,RoundingMode.FLOOR);
        if(timeDiffSecond.compareTo(BigDecimal.ZERO)==0) timeDiffSecond=BigDecimal.ONE;
        BigDecimal tps = BigDecimal.valueOf(recordCount)
                .divide(timeDiffSecond,16, RoundingMode.FLOOR);
        double dtps = tps.setScale(2,RoundingMode.FLOOR).doubleValue();
        logger.info("{}总量:{},当前量:{},入库耗时:{}s,tps:{}",name,totalCount,recordCount,timeDiffSecond,dtps);
        return dtps;
    }
}
