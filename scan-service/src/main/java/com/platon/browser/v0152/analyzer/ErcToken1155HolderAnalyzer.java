package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.entity.Token1155Holder;
import com.platon.browser.dao.entity.Token1155HolderKey;
import com.platon.browser.dao.mapper.Token1155HolderMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.utils.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Erc721 token 持有者服务
 */
@Slf4j
@Service
public class ErcToken1155HolderAnalyzer {

    @Resource
    private Token1155HolderMapper token1155HolderMapper;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private AddressCache addressCache;

    private Token1155HolderKey getTokenHolderKey(String ownerAddress, ErcTx ercTx) {
        Token1155HolderKey key = new Token1155HolderKey();
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
        List<Token1155Holder> insertOrUpdate = new ArrayList<>();
        txList.forEach(tx -> {
            resolveTokenHolder(false, tx.getFrom(), tx, insertOrUpdate);
            resolveTokenHolder(true, tx.getTo(), tx, insertOrUpdate);
        });
        if (CollUtil.isNotEmpty(insertOrUpdate)) {
            customTokenHolderMapper.batchInsertOrUpdateSelective1155(insertOrUpdate, Token1155Holder.Column.values());
        }
    }

    /**
     * 解析
     *
     * @param to:             是否为to地址
     * @param ownerAddress:   地址
     * @param ercTx:          erc交易
     * @param insertOrUpdate: 更新列表
     * @return: void
     * @date: 2022/8/1
     */
    private void resolveTokenHolder(boolean to, String ownerAddress, ErcTx ercTx, List<Token1155Holder> insertOrUpdate) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        Token1155HolderKey key = getTokenHolderKey(ownerAddress, ercTx);
        Token1155Holder tokenHolder = token1155HolderMapper.selectByPrimaryKey(key);
        if (tokenHolder == null) {
            tokenHolder = new Token1155Holder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            tokenHolder.setBalance("0");
            tokenHolder.setTokenId(ercTx.getTokenId());
        }
        // to地址对合约交易次数加1
        if (to && ercTx.getTo().equalsIgnoreCase(ownerAddress)) {
            int tokenOwnerTxQty = tokenHolder.getTokenOwnerTxQty() == null ? 0 : tokenHolder.getTokenOwnerTxQty();
            tokenHolder.setTokenOwnerTxQty(tokenOwnerTxQty + 1);
        } else {
            tokenHolder.setTokenOwnerTxQty(1);
        }
        //TokenTxQty： 用户对该erc20的交易总数，或者是用户对该erc721, erc1155所有tokenId的交易总数
        log.info("该1155合约地址[{}][{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getTokenId(), tokenHolder.getAddress(), tokenHolder.getTokenOwnerTxQty());
        insertOrUpdate.add(tokenHolder);
    }

}
