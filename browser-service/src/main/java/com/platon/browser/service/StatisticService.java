package com.platon.browser.service;

import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;

public interface StatisticService {
    IndexInfo getIndexInfo(String chainId);
    StatisticInfo getStatisticInfo(String chainId);
}
