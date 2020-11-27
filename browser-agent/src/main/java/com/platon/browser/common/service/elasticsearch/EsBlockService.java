package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsBlockService implements EsService<Block>{
    @Autowired
    private BlockESRepository blockESRepository;
    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<Block> blocks) throws IOException {
        if(blocks.isEmpty()) return;
        try {
            Map<String,Block> blockMap = new HashMap<>();
            // 使用区块号作ES的docId
            blocks.forEach(b->blockMap.put(b.getNum().toString(),b));
            blockESRepository.bulkAddOrUpdate(blockMap);
        }catch (Exception e){
            log.error("",e);
            throw new BusinessException(e.getMessage());
        }
    }
}
