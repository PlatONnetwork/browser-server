package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.common.queue.gasestimate.event.GasEstimateEpoch;
import com.platon.browser.elasticsearch.GasEstimateEpochESRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2020/01/14 18:34:37
 * @Description: ES服务: 委托奖励服务
 */
@Slf4j
@Service
public class EsGasEstimateEpochService implements EsService<GasEstimateEpoch>{
    @Autowired
    private GasEstimateEpochESRepository gasEstimateEpochESRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<GasEstimateEpoch> data) throws IOException {
        if(data.isEmpty()) return;
        try {
            Map<String,GasEstimateEpoch> map = new HashMap<>();
            // 使用(<nodeId+sbn+addr>)作ES的docId
            data.forEach(e->map.put(e.getNodeId()+e.getSbn()+e.getAddr(),e));
            gasEstimateEpochESRepository.bulkAddOrUpdate(map);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}
