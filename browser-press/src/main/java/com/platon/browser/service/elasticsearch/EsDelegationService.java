package com.platon.browser.service.elasticsearch;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.elasticsearch.DelegationESRepository;
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
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsDelegationService implements EsService<Delegation> {
    @Autowired
    private DelegationESRepository delegationESRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<Delegation> delegations) throws IOException {
        if(delegations.isEmpty()) return;
        try {
            Map<String,Delegation> delegationMap = new HashMap<>();
            // 使用(<节点ID>-<质押区块号>-<委托人地址>)作ES的docId
            delegations.forEach(d->delegationMap.put(d.getNodeId()+"-"+d.getStakingBlockNum()+"-"+d.getDelegateAddr(),d));
            delegationESRepository.bulkAddOrUpdate(delegationMap);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}
