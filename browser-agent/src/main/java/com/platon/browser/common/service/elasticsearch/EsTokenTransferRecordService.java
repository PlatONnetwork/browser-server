package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 代币内部交易ES处理器
 *
 * @author AgentRJ
 * @create 2020-09-24 11:05
 */
@Service
@Slf4j
public class EsTokenTransferRecordService implements EsService<ESTokenTransferRecord> {

    @Autowired
    private TokenTransferRecordESRepository tokenTransferRecordESRepository;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<ESTokenTransferRecord> recordSet) {
        try {
            if (recordSet.isEmpty()) return;
            // key: _doc id
            Map<String, ESTokenTransferRecord> recordHashMap = new HashMap<>();
            recordSet.forEach(t-> {
                recordHashMap.put(generateUniqueDocId(t.getTxHash(), t.getTxFrom(), t.getTransferTo()), t);
            });
            tokenTransferRecordESRepository.bulkAddOrUpdate(recordHashMap);
        }catch (Exception e){
            log.error("Batch save data of ESTokenTransferRecord exception", e);
            throw new RuntimeException(e);
        }
    }

    public String generateUniqueDocId(String txHash, String from, String to) {
        return txHash.substring(0, txHash.length() / 2) + from.substring(0, from.length() / 2);
    }
}
