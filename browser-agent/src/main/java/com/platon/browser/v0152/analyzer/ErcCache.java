package com.platon.browser.v0152.analyzer;

import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ErcCache {
    Map<String, ErcToken> tokenCache = new ConcurrentHashMap<>();
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
        });
    }

    public Collection<ErcToken> getTokenCache() {
        return Collections.unmodifiableCollection(tokenCache.values());
    }

}
