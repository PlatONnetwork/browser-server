package com.platon.browser.service.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 委托奖励操作
 */
@Repository
public class EsDelegationRewardRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getDelegationRewardIndexName();
    }

    @Override
    public String getTemplateFileName() {
        return "delegate-reward";
    }
}
