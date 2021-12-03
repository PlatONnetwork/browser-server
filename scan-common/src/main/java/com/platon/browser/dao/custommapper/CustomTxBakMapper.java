package com.platon.browser.dao.custommapper;

import com.platon.browser.elasticsearch.dto.Transaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTxBakMapper {

    int batchInsertOrUpdateSelective(@Param("list") List<Transaction> list);

}