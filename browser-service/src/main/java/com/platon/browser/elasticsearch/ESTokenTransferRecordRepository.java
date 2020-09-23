package com.platon.browser.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * 针对处理合约内部转账记录的ES处理器
 */
@Repository
public class ESTokenTransferRecordRepository extends ESRepository {
    @Value("${spring.elasticsearch.high-level-client.innerTxIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
    
    public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
}
