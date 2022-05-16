package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.DelegationReward;
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
 * @Date: 2020/01/07 13:41:25
 * @Description: ES服务: 委托奖励服务
 */
@Slf4j
@Service
public class EsDelegateRewardService implements EsService<DelegationReward> {

    @Resource
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Override
    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<DelegationReward> data) throws IOException {
        if (data.isEmpty()) {
            return;
        }
        try {
            Map<String, DelegationReward> map = new HashMap<>();
            // 使用(<hash>)作ES的docId
            data.forEach(e -> map.put(e.getHash(), e));
            ESDelegationRewardRepository.bulkAddOrUpdate(map);
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(e.getMessage());
        }
    }

}
