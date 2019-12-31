package com.platon.browser.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 委托奖励操作
 */
@Repository
public class DelegationRewardESRepository extends ESRepository {
    @Value("${spring.elasticsearch.high-level-client.delegationRewardIndexName}")
    private String indexName;

    @Override
    public String getIndexName() {
        return indexName;
    }
}
