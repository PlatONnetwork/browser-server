package com.platon.browser.v0152.cache;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.bean.ErcTokenCacheItem;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
@Component
public class ErcCache {
    private Map<String, ErcTokenCacheItem> tokenCache = new ConcurrentHashMap<>();
    private Set<String> erc20AddressCache = new ConcurrentSkipListSet<>();
    private Set<String> erc721AddressCache = new ConcurrentSkipListSet<>();
    @Resource
    private TokenMapper tokenMapper;
    public void init(){
        List<Token> tokens = tokenMapper.selectByExample(null);
        tokens.forEach(token->{
            ErcTokenCacheItem cacheItem = new ErcTokenCacheItem();
            BeanUtils.copyProperties(token,cacheItem);
            ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
            cacheItem.setTypeEnum(typeEnum);
            tokenCache.put(cacheItem.getAddress(),cacheItem);
            switch (typeEnum){
                case ERC721:
                    erc721AddressCache.add(cacheItem.getAddress());
                    break;
                case ERC20:
                    erc20AddressCache.add(cacheItem.getAddress());
                    break;
            }
        });
    }
}
