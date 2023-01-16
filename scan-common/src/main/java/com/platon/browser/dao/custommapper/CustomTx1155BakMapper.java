package com.platon.browser.dao.custommapper;

import com.platon.browser.elasticsearch.dto.ErcTx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomTx1155BakMapper {

    int batchInsert(@Param("list") List<ErcTx> txList);

    long findMaxId();
}
