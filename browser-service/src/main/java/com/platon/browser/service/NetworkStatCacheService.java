package com.platon.browser.service;

import com.platon.browser.dao.entity.NetworkStat;

import java.util.Set;

public interface NetworkStatCacheService {
    void clear();
    void update( Set <NetworkStat> items);
}
