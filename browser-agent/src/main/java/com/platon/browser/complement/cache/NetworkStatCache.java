package com.platon.browser.complement.cache;

import org.springframework.stereotype.Component;

import com.platon.browser.dao.entity.NetworkStat;

import lombok.Data;

@Component
@Data
public class NetworkStatCache {
    private NetworkStat networkStat;
}
