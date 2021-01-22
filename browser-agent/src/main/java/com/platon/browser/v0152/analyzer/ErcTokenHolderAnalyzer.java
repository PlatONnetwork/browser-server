package com.platon.browser.v0152.analyzer;

import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderKey;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Erc721 token 持有者服务
 */
@Slf4j
@Service
public class ErcTokenHolderAnalyzer {
    @Resource
    private TokenHolderMapper tokenHolderMapper;
    private void resolveTokenHolder(
            String tokenAddress,
            String userAddress,
            List<TokenHolder> update,
            List<TokenHolder> insert
    ){
        Date date = new Date();
        TokenHolderKey key = new TokenHolderKey();
        key.setTokenAddress(tokenAddress);
        key.setAddress(userAddress);
        TokenHolder tokenHolder = tokenHolderMapper.selectByPrimaryKey(key);
        if(tokenHolder==null){
            tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            tokenHolder.setCreateTime(date);
            tokenHolder.setTokenTxQty(1);
            tokenHolder.setBalance(BigDecimal.ZERO);
            insert.add(tokenHolder);
        }else{
            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty()+1);
            update.add(tokenHolder);
        }
        tokenHolder.setUpdateTime(date);
    }
    /**
     * 解析Token Holder
     */
    public void analyze(List<ErcTx> txList) {
        List<TokenHolder> update = new ArrayList<>();
        List<TokenHolder> insert = new ArrayList<>();
        txList.forEach(tx->{
            resolveTokenHolder(tx.getContract(),tx.getFrom(),update,insert);
            resolveTokenHolder(tx.getContract(),tx.getTo(),update,insert);
        });
        if(!insert.isEmpty()) tokenHolderMapper.batchInsert(insert);
        if(!update.isEmpty()) {
            update.forEach(th->tokenHolderMapper.updateByPrimaryKey(th));
        }
    }
}
