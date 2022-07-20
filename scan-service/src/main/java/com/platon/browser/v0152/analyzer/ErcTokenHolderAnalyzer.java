package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.cache.AddressCache;
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

    @Resource
    private AddressCache addressCache;

    private TokenHolderKey getTokenHolderKey(String ownerAddress, ErcTx ercTx) {
        TokenHolderKey key = new TokenHolderKey();
        key.setTokenAddress(ercTx.getContract());
        key.setAddress(ownerAddress);
        key.setTokenId(ercTx.getTokenId());
        return key;
    }
    /**
     * 解析Token Holder
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze(List<ErcTx> txList) {
        List<TokenHolder> insertOrUpdate = new ArrayList<>();
        txList.forEach(tx -> {
            resolveTokenHolder(tx.getFrom(), tx, insertOrUpdate);
            resolveTokenHolder(tx.getTo(), tx, insertOrUpdate);
        });
        if (CollUtil.isNotEmpty(insertOrUpdate)) {
            customTokenHolderMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenHolder.Column.values());
        }
    }

    private void resolveTokenHolder(String ownerAddress, ErcTx ercTx, List<TokenHolder> insertOrUpdate) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        Date date = new Date();
        TokenHolderKey key = getTokenHolderKey(ownerAddress, ercTx);
        TokenHolder tokenHolder = tokenHolderMapper.selectByPrimaryKey(key);
        if (tokenHolder == null) {
            tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            tokenHolder.setCreateTime(date);
            tokenHolder.setTokenTxQty(1);
            tokenHolder.setBalance("0");
            tokenHolder.setTokenId(ercTx.getTokenId());
        } else {
            // 旧数据表没有 tokenId 字段，所以获取到的 tokenId 为 null
            // 现在为了兼容旧数据，要判断合约是 ERC721 还是 ERC20
            // 如果是 ERC20 tokenId 则设置为 0
            // 旧数据表的 RC721 的 tokenId 字段放在 balance 字段， 因此 ERC721 要把 balance 字段的值设置到 tokenId 中，同时 balance 设置为 0
            if (tokenHolder.getTokenId() == null) {
                if (addressCache.isErc20ContractAddress(tokenHolder.getTokenAddress())) {
                    tokenHolder.setTokenId("0");
                } else if (addressCache.isErc721ContractAddress(tokenHolder.getTokenAddress())) {
                    tokenHolder.setTokenId(tokenHolder.getBalance());
                    tokenHolder.setBalance("0");
                }
            }

            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
        }
        //TokenTxQty： 用户对该erc20的交易总数，或者是用户对该erc721, erc1155所有tokenId的交易总数
        log.info("该合约地址[{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getAddress(), tokenHolder.getTokenTxQty());
        tokenHolder.setUpdateTime(date);
        insertOrUpdate.add(tokenHolder);
    }

}
