package com.platon.browser.service.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 针对处理合约内部转账记录的ES处理器
 */
@Repository
@Slf4j
public class EsTransferTxRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getTransferTxIndexName();
    }
    @Override
    public String getTemplateFileName() {
        return "transfer-tx";
    }
}
