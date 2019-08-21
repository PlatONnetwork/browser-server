package com.platon.browser.service;

import com.platon.browser.dao.entity.NetworkStat;

public interface NetworkStatCacheService {
    void clear();
    void update(NetworkStat item);
}
