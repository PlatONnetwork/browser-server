package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryExample;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * 解析Token库存
     */

    public void analyze(String txHash, List<ErcTx> txList, BigInteger blockNumber) {
        if(CollUtil.isEmpty(txList)){
            return;
        }
        StopWatch watch = new StopWatch("分析区块token721库存信息，区块：" + blockNumber);

        List<ErcTx> tobeDeletedTokenList = txList.stream().filter(tx-> StrUtil.isNotBlank(tx.getTo()) && AddressUtil.isAddrZero(tx.getTo())).collect(Collectors.toList());
        List<ErcTx> tobeInsertOnDuplicateUpdateList = txList.stream().filter(tx-> StrUtil.isNotBlank(tx.getTo()) && !AddressUtil.isAddrZero(tx.getTo())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(tobeDeletedTokenList)) {
            log.debug("删除erc1155库存: {}", JSON.toJSONString(tobeDeletedTokenList));
            watch.start("删除erc721库存，数量:" + tobeDeletedTokenList.size());
            customTokenInventoryMapper.burn(tobeDeletedTokenList);
            watch.stop();
        }
        if (CollUtil.isNotEmpty(tobeInsertOnDuplicateUpdateList)) {
            watch.start("新增或更新erc721库存，数量:" + tobeInsertOnDuplicateUpdateList.size());
            //如果on duplicate, 则说明这个NFT转给了其它人。owner发生了改变
            customTokenInventoryMapper.insertOnDuplicateUpdate(tobeInsertOnDuplicateUpdateList);
            watch.stop();
        }
        log.debug("结束分析区块token721库存信息，块高：{}，耗时统计：{}", blockNumber, watch.prettyPrint());
    }


    /**
     * 解析Token库存
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze_old(String txHash, List<ErcTx> txList, BigInteger blockNumber) {
        log.info("开始分析ERC721库存，块高：{}", blockNumber);
        StopWatch watch = new StopWatch("分析ERC721库存");

        List<TokenInventoryWithBLOBs> insertOrUpdate = new ArrayList<>();
        List<TokenInventoryKey> delTokenInventory = new ArrayList<>();
        if (CollUtil.isNotEmpty(txList)) {
            txList.forEach(tx -> {
                String tokenAddress = tx.getContract();
                String tokenId = tx.getTokenId();
                // 校验tokenid长度是否符合入库标准
                if (CommonUtil.ofNullable(() -> tokenId.length()).orElse(0) > 128) {
                    // 仅打印日志而不能抛出异常来阻塞流程
                    log.warn("当前交易[{}]token[{}]不符合合约标准，tokenId[{}]过长，仅支持128位", txHash, tokenAddress, tokenId);
                } else {
                    TokenInventoryExample example = new TokenInventoryExample();
                    example.createCriteria().andTokenAddressEqualTo(tokenAddress).andTokenIdEqualTo(tokenId);
                    watch.start(String.format("查询DB的ERC721 token库存信息(tokenId:%s)",tokenId));
                    List<TokenInventoryWithBLOBs> tokenInventoryWithBLOBs = tokenInventoryMapper.selectByExampleWithBLOBs(example);
                    watch.stop();
                    TokenInventoryWithBLOBs tokenInventory;
                    // 不为空，交易次数加1
                    if (CollUtil.isNotEmpty(tokenInventoryWithBLOBs) && tokenInventoryWithBLOBs.size() == 1) {
                        tokenInventory = CollUtil.getFirst(tokenInventoryWithBLOBs);
                        tokenInventory.setTokenTxQty(tokenInventory.getTokenTxQty() + 1);
                    } else {
                        // 为空，则新建对象
                        watch.start(String.format("创建新ERC721 token库存(tokenId:%s)",tokenId));
                        tokenInventory = new TokenInventoryWithBLOBs();
                        tokenInventory.setTokenAddress(tokenAddress);
                        tokenInventory.setTokenId(tokenId);
                        tokenInventory.setTokenTxQty(1);
                        tokenInventory.setRetryNum(0);
                        //todo: 非常耗时，想办法多线程异步获取；或者有scan-job来补齐（采用此方案）
                        /*String tokenURI = ercServiceImpl.getTokenURI(tokenAddress, new BigInteger(tokenId), blockNumber);
                        if (StrUtil.isNotBlank(tokenURI)) {
                            tokenInventory.setTokenUrl(tokenURI);
                        } else {
                            log.warn("当前块高[{}]获取合约[{}]tokenId[{}]的tokenUrl为空，请联系管理员处理", blockNumber, tokenAddress, tokenId);
                        }*/
                        watch.stop();
                    }
                    //todo: todo: 2023/07/25 lvxiaoyi，这个TokenOwnerTxQty，缺省是不是应该是 1 而不是 0？
                    if (tx.getTo().equalsIgnoreCase(tokenInventory.getOwner())) {
                        int tokenOwnerTxQty = tokenInventory.getTokenOwnerTxQty() == null ? 0 : tokenInventory.getTokenOwnerTxQty();
                        tokenInventory.setTokenOwnerTxQty(tokenOwnerTxQty + 1);
                    } else {
                        tokenInventory.setTokenOwnerTxQty(1);
                    }
                    tokenInventory.setOwner(tx.getTo());
                    insertOrUpdate.add(tokenInventory);
                    // 如果合约交易当中，to地址是0地址的话，需要清除TokenInventory记录
                    if (StrUtil.isNotBlank(tx.getTo()) && AddressUtil.isAddrZero(tx.getTo())) {
                        TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
                        tokenInventoryKey.setTokenId(tx.getTokenId());
                        tokenInventoryKey.setTokenAddress(tx.getContract());
                        delTokenInventory.add(tokenInventoryKey);
                    }
                }
            });
            if (CollUtil.isNotEmpty(insertOrUpdate)) {
                watch.start("更新ERC721 token库存");
                customTokenInventoryMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenInventory.Column.values());
                watch.stop();
                log.debug("当前交易[{}]添加erc721库存[{}]笔成功", txHash, insertOrUpdate.size());
            }
            if (CollUtil.isNotEmpty(delTokenInventory)) {
                watch.start("销毁ERC721 token库存");
                customTokenInventoryMapper.burnAndDelTokenInventory(delTokenInventory);
                watch.stop();
                log.debug("当前交易[{}]删除erc721库存[{}]笔成功", txHash, delTokenInventory.size());
            }
        }
        log.info("结束分析ERC721库存，块高：{}，耗时统计：{}", blockNumber, watch.prettyPrint());
    }

}
