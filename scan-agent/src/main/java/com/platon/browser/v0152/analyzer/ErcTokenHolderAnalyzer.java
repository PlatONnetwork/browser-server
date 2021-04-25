package com.platon.browser.v0152.analyzer;

import com.platon.bech32.Bech32;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderKey;
import com.platon.browser.dao.mapper.CustomTokenHolderMapper;
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

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    private void resolveTokenHolder(
            String tokenAddress,
            String userAddress,
            List<TokenHolder> insertOrUpdate
    ) {
        String hex = Bech32.addressDecodeHex(userAddress);
        // 零地址不需要創建holder
        if ("0x0000000000000000000000000000000000000000".equals(hex))
            return;
        Date date = new Date();
        TokenHolderKey key = new TokenHolderKey();
        key.setTokenAddress(tokenAddress);
        key.setAddress(userAddress);
        TokenHolder tokenHolder = tokenHolderMapper.selectByPrimaryKey(key);
        if (tokenHolder == null) {
            tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            tokenHolder.setCreateTime(date);
            tokenHolder.setTokenTxQty(1);
            tokenHolder.setBalance(BigDecimal.ZERO);
        } else {
            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
        }
        tokenHolder.setUpdateTime(date);
        insertOrUpdate.add(tokenHolder);
    }

    /**
     * 解析Token Holder
     */
    public void analyze(List<ErcTx> txList) {
        List<TokenHolder> insertOrUpdate = new ArrayList<>();
        txList.forEach(tx -> {
            resolveTokenHolder(tx.getContract(), tx.getFrom(), insertOrUpdate);
            resolveTokenHolder(tx.getContract(), tx.getTo(), insertOrUpdate);
        });
        if (!insertOrUpdate.isEmpty())
            customTokenHolderMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenHolder.Column.values());
    }

}
