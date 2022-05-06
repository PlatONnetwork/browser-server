package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.bean.ErcToken;
import com.platon.browser.bean.Receipt;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.bean.ErcTxInfo;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.service.ErcDetectService;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public ErcToken resolveToken(String contractAddress, BigInteger blockNumber) {
        ErcToken token = new ErcToken();
        token.setTypeEnum(ErcTypeEnum.UNKNOWN);
        try {
            token.setAddress(contractAddress);
            ErcContractId contractId = ercDetectService.getContractId(contractAddress, blockNumber);
            BeanUtils.copyProperties(contractId, token);
            token.setTotalSupply(CommonUtil.ofNullable(() -> contractId.getTotalSupply().toPlainString()).orElse("0"));
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
                    token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress, blockNumber));
                    token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress, blockNumber));
                    ercCache.erc721AddressCache.add(contractAddress);
                    break;
                default:
            }
            if (token.getTypeEnum() != ErcTypeEnum.UNKNOWN) {
                // 入库ERC721或ERC20 Token记录
                token.setTokenTxQty(0);
                token.setContractDestroyBlock(0L);
                token.setContractDestroyUpdate(false);
                // 检查token是否合法
                checkToken(token);
                customTokenMapper.batchInsertOrUpdateSelective(Collections.singletonList(token), Token.Column.values());
                ercCache.tokenCache.put(token.getAddress(), token);
                log.info("创建合约成功，合约地址为[{}],合约类型为[{}]", token.getAddress(), token.getType());
            } else {
                log.warn("该合约地址[{}]无法识别该类型[{}]", token.getAddress(), token.getTypeEnum());
            }
        } catch (Exception e) {
            log.error("合约创建,解析Token异常", e);
        }
        return token;
    }

    /**
     * token校验---根据mysql定义字段来约束校验
     *
     * @param token 合约
     * @return void
     * @date 2021/4/29
     */
    private void checkToken(ErcToken token) {
        // 1.校验合约名称，可以为null，约束为64
        if (StrUtil.isNotEmpty(token.getName())) {
            // 校验合约名称长度，默认为64
            if (CommonUtil.ofNullable(() -> token.getName().length()).orElse(0) > 64) {
                String name = StrUtil.fillAfter(StrUtil.sub(token.getName(), 0, 61), '.', 64);
                log.warn("该token[{}]的名称过长（默认64位）,将自动截取,旧值[{}],新值[{}]", token.getAddress(), token.getName(), name);
                token.setName(name);
            }
        }
        // 2.校验合约符号，可以为nul，约束为64位
        if (StrUtil.isNotEmpty(token.getSymbol())) {
            // 校验合约符号，默认为64
            if (CommonUtil.ofNullable(() -> token.getSymbol().length()).orElse(0) > 64) {
                String symbol = StrUtil.fillAfter(StrUtil.sub(token.getSymbol(), 0, 61), '.', 64);
                log.warn("该token[{}]的合约符号过长（默认64位）,将自动截取,旧值[{}],新值[{}]", token.getAddress(), token.getSymbol(), symbol);
                token.setSymbol(symbol);
            }
        }
    }

    /**
     * 从交易回执的事件中解析出交易
     *
     * @param token     token
     * @param tx        交易
     * @param eventList 事件列表
     * @return java.util.List<com.platon.browser.elasticsearch.dto.ErcTx> erc交易列表
     * @date 2021/4/14
     */
    private List<ErcTx> resolveErcTxFromEvent(Token token, CollectionTransaction tx, List<ErcContract.ErcTxEvent> eventList, Long seq) {
        List<ErcTx> txList = new ArrayList<>();
        eventList.forEach(event -> {
            // 转换参数进行设置内部交易
            ErcTx ercTx = ErcTx.builder()
                               .seq(seq)
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
            txList.add(ercTx);
            addAddressCache(event.getFrom(), event.getTo());
        });
        return txList;
    }

    /**
     * 真实交易的from和to地址添加到地址缓存，然后入库
     *
     * @param from:
     * @param to:
     * @return: void
     * @date: 2021/12/14
     */
    private void addAddressCache(String from, String to) {
        if (StrUtil.isNotBlank(from)) {
            Address fromAddress = addressCache.getAddress(from);
            if (fromAddress == null && !AddressUtil.isAddrZero(from)) {
                fromAddress = addressCache.createDefaultAddress(from);
                addressCache.addAddress(fromAddress);
            }
        }
        if (StrUtil.isNotBlank(to)) {
            Address toAddress = addressCache.getAddress(to);
            if (toAddress == null && !AddressUtil.isAddrZero(to)) {
                toAddress = addressCache.createDefaultAddress(to);
                addressCache.addAddress(toAddress);
            }
        }
    }

    /**
     * 获取交易信息
     *
     * @param txList 交易列表
     * @return java.lang.String
     * @date 2021/4/14
     */
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
     * @param collectionBlock 当前区块
     * @param tx              交易对象
     * @param receipt         交易回执：一笔交易可能包含多个事件，故可能有多条交易
     * @return void
     * @date 2021/4/15
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void resolveTx(Block collectionBlock, CollectionTransaction tx, Receipt receipt) {
        try {
            // 过滤交易回执日志，地址不能为空且在token缓存里的
            List<Log> tokenLogs = receipt.getLogs()
                                         .stream()
                                         .filter(receiptLog -> StrUtil.isNotEmpty(receiptLog.getAddress()))
                                         .filter(receiptLog -> ercCache.tokenCache.containsKey(receiptLog.getAddress()))
                                         .collect(Collectors.toList());

            if (CollUtil.isEmpty(tokenLogs)) {
                return;
            }

            tokenLogs.forEach(tokenLog -> {
                ErcToken token = ercCache.tokenCache.get(tokenLog.getAddress());
                if (ObjectUtil.isNotNull(token)) {
                    List<ErcTx> txList = Collections.emptyList();
                    String contractAddress = token.getAddress();
                    ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
                    TransactionReceipt transactionReceipt = new TransactionReceipt();
                    transactionReceipt.setLogs(receipt.getLogs().stream().filter(v -> v.getAddress().equalsIgnoreCase(contractAddress)).collect(Collectors.toList()));
                    transactionReceipt.setContractAddress(contractAddress);
                    List<ErcContract.ErcTxEvent> eventList;
                    switch (typeEnum) {
                        case ERC20:
                            eventList = ercDetectService.getErc20TxEvents(transactionReceipt, BigInteger.valueOf(collectionBlock.getNum()));
                            List<ErcContract.ErcTxEvent> erc20TxEventList = eventList.stream().filter(v -> ObjectUtil.equal(v.getLog(), tokenLog)).collect(Collectors.toList());
                            if (erc20TxEventList.size() > 1) {
                                log.error("当前交易[{}]erc20交易回执日志解析异常{}", tx.getHash(), tokenLog);
                                break;
                            }
                            txList = resolveErcTxFromEvent(token, tx, erc20TxEventList, collectionBlock.getSeq().incrementAndGet());
                            tx.getErc20TxList().addAll(txList);
                            break;
                        case ERC721:
                            eventList = ercDetectService.getErc721TxEvents(transactionReceipt, BigInteger.valueOf(collectionBlock.getNum()));
                            List<ErcContract.ErcTxEvent> erc721TxEventList = eventList.stream().filter(v -> v.getLog().equals(tokenLog)).collect(Collectors.toList());
                            if (erc721TxEventList.size() > 1) {
                                log.error("当前交易[{}]erc721交易回执日志解析异常{}", tx.getHash(), tokenLog);
                                break;
                            }
                            txList = resolveErcTxFromEvent(token, tx, erc721TxEventList, collectionBlock.getSeq().incrementAndGet());
                            tx.getErc721TxList().addAll(txList);
                            ercTokenInventoryAnalyzer.analyze(tx.getHash(), txList, BigInteger.valueOf(collectionBlock.getNum()));
                            break;
                    }
                    token.setUpdateTime(new Date());
                    token.setDirty(true);
                    ercTokenHolderAnalyzer.analyze(txList);
                } else {
                    log.error("当前交易[{}]缓存中未找到合约地址[{}]对应的Erc Token", tx.getHash(), tokenLog.getAddress());
                }
            });
            tx.setErc20TxInfo(getErcTxInfo(tx.getErc20TxList()));
            tx.setErc721TxInfo(getErcTxInfo(tx.getErc721TxList()));
            log.info("当前交易[{}]有[{}]笔log,其中token交易有[{}]笔，其中erc20有[{}]笔,其中erc721有[{}]笔",
                     tx.getHash(),
                     CommonUtil.ofNullable(() -> receipt.getLogs().size()).orElse(0),
                     CommonUtil.ofNullable(() -> tokenLogs.size()).orElse(0),
                     CommonUtil.ofNullable(() -> tx.getErc20TxList().size()).orElse(0),
                     CommonUtil.ofNullable(() -> tx.getErc721TxList().size()).orElse(0));

            // 针对销毁的合约处理
            if (tx.getType() == com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.CONTRACT_EXEC_DESTROY.getCode()) {
                Token token = new Token();
                token.setAddress(tx.getTo());
                token.setContractDestroyBlock(collectionBlock.getNum());
                tokenMapper.updateByPrimaryKeySelective(token);
                log.info("合约[{}]在区块[{}]已销毁", receipt.getContractAddress(), collectionBlock.getNum());
            }

        } catch (Exception e) {
            log.error(StrUtil.format("当前交易[{}]解析ERC交易异常", tx.getHash()), e);
            throw e;
        }
    }

}
