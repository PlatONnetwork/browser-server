package com.platon.browser.service.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 区块操作
 */
@Repository
public class EsBlockRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getBlockIndexName();
    }
    @Override
    public String getTemplateFileName() {
        return "block";
    }
}
