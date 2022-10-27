package com.platon.browser.service.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * ERC20交易记录ES操作类
 */
@Repository
@Slf4j
public class EsErc1155TxRepository extends AbstractEsRepository {
    @Override
    public String getIndexName() {
        return config.getErc1155TxIndexName();
    }
    @Override
    public String getTemplateFileName(){return "erc1155-tx";}
}
