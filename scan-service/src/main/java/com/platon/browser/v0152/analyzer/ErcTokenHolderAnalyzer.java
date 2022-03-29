package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderKey;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.utils.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    /**
     * 解析Token Holder
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze(List<ErcTx> txList) {
        List<TokenHolder> insertOrUpdate = new ArrayList<>();
        txList.forEach(tx -> {
            resolveTokenHolder(tx.getContract(), tx.getFrom(), insertOrUpdate);
            resolveTokenHolder(tx.getContract(), tx.getTo(), insertOrUpdate);
        });
        if (CollUtil.isNotEmpty(insertOrUpdate)) {
            customTokenHolderMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenHolder.Column.values());
        }
    }

    private void resolveTokenHolder(String tokenAddress, String userAddress, List<TokenHolder> insertOrUpdate) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(userAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", userAddress);
            return;
        }
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
            tokenHolder.setBalance("0");
        } else {
            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
        }
        //TokenTxQty： 用户对该erc20的交易总数，或者是用户对该erc721所有tokenId的交易总数
        log.info("该合约地址[{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getAddress(), tokenHolder.getTokenTxQty());
        tokenHolder.setUpdateTime(date);
        insertOrUpdate.add(tokenHolder);
    }

}
