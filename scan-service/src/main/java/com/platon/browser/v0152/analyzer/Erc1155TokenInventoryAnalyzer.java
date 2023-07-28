package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.platon.browser.dao.custommapper.CustomToken1155InventoryMapper;
import com.platon.browser.dao.entity.Token1155Inventory;
import com.platon.browser.dao.entity.Token1155InventoryExample;
import com.platon.browser.dao.entity.Token1155InventoryKey;
import com.platon.browser.dao.entity.Token1155InventoryWithBLOBs;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Erc1155,1155 token 库存服务
 */
@Slf4j
@Service
public class Erc1155TokenInventoryAnalyzer {

    @Resource
    private CustomToken1155InventoryMapper customToken1155InventoryMapper;

    @Resource
    private ErcServiceImpl ercServiceImpl;

    /**
     * 解析Token库存
     * 整体逻辑
     * 1, 判断token是否记录过，如果记录或交易次数增加1
     * 2，未存在过则入库，然后交易次数+1
     * 3. 如果tx.to为空，则删除
     *  // todo: 2023/07/25 lvxiaoyi 可以修改为：
     *  1. 从txList中，过滤出to为空的tokenAddress + tokenId，作为删除记录的条件；
     *  2. 剩余的txList，批量update(交易次数+1)，再insert ignore into(缺省交易次数=1
     *  3. 或者insert into on duplicate update
     */
    public void analyze(String txHash, List<ErcTx> txList, BigInteger blockNumber) {
        if(CollUtil.isEmpty(txList)){
            return;
        }
        StopWatch watch = new StopWatch("分析区块token1155库存信息，区块：" + blockNumber);

        List<ErcTx> tobeDeletedTokenList = txList.stream().filter(tx-> StrUtil.isNotBlank(tx.getTo()) && AddressUtil.isAddrZero(tx.getTo())).collect(Collectors.toList());
        List<ErcTx> tobeInsertOnDuplicateUpdateList = txList.stream().filter(tx-> StrUtil.isNotBlank(tx.getTo()) && !AddressUtil.isAddrZero(tx.getTo())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(tobeDeletedTokenList)) {
            log.debug("删除erc1155库存: {}", JSON.toJSONString(tobeDeletedTokenList));
            watch.start("删除erc1155库存，数量:" + tobeDeletedTokenList.size());
            customToken1155InventoryMapper.burn(tobeDeletedTokenList);
            watch.stop();
        }
        if (CollUtil.isNotEmpty(tobeInsertOnDuplicateUpdateList)) {
            watch.start("新增或更新erc1155库存，数量:" + tobeInsertOnDuplicateUpdateList.size());
            customToken1155InventoryMapper.insertOnDuplicateUpdate(tobeInsertOnDuplicateUpdateList);
            watch.stop();
        }
        log.debug("结束分析区块token1155库存信息，块高：{}，耗时统计：{}", blockNumber, watch.prettyPrint());
    }

    /**
     * 解析Token库存
     * 整体逻辑
     * 1, 判断token是否记录过，如果记录或交易次数增加1
     * 2，未存在过则入库，然后交易次数+1
     */
    public void analyze_old(String txHash, List<ErcTx> txList, BigInteger blockNumber) {
        List<Token1155InventoryWithBLOBs> insertOrUpdate = new ArrayList<>();
        List<Token1155InventoryKey> delToken1155InventoryKey = new ArrayList<>();
        if (CollUtil.isNotEmpty(txList)) {
            txList.forEach(tx -> {
                String tokenAddress = tx.getContract();
                String tokenId = tx.getTokenId();
                // 校验tokenid长度是否符合入库标准
                if (CommonUtil.ofNullable(tokenId::length).orElse(0) > 128) {
                    // 仅打印日志而不能抛出异常来阻塞流程
                    log.warn("当前交易[{}]token[{}]不符合合约标准，tokenId[{}]过长，仅支持128位", txHash, tokenAddress, tokenId);
                } else {
                    // 1, 判断接收地址是否存在, 如果存在则对balance做增加动作
                    Token1155InventoryExample toExample = new Token1155InventoryExample();
                    toExample.createCriteria().andTokenAddressEqualTo(tokenAddress).andTokenIdEqualTo(tokenId);
                    Token1155InventoryKey key = new Token1155InventoryKey();
                    key.setTokenAddress(tokenAddress);
                    key.setTokenId(tokenId);
                    Token1155InventoryWithBLOBs toTokenInventory = customToken1155InventoryMapper.findOneByUK(key);
                    // 不为空,交易次数+1
                    if (ObjectUtil.isNotNull(toTokenInventory)) {
                        toTokenInventory.setTokenTxQty(toTokenInventory.getTokenTxQty() + 1);
                    } else {
                        toTokenInventory = new Token1155InventoryWithBLOBs();
                        toTokenInventory.setTokenAddress(tokenAddress);
                        toTokenInventory.setTokenId(tokenId);
                        toTokenInventory.setTokenTxQty(1);
                        toTokenInventory.setRetryNum(0);
                        //todo: 非常耗时，想办法多线程异步获取；或者有scan-job来补齐（采用此方案）
                        /*String tokenURI = ercServiceImpl.getToken1155URI(tokenAddress, new BigInteger(tokenId), blockNumber);
                        if (StrUtil.isNotBlank(tokenURI)) {
                            toTokenInventory.setTokenUrl(tokenURI);
                        } else {
                            log.warn("当前块高[{}]获取合约[{}]tokenId[{}]的tokenUrl为空，请联系管理员处理", blockNumber, tokenAddress, tokenId);
                        }*/
                    }

                    insertOrUpdate.add(toTokenInventory);
                    // 如果合约交易当中，to地址是0地址的话，需要清除TokenInventory记录
                    if (StrUtil.isNotBlank(tx.getTo()) && AddressUtil.isAddrZero(tx.getTo())) {
                        Token1155InventoryKey token1155InventoryKey = new Token1155InventoryKey();
                        token1155InventoryKey.setTokenId(tx.getTokenId());
                        token1155InventoryKey.setTokenAddress(tx.getContract());
                        delToken1155InventoryKey.add(token1155InventoryKey);
                    }
                }
            });

            if (CollUtil.isNotEmpty(insertOrUpdate)) {
                customToken1155InventoryMapper.batchInsertOrUpdateSelective(insertOrUpdate, Token1155Inventory.Column.values());
                log.debug("当前交易[{}]添加erc1155库存[{}]笔成功", txHash, insertOrUpdate.size());
            }
            if (CollUtil.isNotEmpty(delToken1155InventoryKey)) {
                customToken1155InventoryMapper.burnAndDelTokenInventory(delToken1155InventoryKey);
                log.debug("当前交易[{}]删除erc721库存[{}]笔成功", txHash, delToken1155InventoryKey.size());
            }
        }
    }


}
