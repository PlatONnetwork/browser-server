package com.platon.browser.repository.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 节点日志操作
 */
@Repository
public class NodeOptESRepository extends ESRepository {
    @Value("${elasticsearch.nodeOptIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
}
