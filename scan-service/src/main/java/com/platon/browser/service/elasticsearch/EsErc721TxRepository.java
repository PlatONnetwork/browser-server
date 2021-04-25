package com.platon.browser.service.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * ERC20交易记录ES操作类
 */
@Repository
@Slf4j
public class EsErc721TxRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getErc721TxIndexName();
    }
    @Override
    public String getTemplateFileName() {
        return "erc721-tx";
    }
}
