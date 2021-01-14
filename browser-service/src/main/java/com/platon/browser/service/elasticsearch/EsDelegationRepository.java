package com.platon.browser.service.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 交易操作
 */
@Repository
public class EsDelegationRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getDelegationIndexName();
    }
    @Override
    public String getTemplateFileName() {
        return "delegate";
    }
}
