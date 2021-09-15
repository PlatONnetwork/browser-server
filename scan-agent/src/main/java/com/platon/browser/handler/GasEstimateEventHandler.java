package com.platon.browser.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.bean.GasEstimateEvent;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class GasEstimateEventHandler implements EventHandler<GasEstimateEvent> {

    @Resource
    private GasEstimateLogMapper gasEstimateLogMapper;

    @Resource
    private EpochBusinessMapper epochBusinessMapper;

    private Long prevSeq = 0L;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        try {
            if (prevSeq.equals(event.getSeq())) {
                // 如果当前序列号等于前一次的序列号，证明消息已经处理过
                return;
            }
            List<GasEstimate> estimateList = event.getEstimateList();
            if (estimateList != null && !estimateList.isEmpty()) {
                epochBusinessMapper.updateGasEstimate(estimateList);
            }
            // es入库完成后删除mysql中的日志记录
            gasEstimateLogMapper.deleteByPrimaryKey(event.getSeq());
            prevSeq = event.getSeq();
        } catch (Exception e) {
            log.error("", e);
        }
    }

}