package com.platon.browser.common.queue.gasestimate.handler;

import com.platon.browser.common.queue.gasestimate.event.GasEstimateEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class GasEstimateEventHandler implements IGasEstimateEventHandler {

    @Autowired
    private GasEstimateLogMapper gasEstimateLogMapper;

    @Autowired
    private EpochBusinessMapper epochBusinessMapper;

    @Override
    public void onEvent(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        try {
            List<GasEstimate> estimateList = event.getEstimateList();
            if(estimateList!=null&&!estimateList.isEmpty()){
                epochBusinessMapper.updateGasEstimate(estimateList);
            }
            // es入库完成后删除mysql中的日志记录
            gasEstimateLogMapper.deleteByPrimaryKey(event.getSeq());
        }catch (Exception e){
            log.error("",e);
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}