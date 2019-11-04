package com.platon.browser.persistence.service.elasticsearch;

import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

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
@Component
public class EsNodeOptService implements EsService<NodeOpt>{
    @Autowired
    private NodeOptESRepository nodeOptESRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(Set<NodeOpt> nodeOpts) throws IOException {
        if(nodeOpts.isEmpty()) return;
        try {
            Map<String,NodeOpt> nodeOptMap = new HashMap<>();
            // 使用(<id>)作ES的docId
            nodeOpts.forEach(n->nodeOptMap.put(n.getId().toString(),n));
            nodeOptESRepository.bulkAddOrUpdate(nodeOptMap);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}
