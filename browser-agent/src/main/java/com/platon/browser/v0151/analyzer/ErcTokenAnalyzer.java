package com.platon.browser.v0151.analyzer;

import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.bean.Receipt;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.v0151.bean.ErcContractId;
import com.platon.browser.v0151.bean.ErcToken;
import com.platon.browser.v0151.bean.ErcTxInfo;
import com.platon.browser.v0151.cache.ErcCache;
import com.platon.browser.v0151.contract.ErcContract;
import com.platon.browser.v0151.enums.ErcTypeEnum;
import com.platon.browser.v0151.service.ErcDetectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Erc合约相关服务
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
    private TokenMapper tokenMapper;

    /**
     * 解析Token,在合约创建时调用
     * @param contractAddress
     */
    public ErcToken resolveToken(String contractAddress) {
        ErcToken token = new ErcToken();
        token.setAddress(contractAddress);
        ErcContractId contractId = ercDetectService.getContractId(contractAddress);
        BeanUtils.copyProperties(contractId,token);
        token.setTypeEnum(contractId.getTypeEnum());
        token.setType(contractId.getTypeEnum().name().toLowerCase());
        switch (contractId.getTypeEnum()){
            case ERC20:
                token.setIsSupportErc20(true);
                token.setIsSupportErc165(false);
                token.setIsSupportErc721(false);
                token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                ercCache.getTokenMap().put(contractAddress,token);
                ercCache.getErc20AddressCache().add(contractAddress);
                break;
            case ERC721:
                token.setIsSupportErc20(false);
                token.setIsSupportErc165(true);
                token.setIsSupportErc721(true);
                token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress));
                token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress));
                ercCache.getTokenMap().put(contractAddress,token);
                ercCache.getErc721AddressCache().add(contractAddress);
                break;
            default:
        }
        if(token.getTypeEnum()!= ErcTypeEnum.UNKNOWN){
            // 入库ERC721或ERC20 Token记录
            tokenMapper.insert(token);
        }
        return token;
    }

    private List<ErcTx> resolveErcTxFromEvent(CollectionTransaction tx,List<ErcContract.ErcTxEvent> eventList){
        List<ErcTx> txList = new ArrayList<>();
        AtomicInteger seq = new AtomicInteger((int)(tx.getNum()*100000));
        eventList.forEach(event -> {
            // 仅添加与指定的合约地址相同的记录
            if (event.getLog().getAddress().equalsIgnoreCase(tx.getTo())) {
                // 转换参数进行设置内部交易
                ErcTx ercTx = ErcTx.builder()
                        .seq(seq.longValue())
                        .contract(tx.getTo())
                        .from(event.getFrom())
                        .to(event.getTo())
                        .value(event.getValue().toString())
                        .bn(tx.getNum())
                        .hash(tx.getHash())
                        .bTime(tx.getTime()).value(tx.getValue())
                        .fromType(addressCache.getTypeData(event.getFrom()))
                        .toType(addressCache.getTypeData(event.getTo()))
                        .build();
                seq.getAndIncrement();
                txList.add(ercTx);
            }
        });
        return txList;
    }

    private String getErcTxInfo(List<ErcTx> txList){
        List<ErcTxInfo> infoList = new ArrayList<>();
        txList.forEach(tx->{
            ErcTxInfo eti = new ErcTxInfo();
            BeanUtils.copyProperties(tx,eti);
            infoList.add(eti);
        });
        return JSON.toJSONString(infoList);
    }

    /**
     * 解析ERC交易, 在合约调用时调用
     * @param tx
     */
    public void resolveTx(CollectionTransaction tx, Receipt receipt) {
        Token token = tokenMapper.selectByPrimaryKey(tx.getTo());
        if(token==null){
            log.warn("未找到合约地址[{}]对应的Erc Token",tx.getContractAddress());
            return;
        }
        String contractAddress = token.getAddress();
        ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());

        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setLogs(receipt.getLogs());
        transactionReceipt.setContractAddress(contractAddress);
        List<ErcContract.ErcTxEvent> eventList;
        List<ErcTx> txList = Collections.emptyList();
        switch (typeEnum){
            case ERC20:
                eventList = ercDetectService.getErc20TxEvents(transactionReceipt);
                txList = resolveErcTxFromEvent(tx,eventList);
                tx.setErc20TxList(txList);
                tx.setErc20TxInfo(getErcTxInfo(txList));
                break;
            case ERC721:
                eventList = ercDetectService.getErc721TxEvents(transactionReceipt);
                txList = resolveErcTxFromEvent(tx,eventList);
                tx.setErc721TxList(txList);
                tx.setErc721TxInfo(getErcTxInfo(txList));
                ercTokenInventoryAnalyzer.analyze(txList);
                break;
        }
        ercTokenHolderAnalyzer.analyze(txList);
    }
}
