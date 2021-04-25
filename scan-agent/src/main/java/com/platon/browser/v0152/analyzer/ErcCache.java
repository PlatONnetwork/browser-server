package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ErcCache {
    Map<String, ErcToken> tokenCache = new ConcurrentHashMap<>();
    Set<String> erc20AddressCache = new ConcurrentHashSet<>();
    Set<String> erc721AddressCache = new ConcurrentHashSet<>();
    @Resource
    private TokenMapper tokenMapper;

    public void init() {
        List<Token> tokens = tokenMapper.selectByExample(null);
        tokens.forEach(token -> {
            ErcToken et = new ErcToken();
            BeanUtils.copyProperties(token, et);
            ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
            et.setTypeEnum(typeEnum);
            tokenCache.put(et.getAddress(),et);
            switch (typeEnum){
                case ERC20:
                    erc20AddressCache.add(token.getAddress());
                    break;
                case ERC721:
                    erc721AddressCache.add(token.getAddress());
                    break;
            }
        });
    }

    public Map<String,ErcToken> getTokenCache() {
        return Collections.unmodifiableMap(tokenCache);
    }
    public Collection<String> getErc20AddressCache() {
        return Collections.unmodifiableCollection(erc20AddressCache);
    }

    public Collection<String> getErc721AddressCache() {
        return Collections.unmodifiableCollection(erc721AddressCache);
    }
}
