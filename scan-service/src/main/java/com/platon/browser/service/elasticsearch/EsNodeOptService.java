package com.platon.browser.service.elasticsearch;

import com.platon.browser.dao.entity.NOptBak;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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
public class EsNodeOptService implements EsService<NOptBak> {

    @Resource
    private EsNodeOptRepository ESNodeOptRepository;

    @Override
    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<NOptBak> nodeOpts) throws IOException {
        if (nodeOpts.isEmpty()) {
            return;
        }
        try {
            Map<String, NOptBak> nodeOptMap = new HashMap<>();
            // 使用(<id>)作ES的docId
            nodeOpts.forEach(n -> nodeOptMap.put(n.getId().toString(), n));
            ESNodeOptRepository.bulkAddOrUpdate(nodeOptMap);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(e.getMessage());
        }
    }

}
