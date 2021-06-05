package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.TxBak;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTxBakMapper {
    int batchInsertOrUpdateSelective(@Param("list") List<TxBak> list, @Param("selective") TxBak.Column... selective);
}