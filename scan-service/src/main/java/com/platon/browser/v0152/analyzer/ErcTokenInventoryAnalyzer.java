package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.dao.custommapper.CustomTokenInventoryMapper;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryExample;
import com.platon.browser.dao.entity.TokenInventoryKey;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.service.erc.ErcServiceImpl;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.CommonUtil;
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

    @Resource
    private ErcServiceImpl ercServiceImpl;

    /**
     * 解析Token库存
     */
    public void analyze(String txHash, List<ErcTx> txList, BigInteger blockNumber) {
        List<TokenInventoryWithBLOBs> insertOrUpdate = new ArrayList<>();
        List<TokenInventoryKey> delTokenInventory = new ArrayList<>();
        Date date = new Date();
        if (CollUtil.isNotEmpty(txList)) {
            txList.forEach(tx -> {
                String tokenAddress = tx.getContract();
                String tokenId = tx.getValue();
                // 校验tokenid长度是否符合入库标准
                if (CommonUtil.ofNullable(() -> tokenId.length()).orElse(0) > 128) {
                    // 仅打印日志而不能抛出异常来阻塞流程
                    log.warn("当前交易[{}]token[{}]不符合合约标准，tokenId[{}]过长，仅支持128位", txHash, tokenAddress, tokenId);
                } else {
                    TokenInventoryExample example = new TokenInventoryExample();
                    example.createCriteria().andTokenAddressEqualTo(tokenAddress).andTokenIdEqualTo(tokenId);
                    List<TokenInventoryWithBLOBs> tokenInventoryWithBLOBs = tokenInventoryMapper.selectByExampleWithBLOBs(example);
                    TokenInventoryWithBLOBs tokenInventory;
                    // 不为空，交易次数加1
                    if (CollUtil.isNotEmpty(tokenInventoryWithBLOBs) && tokenInventoryWithBLOBs.size() == 1) {
                        tokenInventory = CollUtil.getFirst(tokenInventoryWithBLOBs);
                        tokenInventory.setTokenTxQty(tokenInventory.getTokenTxQty() + 1);
                    } else {
                        // 为空，则新建对象
                        tokenInventory = new TokenInventoryWithBLOBs();
                        tokenInventory.setTokenAddress(tokenAddress);
                        tokenInventory.setTokenId(tokenId);
                        tokenInventory.setCreateTime(date);
                        tokenInventory.setTokenTxQty(1);
                        tokenInventory.setRetryNum(0);
                        String tokenURI = ercServiceImpl.getTokenURI(tokenAddress, new BigInteger(tokenId), blockNumber);
                        if (StrUtil.isNotBlank(tokenURI)) {
                            tokenInventory.setTokenUrl(tokenURI);
                        } else {
                            log.warn("当前块高[{}]获取合约[{}]tokenId[{}]的tokenUrl为空，请联系管理员处理", blockNumber, tokenAddress, tokenId);
                        }
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
                    if (StrUtil.isNotBlank(tx.getTo()) && AddressUtil.isAddrZero(tx.getTo())) {
                        TokenInventoryKey tokenInventoryKey = new TokenInventoryKey();
                        tokenInventoryKey.setTokenId(tx.getValue());
                        tokenInventoryKey.setTokenAddress(tx.getContract());
                        delTokenInventory.add(tokenInventoryKey);
                    }
                }
            });
            if (CollUtil.isNotEmpty(insertOrUpdate)) {
                customTokenInventoryMapper.batchInsertOrUpdateSelective(insertOrUpdate, TokenInventory.Column.values());
                log.info("当前交易[{}]添加erc721库存[{}]笔成功", txHash, insertOrUpdate.size());
            }
            if (CollUtil.isNotEmpty(delTokenInventory)) {
                customTokenInventoryMapper.burnAndDelTokenInventory(delTokenInventory);
                log.info("当前交易[{}]删除erc721库存[{}]笔成功", txHash, delTokenInventory.size());
            }
        }
    }

}
