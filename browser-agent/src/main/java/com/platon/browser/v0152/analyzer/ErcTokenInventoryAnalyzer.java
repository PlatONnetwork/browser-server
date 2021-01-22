package com.platon.browser.v0152.analyzer;

import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Erc721 token 库存服务
 */
@Slf4j
@Service
public class ErcTokenInventoryAnalyzer {
    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    /**
     * 解析Token库存
     */
    public void analyze(List<ErcTx> txList) {
        List<TokenInventory> update = new ArrayList<>();
        List<TokenInventory> insert = new ArrayList<>();
        Date date = new Date();
        txList.forEach(tx->{
            TokenInventoryKey key = new TokenInventory();
            key.setTokenAddress(tx.getContract());
            key.setTokenId(Long.parseLong(tx.getValue()));
            TokenInventory tokenInventory = tokenInventoryMapper.selectByPrimaryKey(key);
            if(tokenInventory==null){
                tokenInventory = new TokenInventory();
                tokenInventory.setTokenAddress(key.getTokenAddress());
                tokenInventory.setTokenId(key.getTokenId());
                tokenInventory.setCreateTime(date);
                tokenInventory.setTokenTxQty(1);
                insert.add(tokenInventory);
            }else{
                tokenInventory.setTokenTxQty(tokenInventory.getTokenTxQty()+1);
                update.add(tokenInventory);
            }
            tokenInventory.setOwner(tx.getTo());
            tokenInventory.setUpdateTime(date);
        });
        if(!insert.isEmpty()) tokenInventoryMapper.batchInsert(insert);
        if(!update.isEmpty()) {
            update.forEach(ti->tokenInventoryMapper.updateByPrimaryKey(ti));
        }
    }
}
