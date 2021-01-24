package com.platon.browser.v0152.cache;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Component
public class ErcCache {
    private Set<ErcToken> erc20TokenCache = new CopyOnWriteArraySet<>();
    private Set<ErcToken> erc721TokenCache = new CopyOnWriteArraySet<>();
    private Set<String> holderCache = new CopyOnWriteArraySet<>();
    private Set<String> erc20AddressCache = new CopyOnWriteArraySet<>();
    private Set<String> erc721AddressCache = new CopyOnWriteArraySet<>();
    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private TokenHolderMapper tokenHolderMapper;
    public void init(){
        List<Token> tokens = tokenMapper.selectByExample(null);
        tokens.forEach(token->{
            ErcToken et = new ErcToken();
            BeanUtils.copyProperties(token,et);
            ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
            et.setTypeEnum(typeEnum);
            switch (typeEnum){
                case ERC721:
                    erc721TokenCache.add(et);
                    erc721AddressCache.add(et.getAddress());
                    break;
                case ERC20:
                    erc20TokenCache.add(et);
                    erc20AddressCache.add(et.getAddress());
                    break;
            }
        });

        List<TokenHolder> holders = tokenHolderMapper.selectByExample(null);
        holders.forEach(holder->holderCache.add(holder.getAddress()));
    }
}
