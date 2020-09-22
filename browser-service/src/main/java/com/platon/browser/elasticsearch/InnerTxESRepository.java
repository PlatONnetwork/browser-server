package com.platon.browser.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/***
*@program: InnerTxESRepository.java
*@description: 
*@author: Rongjin Zhang
*@create: 2020/9/22
*/
@Repository
public class InnerTxESRepository extends ESRepository {
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
