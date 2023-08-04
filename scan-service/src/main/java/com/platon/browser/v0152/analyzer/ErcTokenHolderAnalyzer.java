package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.dao.custommapper.CustomTokenHolderMapper;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.dao.entity.TokenHolderKey;
import com.platon.browser.dao.mapper.TokenHolderMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.utils.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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

    private TokenHolderKey getTokenHolderKey(String ownerAddress, ErcTx ercTx) {
        TokenHolderKey key = new TokenHolderKey();
        key.setTokenAddress(ercTx.getContract());
        key.setAddress(ownerAddress);
        return key;
    }

    /**
     * 解析Token Holder
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze(List<ErcTx> txList) {
        List<TokenHolder> insert = new ArrayList<>();
        List<TokenHolder> update = new ArrayList<>();
        Map<String, TokenHolder> context = new HashMap<>();
        txList.forEach(tx -> {
            resolveTokenHolder(tx.getFrom(), tx, context, insert, update);
            resolveTokenHolder(tx.getTo(), tx, context, insert, update);
        });
        if (CollUtil.isNotEmpty(insert)) {
            customTokenHolderMapper.batchInsert(insert);
        }
        if (CollUtil.isNotEmpty(update)) {
            customTokenHolderMapper.batchUpdateTokenTxQty(update);
        }
    }


    private void resolveTokenHolder(String ownerAddress, ErcTx ercTx, Map<String, TokenHolder> context,  List<TokenHolder> insert, List<TokenHolder> update) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        // 是否在缓存
        String objectKey = StringUtils.join(ercTx.getContract(), ownerAddress);
        TokenHolder tokenHolder = null;
        if(context.containsKey(objectKey)){
            tokenHolder = context.get(objectKey);
            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
        } else {
            TokenHolderKey key = getTokenHolderKey(ownerAddress, ercTx);
            tokenHolder = tokenHolderMapper.selectByPrimaryKey(key);
            if (tokenHolder == null) {
                tokenHolder = new TokenHolder();
                tokenHolder.setTokenAddress(key.getTokenAddress());
                tokenHolder.setAddress(key.getAddress());
                tokenHolder.setTokenTxQty(1);
                tokenHolder.setBalance("0");
                insert.add(tokenHolder);
            } else {
                tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
                update.add(tokenHolder);
            }
            context.put(objectKey, tokenHolder);
        }
        //TokenTxQty： 用户对该erc20的交易总数，或者是用户对该erc721, erc1155所有tokenId的交易总数
        log.info("该合约地址[{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getAddress(), tokenHolder.getTokenTxQty());
    }


//    /**
//     * 解析Token Holder
//     * lvxiaoyi, 2023/3/29
//     * 这里实时维护账户持有token的数量，不用在ErcTOkenUpdateTask中去靠job更新
//     * todo:如果合约的创建时铸币时没有transfer事件，将会使mint出来的数量没有被记录到minter账户上。
//     * 然后这个minter转账后给其它人后，这个minter的balance会变成负数
//     * 这个是因为这样的合约不是标准合约。所以如果实时维护就不能支持这样的非标准合约了。
//     */
//    @Transactional(rollbackFor = {Exception.class, Error.class})
//    public void analyze(List<ErcTx> txList) {
//        List<TokenHolder> changeBalanceList = new ArrayList<>();
//        txList.forEach(tx -> {
//            //resolveTokenHolder(tx.getFrom(), tx, insertOrUpdate);
//            //resolveTokenHolder(tx.getTo(), tx, insertOrUpdate);
//            if (StringUtils.isBlank(tx.getFrom()) || AddressUtil.isAddrZero(tx.getFrom())){ //铸币
//                TokenHolder toHolder = new TokenHolder();
//                toHolder.setTokenAddress(tx.getContract());
//                toHolder.setAddress(tx.getTo());
//                toHolder.setIncrement(new BigDecimal(tx.getValue())); //变动量
//                changeBalanceList.add(toHolder);
//            }else if (StringUtils.isBlank(tx.getTo()) || AddressUtil.isAddrZero(tx.getTo())) { //销毁币（不是销毁合约）
//                TokenHolder fromHolder = new TokenHolder();
//                fromHolder.setTokenAddress(tx.getContract());
//                fromHolder.setAddress(tx.getFrom());
//                fromHolder.setIncrement(new BigDecimal(tx.getValue()).negate()); //变动量
//                changeBalanceList.add(fromHolder);
//            }else{ //正常token转账
//                //from减少
//                TokenHolder fromHolder = new TokenHolder();
//                fromHolder.setTokenAddress(tx.getContract());
//                fromHolder.setAddress(tx.getFrom());
//                fromHolder.setIncrement(new BigDecimal(tx.getValue()).negate()); //变动量
//                changeBalanceList.add(fromHolder);
//
//                //to增加
//                TokenHolder toHolder = new TokenHolder();
//                toHolder.setTokenAddress(tx.getContract());
//                toHolder.setAddress(tx.getTo());
//                toHolder.setIncrement(new BigDecimal(tx.getValue())); //变动量
//                changeBalanceList.add(toHolder);
//            }
//        });
//        if (CollUtil.isNotEmpty(changeBalanceList)) {
//            //customTokenHolderMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenHolder.Column.values());
//            log.debug("区块中ERC20/ERC721交易数量：{}，需要更新的token持有者记录数量为：{}", txList.size(), changeBalanceList.size());
//            customTokenHolderMapper.batchChange(changeBalanceList);
//        }
//    }

    /*private void resolveTokenHolder(String ownerAddress, ErcTx ercTx, List<TokenHolder> insertOrUpdate) {
        // 零地址不需要創建holder
        if (AddressUtil.isAddrZero(ownerAddress)) {
            log.warn("该地址[{}]为0地址，不创建token holder", ownerAddress);
            return;
        }
        TokenHolderKey key = getTokenHolderKey(ownerAddress, ercTx);
        TokenHolder tokenHolder = tokenHolderMapper.selectByPrimaryKey(key);
        if (tokenHolder == null) {
            tokenHolder = new TokenHolder();
            tokenHolder.setTokenAddress(key.getTokenAddress());
            tokenHolder.setAddress(key.getAddress());
            tokenHolder.setTokenTxQty(1);
            tokenHolder.setBalance("0");
        } else {
            tokenHolder.setTokenTxQty(tokenHolder.getTokenTxQty() + 1);
        }
        //TokenTxQty： 用户对该erc20的交易总数，或者是用户对该erc721, erc1155所有tokenId的交易总数
        log.info("该合约地址[{}],持有者地址[{}],持有者对该合约的交易数为[{}]", tokenHolder.getTokenAddress(), tokenHolder.getAddress(), tokenHolder.getTokenTxQty());
        insertOrUpdate.add(tokenHolder);
    }*/

}
