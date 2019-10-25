package com.platon.browser.repository.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 交易操作
 */
@Repository
public class TransactionESRepository extends ESRepository {
    @Value("${elasticsearch.blockIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
}
