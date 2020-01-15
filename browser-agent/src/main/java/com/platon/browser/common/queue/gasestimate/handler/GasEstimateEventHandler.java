package com.platon.browser.common.queue.gasestimate.handler;

import com.platon.browser.common.queue.gasestimate.event.GasEstimateEvent;
import com.platon.browser.common.service.elasticsearch.EsGasEstimateEpochService;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class GasEstimateEventHandler implements IGasEstimateEventHandler {

    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private EsGasEstimateEpochService esGasEstimateEpochService;

    @Override
    public void onEvent(GasEstimateEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        try {
            switch (event.getAction()){
                case ADD:
                case UPDATE:
                    esGasEstimateEpochService.save(new HashSet<>(event.getEpoches()));
                    break;
                case DELETE:
                    esGasEstimateEpochService.delete(new HashSet<>(event.getEpoches()));
                    break;
            }
        }catch (Exception e){
            log.error("",e);
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}