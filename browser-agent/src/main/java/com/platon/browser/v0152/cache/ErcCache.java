package com.platon.browser.v0152.cache;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.v0152.bean.ErcToken;
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
    private Map<String, ErcToken> tokenMap = new ConcurrentHashMap<>();
    private Set<String> erc20AddressCache = new ConcurrentSkipListSet<>();
    private Set<String> erc721AddressCache = new ConcurrentSkipListSet<>();
    @Resource
    private TokenMapper tokenMapper;
    public void init(){
        List<Token> tokens = tokenMapper.selectByExample(null);
        tokens.forEach(token->{
            ErcToken et = new ErcToken();
            BeanUtils.copyProperties(token,et);
            ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
            et.setTypeEnum(typeEnum);
            tokenMap.put(et.getAddress(),et);
            switch (typeEnum){
                case ERC721:
                    erc721AddressCache.add(et.getAddress());
                    break;
                case ERC20:
                    erc20AddressCache.add(et.getAddress());
                    break;
            }
        });
    }
}
