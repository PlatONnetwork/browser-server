package com.platon.browser.v0152.analyzer;

import com.platon.browser.utils.AddressUtil;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.bean.Receipt;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.CustomTokenMapper;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.bean.ErcToken;
import com.platon.browser.v0152.bean.ErcTxInfo;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import com.platon.browser.v0152.service.ErcDetectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Erc Token 服务
 */
@Slf4j
@Service
public class ErcTokenAnalyzer {

    @Resource
    private ErcDetectService ercDetectService;

    @Resource
    private ErcCache ercCache;

    @Resource
    private AddressCache addressCache;

    @Resource
    private ErcTokenInventoryAnalyzer ercTokenInventoryAnalyzer;

    @Resource
    private ErcTokenHolderAnalyzer ercTokenHolderAnalyzer;

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private TokenMapper tokenMapper;

    /**
     * 解析Token,在合约创建时调用
     *
     * @param contractAddress
     */
    public ErcToken resolveToken(String contractAddress) {
        ErcToken token = new ErcToken();
        token.setAddress(contractAddress);
        ErcContractId contractId = ercDetectService.getContractId(contractAddress);
        BeanUtils.copyProperties(contractId, token);
        token.setTypeEnum(contractId.getTypeEnum());
        token.setType(contractId.getTypeEnum().name().toLowerCase());
        switch (contractId.getTypeEnum()) {
            case ERC20:
                token.setIsSupportErc20(true);
                token.setIsSupportErc165(false);
                token.setIsSupportErc721(false);
                token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                ercCache.erc20AddressCache.add(contractAddress);
                break;
            case ERC721:
                token.setIsSupportErc20(false);
                token.setIsSupportErc165(true);
                token.setIsSupportErc721(true);
                token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress));
                token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress));
                ercCache.erc721AddressCache.add(contractAddress);
                break;
            default:
        }
        if (token.getTypeEnum() != ErcTypeEnum.UNKNOWN) {
            // 入库ERC721或ERC20 Token记录
            token.setTokenTxQty(0);
            customTokenMapper.batchInsertOrUpdateSelective(Collections.singletonList(token), Token.Column.values());
            ercCache.tokenCache.put(token.getAddress(), token);
        }
        return token;
    }

    private List<ErcTx> resolveErcTxFromEvent(Token token, CollectionTransaction tx, List<ErcContract.ErcTxEvent> eventList) {
        List<ErcTx> txList = new ArrayList<>();
        AtomicLong seq = new AtomicLong(tx.getNum() * 100000);
        eventList.forEach(event -> {
            // 转换参数进行设置内部交易
            ErcTx ercTx = ErcTx.builder()
                    .seq(seq.longValue())
                    .bn(tx.getNum())
                    .hash(tx.getHash())
                    .bTime(tx.getTime())
                    .txFee(tx.getCost())
                    .fromType(addressCache.getTypeData(event.getFrom()))
                    .toType(addressCache.getTypeData(event.getTo()))
                    .from(event.getFrom())
                    .to(event.getTo())
                    .value(event.getValue().toString())
                    .name(token.getName())
                    .symbol(token.getSymbol())
                    .decimal(token.getDecimal())
                    .contract(token.getAddress())
                    .build();
            seq.getAndIncrement();
            txList.add(ercTx);
        });
        return txList;
    }

    private String getErcTxInfo(List<ErcTx> txList) {
        List<ErcTxInfo> infoList = new ArrayList<>();
        txList.forEach(tx -> {
            ErcTxInfo eti = new ErcTxInfo();
            BeanUtils.copyProperties(tx, eti);
            infoList.add(eti);
        });
        return JSON.toJSONString(infoList);
    }

    /**
     * 解析ERC交易, 在合约调用时调用
     *
     * @param tx
     */
    public ErcTypeEnum resolveTx(CollectionTransaction tx, Receipt receipt) {
        ErcToken token = ercCache.tokenCache.get(tx.getTo());
        if (token == null) {
            log.warn("缓存中未找到合约地址[{}]对应的Erc Token", tx.getContractAddress());
            return ErcTypeEnum.UNKNOWN;
        }
        String contractAddress = token.getAddress();
        ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());

        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setLogs(receipt.getLogs());
        transactionReceipt.setContractAddress(contractAddress);
        List<ErcContract.ErcTxEvent> eventList;
        List<ErcTx> txList = Collections.emptyList();
        switch (typeEnum) {
            case ERC20:
                eventList = ercDetectService.getErc20TxEvents(transactionReceipt);
                txList = resolveErcTxFromEvent(token, tx, eventList);
                tx.setErc20TxList(txList);
                tx.setErc20TxInfo(getErcTxInfo(txList));
                break;
            case ERC721:
                eventList = ercDetectService.getErc721TxEvents(transactionReceipt);
                txList = resolveErcTxFromEvent(token, tx, eventList);
                tx.setErc721TxList(txList);
                tx.setErc721TxInfo(getErcTxInfo(txList));
                ercTokenInventoryAnalyzer.analyze(txList);
                break;
        }

        token.setTokenTxQty(token.getTokenTxQty() + txList.size());
        token.setUpdateTime(new Date());
        token.setDirty(true);
        ercTokenHolderAnalyzer.analyze(txList);

        // 以上所有操作无误，最后更新地址表erc交易数缓存
        txList.forEach(ercTx -> {
            if (!AddressUtil.isAddrZero(ercTx.getFrom())) {
                switch (typeEnum) {
                    case ERC20:
                        addressCache.updateErc20TxQty(ercTx.getFrom());
                        break;
                    case ERC721:
                        addressCache.updateErc721TxQty(ercTx.getFrom());
                        break;
                }
            }
            if (!AddressUtil.isAddrZero(ercTx.getTo())) {
                switch (typeEnum) {
                    case ERC20:
                        addressCache.updateErc20TxQty(ercTx.getTo());
                        break;
                    case ERC721:
                        addressCache.updateErc721TxQty(ercTx.getTo());
                        break;
                }
            }
        });
        return typeEnum;
    }

    public static void main(String[] args) {
        System.out.println("打印结果为：" + AddressUtil.isAddrZero("lat1qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq542u6a"));
    }

}
