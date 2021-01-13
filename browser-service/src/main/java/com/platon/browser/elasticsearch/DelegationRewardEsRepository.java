package com.platon.browser.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 委托奖励操作
 */
@Repository
public class DelegationRewardEsRepository extends EsRepository {
    @Override
    public String getIndexName() {
        return config.getDelegationRewardIndexName();
    }
}
