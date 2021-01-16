package com.platon.browser.v0151.cache;

import com.platon.browser.dao.entity.Token;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
@Component
public class ErcCache {
    private Map<String, Token> tokenMap = new ConcurrentHashMap<>();
    private Set<String> erc20AddressCache = new ConcurrentSkipListSet<>();
    private Set<String> erc721AddressCache = new ConcurrentSkipListSet<>();
}
