package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.NOptBak;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomNOptBakMapper {
    int batchInsertOrUpdateSelective(@Param("list") List<NOptBak> list, @Param("selective") NOptBak.Column... selective);
}