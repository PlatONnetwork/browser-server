package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.TokenHolderBalanceRefreshLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TokenHolderBalanceRefreshLogMapper {
    int batchInsert(@Param("list") List<TokenHolderBalanceRefreshLog> list);
    List<TokenHolderBalanceRefreshLog> list(@Param("batchSize") int batchSize);
    int batchDelete(@Param("list") List<Long> idList);
}
