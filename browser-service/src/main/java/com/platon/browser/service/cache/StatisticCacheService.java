package com.platon.browser.service.cache;

import com.platon.browser.dto.StatisticsCache;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticCacheService {
    void clearStatisticsCache(String chainId);
    boolean updateStatisticsCache(String chainId);
    StatisticsCache getStatisticsCache(String chainId);
    boolean updateTransCount(String chainId);
    long getTransCount(String chainId);
    boolean updateTransCount24Hours(String chainId);
    long getTransCount24Hours(String chainId);
    boolean updateAddressCount(String chainId);
    long getAddressCount(String chainId);
    boolean updateAvgBlockTransCount(String chainId);
    BigDecimal getAvgBlockTransCount(String chainId);
    boolean updateTicketPrice(String chainId);
    BigDecimal getTicketPrice(String chainId);
    boolean updateVoteCount(String chainId);
    long getVoteCount(String chainId);
    boolean updateMaxTps(String chainId, String value);
    long getMaxTps(String chainId);
}
