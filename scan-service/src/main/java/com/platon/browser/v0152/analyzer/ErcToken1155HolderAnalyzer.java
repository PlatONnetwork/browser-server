package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.dao.custommapper.CustomToken1155HolderMapper;
import com.platon.browser.dao.entity.Token1155Holder;
import com.platon.browser.dao.entity.Token1155HolderKey;
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
    private CustomToken1155HolderMapper customToken1155HolderMapper;

    /**
     * 解析Token Holder
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze(List<ErcTx> txList) {
        List<Token1155Holder> insert = new ArrayList<>();
        List<Token1155Holder> update = new ArrayList<>();
        txList.forEach(tx -> {
            resolveTokenHolder(tx.getFrom(), tx, insert, update);
            resolveTokenHolder(tx.getTo(), tx, insert, update);
        });

        if (CollUtil.isNotEmpty(insert)) {
            customToken1155HolderMapper.batchInsert(insert);
        }
        if (CollUtil.isNotEmpty(update)) {
            customToken1155HolderMapper.batchUpdateTokenOwnerTxQty(update);
        }
    }

    /**
     * 解析
     *
     * @param ownerAddress:   地址
     * @param ercTx:          erc交易
     * @return: void
     * @date: 2022/8/1
     */
    private void resolveTokenHolder(String ownerAddress, ErcTx ercTx, List<Token1155Holder> insert, List<Token1155Holder> update) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        Token1155HolderKey key = new Token1155HolderKey();
        key.setTokenAddress(ercTx.getContract());
        key.setAddress(ownerAddress);
        key.setTokenId(ercTx.getTokenId());
        Token1155Holder tokenHolder = customToken1155HolderMapper.selectByUK(key);
        if (tokenHolder == null) {
            tokenHolder = new Token1155Holder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            // 余额由定时任务更新，设置成默认值
            tokenHolder.setBalance("0");
            tokenHolder.setTokenId(ercTx.getTokenId());
            tokenHolder.setTokenOwnerTxQty(1);
            insert.add(tokenHolder);
        } else {
            int tokenOwnerTxQty = tokenHolder.getTokenOwnerTxQty() == null ? 0 : tokenHolder.getTokenOwnerTxQty();
            tokenHolder.setTokenOwnerTxQty(tokenOwnerTxQty + 1);
            update.add(tokenHolder);
        }
        log.info("该1155合约地址[{}][{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getTokenId(), tokenHolder.getAddress(), tokenHolder.getTokenOwnerTxQty());
    }

//    /**
//     * 解析Token Holder
//     */
//    @Transactional(rollbackFor = {Exception.class, Error.class})
//    public void analyze(List<ErcTx> txList) {
//        /*List<Token1155Holder> insertOrUpdate = new ArrayList<>();
//        txList.forEach(tx -> {
//            resolveTokenHolder(tx.getFrom(), tx, insertOrUpdate);
//            resolveTokenHolder(tx.getTo(), tx, insertOrUpdate);
//        });
//        if (CollUtil.isNotEmpty(insertOrUpdate)) {
//            customToken1155HolderMapper.batchInsertOrUpdateSelective1155(insertOrUpdate, Token1155Holder.Column.values());
//        }*/
//        List<Token1155Holder> changeBalanceList = new ArrayList<>();
//        txList.forEach(tx -> {
//            //resolveTokenHolder(tx.getFrom(), tx, insertOrUpdate);
//            //resolveTokenHolder(tx.getTo(), tx, insertOrUpdate);
//            if (StringUtils.isBlank(tx.getFrom()) || AddressUtil.isAddrZero(tx.getFrom())){ //铸币
//                Token1155Holder toHolder = new Token1155Holder();
//                toHolder.setTokenAddress(tx.getContract());
//                toHolder.setAddress(tx.getTo());
//                toHolder.setTokenId(tx.getTokenId());
//                toHolder.setIncrement(new BigDecimal(tx.getValue())); //变动量
//                changeBalanceList.add(toHolder);
//            }else if (StringUtils.isBlank(tx.getTo()) || AddressUtil.isAddrZero(tx.getTo())) { //销毁
//                Token1155Holder fromHolder = new Token1155Holder();
//                fromHolder.setTokenAddress(tx.getContract());
//                fromHolder.setAddress(tx.getFrom());
//                fromHolder.setTokenId(tx.getTokenId());
//                fromHolder.setIncrement(new BigDecimal(tx.getValue()).negate()); //变动量
//                changeBalanceList.add(fromHolder);
//            }else{ //正常token转账
//                //from减少
//                Token1155Holder fromHolder = new Token1155Holder();
//                fromHolder.setTokenAddress(tx.getContract());
//                fromHolder.setAddress(tx.getFrom());
//                fromHolder.setTokenId(tx.getTokenId());
//                fromHolder.setIncrement(new BigDecimal(tx.getValue()).negate()); //变动量
//                changeBalanceList.add(fromHolder);
//
//                //to增加
//                Token1155Holder toHolder = new Token1155Holder();
//                toHolder.setTokenAddress(tx.getContract());
//                toHolder.setAddress(tx.getTo());
//                toHolder.setTokenId(tx.getTokenId());
//                toHolder.setIncrement(new BigDecimal(tx.getValue())); //变动量
//                changeBalanceList.add(toHolder);
//            }
//
//        });
//        if (CollUtil.isNotEmpty(changeBalanceList)) {
//            //customTokenHolderMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenHolder.Column.values());
//            log.debug("区块中ERC1155交易数量：{}，需要更新的token持有者记录数量为：{}", txList.size(), changeBalanceList.size());
//            customToken1155HolderMapper.batchChange(changeBalanceList);
//        }
//    }

    /**
     * 解析
     *
     * @param ownerAddress:   地址
     * @param ercTx:          erc交易
     * @param insertOrUpdate: 更新列表
     * @return: void
     * @date: 2022/8/1
     */
    /*private void resolveTokenHolder(String ownerAddress, ErcTx ercTx, List<Token1155Holder> insertOrUpdate) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        Token1155HolderKey key = new Token1155HolderKey();
        key.setTokenAddress(ercTx.getContract());
        key.setAddress(ownerAddress);
        key.setTokenId(ercTx.getTokenId());
        Token1155Holder tokenHolder = customToken1155HolderMapper.selectByUK(key);
        if (tokenHolder == null) {
            tokenHolder = new Token1155Holder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            // 余额由定时任务更新，设置成默认值
            tokenHolder.setBalance("0");
            tokenHolder.setTokenId(ercTx.getTokenId());
            tokenHolder.setTokenOwnerTxQty(1);
        } else {
            int tokenOwnerTxQty = tokenHolder.getTokenOwnerTxQty() == null ? 0 : tokenHolder.getTokenOwnerTxQty();
            tokenHolder.setTokenOwnerTxQty(tokenOwnerTxQty + 1);
        }
        log.debug("该1155合约地址[{}][{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getTokenId(), tokenHolder.getAddress(), tokenHolder.getTokenOwnerTxQty());
        insertOrUpdate.add(tokenHolder);
    }*/

}
