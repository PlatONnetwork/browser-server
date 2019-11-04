package com.platon.browser.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 交易操作
 */
@Repository
public class DelegationESRepository extends ESRepository {
    @Value("${elasticsearch.delegationIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
}
