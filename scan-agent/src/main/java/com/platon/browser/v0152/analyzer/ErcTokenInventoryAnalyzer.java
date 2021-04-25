package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.mapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
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

    @Resource
    private CustomTokenInventoryMapper customTokenInventoryMapper;

    /**
     * 0地址
     */
    private static final String TO_ADDR_ZERO = "0x0000000000000000000000000000000000000000";

    /**
     * 解析Token库存
     */
    public void analyze(List<ErcTx> txList) {
        List<TokenInventory> insertOrUpdate = new ArrayList<>();
        List<TokenInventoryKey> delTokenInventory = new ArrayList<>();
        Date date = new Date();
        if (CollUtil.isNotEmpty(txList)) {
            txList.forEach(tx -> {
                TokenInventoryKey key = new TokenInventory();
                key.setTokenAddress(tx.getContract());
                key.setTokenId(tx.getValue());
                TokenInventory tokenInventory = tokenInventoryMapper.selectByPrimaryKey(key);
                if (tokenInventory == null) {
                    tokenInventory = new TokenInventory();
                    tokenInventory.setTokenAddress(key.getTokenAddress());
                    tokenInventory.setTokenId(key.getTokenId());
                    tokenInventory.setCreateTime(date);
                    tokenInventory.setTokenTxQty(1);
                } else {
                    tokenInventory.setTokenTxQty(tokenInventory.getTokenTxQty() + 1);
                }
                if (tx.getTo().equalsIgnoreCase(tokenInventory.getOwner())) {
                    int tokenOwnerTxQty = tokenInventory.getTokenOwnerTxQty() == null ? 0 : tokenInventory.getTokenOwnerTxQty();
                    tokenInventory.setTokenOwnerTxQty(tokenOwnerTxQty + 1);
                } else {
                    tokenInventory.setTokenOwnerTxQty(1);
                }
                tokenInventory.setOwner(tx.getTo());
                tokenInventory.setUpdateTime(date);
                insertOrUpdate.add(tokenInventory);
                // 如果合约交易当中，to地址是0地址的话，需要清除TokenInventory记录
                if (StrUtil.isNotBlank(tx.getTo()) && TO_ADDR_ZERO.equalsIgnoreCase(tx.getTo())) {
                    TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
                    tokenInventoryKey.setTokenId(tx.getValue());
                    tokenInventoryKey.setTokenAddress(tx.getContract());
                    delTokenInventory.add(tokenInventoryKey);
                }
            });
            if (!insertOrUpdate.isEmpty()) {
                customTokenInventoryMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenInventory.Column.values());
            }
            if (CollUtil.isNotEmpty(delTokenInventory)) {
                customTokenInventoryMapper.burnAndDelTokenInventory(delTokenInventory);
            }
        }
    }

}
