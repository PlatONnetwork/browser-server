package com.platon.browser.handler;

import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.bean.CommonConstant;
import com.platon.browser.bean.GasEstimateEvent;
import com.platon.browser.dao.custommapper.EpochBusinessMapper;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public void onEvent(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        surroundExec(event, sequence, endOfBatch);
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public void recover(Exception e) {
        retryCount.set(0);
        log.error("重试完成还是业务失败，请联系管理员处理");
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
            if (retryCount.incrementAndGet() > 1) {
                log.error("重试次数[{}],该seq[{}]重复处理，可能会引起数据重复统计，event对象数据为[{}]", retryCount.get(), event.getSeq(), JSONUtil.toJsonStr(event));
            }
            if (prevSeq.equals(event.getSeq())) {
                // 如果当前序列号等于前一次的序列号，证明消息已经处理过
                retryCount.set(0);
                return;
            }
            List<GasEstimate> estimateList = event.getEstimateList();
            if (estimateList != null && !estimateList.isEmpty()) {
                epochBusinessMapper.updateGasEstimate(estimateList);
            }
            // es入库完成后删除mysql中的日志记录
            gasEstimateLogMapper.deleteByPrimaryKey(event.getSeq());
            prevSeq = event.getSeq();
            retryCount.set(0);
        } catch (Exception e) {
            log.error("", e);
        }
    }

}