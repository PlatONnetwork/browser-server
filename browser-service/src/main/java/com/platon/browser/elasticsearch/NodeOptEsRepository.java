package com.platon.browser.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 节点日志操作
 */
@Repository
public class NodeOptEsRepository extends EsRepository {
    @Override
    public String getIndexName() {
        return config.getNodeOptIndexName();
    }
}
