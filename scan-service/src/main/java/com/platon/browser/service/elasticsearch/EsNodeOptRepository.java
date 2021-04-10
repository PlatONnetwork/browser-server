package com.platon.browser.service.elasticsearch;

import org.springframework.stereotype.Repository;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: 节点日志操作
 */
@Repository
public class EsNodeOptRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getNodeOptIndexName();
    }
    @Override
    public String getTemplateFileName() {
        return "nodeopt";
    }
}
