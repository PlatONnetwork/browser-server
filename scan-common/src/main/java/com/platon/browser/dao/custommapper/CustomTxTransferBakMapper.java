package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.TxTransferBak;
import com.platon.browser.elasticsearch.dto.ErcTx;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface CustomTxTransferBakMapper {

    int batchInsert(@Param("set") Set<TxTransferBak> set);

    long findMaxId();
}
