package com.platon.browser.queue.handler;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class AbstractHandler {

    protected long totalCount = 0L;

    protected double printTps(String name,long recordCount,long startTimeMillis,long endTimeMillis){
        totalCount = totalCount+recordCount;
        BigDecimal timeDiffMillis = BigDecimal.valueOf(endTimeMillis-startTimeMillis);
        BigDecimal timeDiffSecond = timeDiffMillis.divide(BigDecimal.valueOf(1000),8,RoundingMode.FLOOR);
        if(timeDiffSecond.compareTo(BigDecimal.ZERO)==0) timeDiffSecond=BigDecimal.ONE;
        BigDecimal tps = BigDecimal.valueOf(recordCount)
                .divide(timeDiffSecond,16, RoundingMode.FLOOR);
        double dtps = tps.setScale(2,RoundingMode.FLOOR).doubleValue();
        log.info("{}总量:{},当前量:{},入库耗时:{}s,tps:{}",name,totalCount,recordCount,timeDiffSecond,dtps);
        return dtps;
    }
}
