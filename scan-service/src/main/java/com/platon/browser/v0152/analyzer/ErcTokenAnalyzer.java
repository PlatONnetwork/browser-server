package com.platon.browser.v0152.analyzer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.Receipt;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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
    private NewAddressCache newAddressCache;

    @Resource
    private ErcTokenInventoryAnalyzer ercTokenInventoryAnalyzer;

    @Resource
    private Erc1155TokenInventoryAnalyzer erc1155TokenInventoryAnalyzer;

    @Resource
    private ErcTokenHolderAnalyzer ercTokenHolderAnalyzer;

    @Resource
    private ErcToken1155HolderAnalyzer ercToken1155HolderAnalyzer;

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private TokenMapper tokenMapper;


    /**
     * 解析Token, 获取满足erc标准的token(erc20,721,1155三种)在合约创建时调用
     * @param contractAddress
     * @param blockNumber
     * @return 如果不是ERC标准，则返回null
     */
    public Token resolveNewToken(ErcTypeEnum ercTypeEum, String contractAddress, BigInteger blockNumber) {
        if (ercTypeEum==null){
            return null;
        }
        try {
            ErcContractId contractId = ercDetectService.getErcContractId(ercTypeEum, contractAddress, blockNumber);
            if (contractId != null) {
                Token token = new Token();
                token.setAddress(contractAddress);
                token.setName(contractId.getName());
                token.setSymbol(contractId.getSymbol());
                token.setDecimal(contractId.getDecimal());
                token.setTotalSupply(CommonUtil.ofNullable(() -> contractId.getTotalSupply().toPlainString()).orElse("0"));
                token.setType(contractId.getTypeEnum().name().toLowerCase());
                token.setCreatedBlockNumber(blockNumber.longValue());
                token.setTokenTxQty(0);
                token.setHolder(0);
                token.setContractDestroyBlock(0L);
                token.setContractDestroyUpdate(false);

                switch (contractId.getTypeEnum()) {
                    case ERC20:
                        token.setIsSupportErc20(true);
                        token.setIsSupportErc165(false);
                        token.setIsSupportErc721(false);
                        token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                        token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                        token.setIsSupportErc1155(false);
                        token.setIsSupportErc1155Metadata(token.getIsSupportErc1155());
                        // 在com.platon.browser.analyzer.TransactionAnalyzer.analyze时，
                        // 会调用com.platon.browser.cache.NewAddressCache.addNewContractAddressToBlockCtx
                        // 把新地址类型加入NewAddressCache
                        //ercCache.erc20AddressCache.add(contractAddress);
                        break;
                    case ERC721:
                        token.setIsSupportErc20(false);
                        token.setIsSupportErc165(true);
                        token.setIsSupportErc721(true);
                        token.setIsSupportErc721Enumeration(ercDetectService.isSupportErc721Enumerable(contractAddress, blockNumber));
                        token.setIsSupportErc721Metadata(ercDetectService.isSupportErc721Metadata(contractAddress, blockNumber));
                        token.setIsSupportErc1155(false);
                        token.setIsSupportErc1155Metadata(token.getIsSupportErc1155());
                        // 在com.platon.browser.analyzer.TransactionAnalyzer.analyze时，
                        // 会调用com.platon.browser.cache.NewAddressCache.addNewContractAddressToBlockCtx
                        // 把新地址类型加入NewAddressCache
                        //ercCache.erc721AddressCache.add(contractAddress);
                        break;
                    case ERC1155:
                        token.setIsSupportErc20(false);
                        token.setIsSupportErc165(true);
                        token.setIsSupportErc721(false);
                        token.setIsSupportErc721Enumeration(token.getIsSupportErc721());
                        token.setIsSupportErc721Metadata(token.getIsSupportErc721());
                        //
                        token.setIsSupportErc1155(true);
                        token.setIsSupportErc1155Metadata(ercDetectService.isSupportErc1155Metadata(contractAddress, blockNumber));
                        // 在com.platon.browser.analyzer.TransactionAnalyzer.analyze时，
                        // 会调用com.platon.browser.cache.NewAddressCache.addNewContractAddressToBlockCtx
                        // 把新地址类型加入NewAddressCache
                        //ercCache.erc1155AddressCache.add(contractAddress);
                        break;
                }


                // 检查token是否合法
                checkToken(token);
                tokenMapper.insert(token);
                newAddressCache.addTokenCache(token.getAddress(), token);
                log.debug("创建合约成功，合约地址为[{}],合约类型为[{}]", token.getAddress(), token.getType());
                return token;
            }
        } catch (Exception e) {
            log.warn("合约创建,解析是否符合ERC标准时异常", e);
        }

        return null;
    }

    /**
     * token校验---根据mysql定义字段来约束校验
     *
     * @param token 合约
     * @return void
     * @date 2021/4/29
     */
    private void checkToken(Token token) {
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
    private List<ErcTx> resolveErcTransferTxFromEvent(Token token, com.platon.browser.elasticsearch.dto.Transaction tx, List<ErcContract.ErcTxEvent> eventList, Long seq) {
        List<ErcTx> txList = new ArrayList<>();
        eventList.forEach(event -> {
            //event.to地址可能是新地址
            newAddressCache.addPendingAddressToBlockCtx(event.getTo());
            log.debug("event.to:{}地址可能是新地址", event.getTo());
            // 转换参数进行设置内部交易
            ErcTx ercTx = ErcTx.builder()
                               .seq(seq)
                               .bn(tx.getNum())
                               .hash(tx.getHash())
                               .bTime(tx.getTime())
                               .txFee(tx.getCost())
                               .fromType(newAddressCache.getAddressType(event.getFrom()).getCode())
                               .toType(newAddressCache.getAddressType(event.getTo()).getCode())
                               .operator(event.getOperator())
                               .from(event.getFrom())
                               .to(event.getTo())
                               .tokenId(event.getTokenId().toString())
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
     * 从交易回执的事件中解析出交易
     *
     * @param token:
     * @param tx:
     * @param eventList:
     * @param seq:
     * @return: java.util.List<com.platon.browser.elasticsearch.dto.ErcTx>
     * @date: 2022/8/3
     */
    private List<ErcTx> resolveErc1155TxFromEvent(Token token, com.platon.browser.elasticsearch.dto.Transaction tx, List<ErcContract.ErcTxEvent> eventList, AtomicLong seq) {
        List<ErcTx> txList = new ArrayList<>();
        eventList.forEach(event -> {
            //event.to地址可能是新地址
            newAddressCache.addPendingAddressToBlockCtx(event.getTo());
            log.info("event.to:{}地址可能是新地址", event.getTo());
            // 转换参数进行设置内部交易
            ErcTx ercTx = ErcTx.builder()
                               .seq(seq.incrementAndGet())
                               .bn(tx.getNum())
                               .hash(tx.getHash())
                               .bTime(tx.getTime())
                               .txFee(tx.getCost())
                               .fromType(newAddressCache.getAddressType(event.getFrom()).getCode())
                               .toType(newAddressCache.getAddressType(event.getTo()).getCode())
                               .operator(event.getOperator())
                               .from(event.getFrom())
                               .to(event.getTo())
                               .tokenId(event.getTokenId().toString())
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
        if (StrUtil.isNotBlank(from) && !AddressUtil.isAddrZero(from)) {
            newAddressCache.addPendingAddressToBlockCtx(from);
        }
        if (StrUtil.isNotBlank(to) && !AddressUtil.isAddrZero(to)) {
            newAddressCache.addPendingAddressToBlockCtx(to);
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
            // 性能低，少用
            // BeanUtils.copyProperties(tx, eti);
            ErcTxInfo eti = ErcTxInfo.convertFromErcTx(tx);
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
    public void resolveTokenTransferTx(Block collectionBlock, com.platon.browser.elasticsearch.dto.Transaction tx, Receipt receipt) {
        try {
            // 过滤交易回执日志，地址不能为空且在token缓存里的
            List<Log> tokenLogs = receipt.getLogs()
                                         .stream()
                                         .filter(receiptLog -> StrUtil.isNotEmpty(receiptLog.getAddress())) //log.address!=null，说明是合约交易
                                         .filter(receiptLog -> newAddressCache.isToken(receiptLog.getAddress())) //只需要token的日志
                                         .collect(Collectors.toList());

            if (CollUtil.isEmpty(tokenLogs)) {
                log.debug("区块{}中交易数量为0", collectionBlock.getNum());
                return;
            }

            log.debug("开始分析区块的token交易，块高：{}", collectionBlock.getNum());
            StopWatch watch = new StopWatch("分析区块token交易");

            tokenLogs.forEach(tokenLog -> {
                Token token = newAddressCache.getToken(tokenLog.getAddress());
                if (ObjectUtil.isNotNull(token)) {
                    List<ErcTx> txList;
                    String contractAddress = token.getAddress();
                    ErcTypeEnum typeEnum = ErcTypeEnum.valueOf(token.getType().toUpperCase());
                    TransactionReceipt transactionReceipt = new TransactionReceipt();
                    transactionReceipt.setLogs(receipt.getLogs().stream().filter(v -> v.getAddress().equalsIgnoreCase(contractAddress)).collect(Collectors.toList()));
                    transactionReceipt.setContractAddress(contractAddress);
                    List<ErcContract.ErcTxEvent> eventList;
                    switch (typeEnum) {
                        case ERC20:
                            watch.start("获取ERC20合约的事件列表");
                            eventList = ercDetectService.getErc20TransferTxEvents(transactionReceipt, BigInteger.valueOf(collectionBlock.getNum()));
                            List<ErcContract.ErcTxEvent> erc20TxEventList = eventList.stream().filter(v -> ObjectUtil.equal(v.getLog(), tokenLog)).collect(Collectors.toList());
                            if (erc20TxEventList.size() > 1) {
                                log.error("当前交易[{}]erc20交易回执日志解析异常{}", tx.getHash(), tokenLog);
                                break;
                            }
                            watch.stop();
                            watch.start("获取ERC20合约内部交易");
                            txList = resolveErcTransferTxFromEvent(token, tx, erc20TxEventList, collectionBlock.getSeq().incrementAndGet());
                            tx.getErc20TxList().addAll(txList);
                            watch.stop();
                            //
                            // 更新token持有者余额，以及持有者交易次数
                            // 分析token内部交易
                            watch.start("分析ERC20合约token的持有人余额情况");
                            ercTokenHolderAnalyzer.analyze(txList);
                            watch.stop();
                            break;
                        case ERC721:
                            watch.start("获取ERC721合约的事件列表");
                            eventList = ercDetectService.getErc721TransferTxEvents(transactionReceipt, BigInteger.valueOf(collectionBlock.getNum()));
                            List<ErcContract.ErcTxEvent> erc721TxEventList = eventList.stream().filter(v -> v.getLog().equals(tokenLog)).collect(Collectors.toList());
                            if (erc721TxEventList.size() > 1) {
                                log.error("当前交易[{}]erc721交易回执日志解析异常{}", tx.getHash(), tokenLog);
                                break;
                            }
                            watch.stop();
                            watch.start("获取ERC721合约内部交易");
                            txList = resolveErcTransferTxFromEvent(token, tx, erc721TxEventList, collectionBlock.getSeq().incrementAndGet());
                            watch.stop();
                            tx.getErc721TxList().addAll(txList);
                            //
                            // 更新NFT的信息
                            watch.start("分析ERC721合约库存");
                            ercTokenInventoryAnalyzer.analyze(tx.getHash(), txList, BigInteger.valueOf(collectionBlock.getNum()));
                            watch.stop();
                            //
                            // 更新token持有者余额，以及持有者交易次数
                            // 分析token内部交易
                            watch.start("分析ERC721合约token的持有人余额情况");
                            ercTokenHolderAnalyzer.analyze(txList);
                            watch.stop();
                            break;
                        case ERC1155:
                            watch.start("获取ERC1155合约的事件列表");
                            eventList = ercDetectService.getErc1155TransferTxEvents(transactionReceipt, BigInteger.valueOf(collectionBlock.getNum()));
                            List<ErcContract.ErcTxEvent> erc1155TxEventList = eventList.stream().filter(v -> v.getLog().equals(tokenLog)).collect(Collectors.toList());
                            watch.stop();
                            watch.start("获取ERC1155合约内部交易");
                            txList = resolveErc1155TxFromEvent(token, tx, erc1155TxEventList, collectionBlock.getSeq());
                            tx.getErc1155TxList().addAll(txList);
                            watch.stop();
                            //
                            // 更新NFT的信息
                            watch.start("分析ERC1155合约库存");
                            erc1155TokenInventoryAnalyzer.analyze(tx.getHash(), txList, BigInteger.valueOf(collectionBlock.getNum()));
                            watch.stop();
                            //
                            // 更新token持有者余额，以及持有者交易次数
                            // 分析token内部交易
                            watch.start("分析ERC1155合约token的持有人余额情况");
                            ercToken1155HolderAnalyzer.analyze(txList);
                            watch.stop();
                            break;
                        default:
                            break;
                    }
                    token.setUpdateTime(new Date());
                    //token.setDirty(true);
                } else {
                    log.error("当前交易[{}]缓存中未找到合约地址[{}]对应的Erc Token", tx.getHash(), tokenLog.getAddress());
                }
            });
            tx.setErc20TxInfo(getErcTxInfo(tx.getErc20TxList()));
            tx.setErc721TxInfo(getErcTxInfo(tx.getErc721TxList()));
            tx.setErc1155TxInfo(getErcTxInfo(tx.getErc1155TxList()));
            log.debug("当前交易[{}]有[{}]笔log,其中token交易有[{}]笔，其中erc20有[{}]笔,其中erc721有[{}]笔,其中erc1155有[{}]笔",
                     tx.getHash(),
                     CommonUtil.ofNullable(() -> receipt.getLogs().size()).orElse(0),
                     CommonUtil.ofNullable(() -> tokenLogs.size()).orElse(0),
                     CommonUtil.ofNullable(() -> tx.getErc20TxList().size()).orElse(0),
                     CommonUtil.ofNullable(() -> tx.getErc721TxList().size()).orElse(0),
                     CommonUtil.ofNullable(() -> tx.getErc1155TxList().size()).orElse(0));

            // 针对销毁的合约处理
            if (tx.getType() == com.platon.browser.elasticsearch.dto.Transaction.TypeEnum.CONTRACT_EXEC_DESTROY.getCode()) {
                //lvixaoyi:合约被销毁后，需要最后查询一次它的totalSupply(),可以在这里做，也可以在scan-job的ErcTokenUpdateTask里做(目前是ErcTokenUpdateTask里做)
                watch.start("处理合约销毁");
                Token token = new Token();
                token.setAddress(tx.getTo());
                token.setContractDestroyBlock(collectionBlock.getNum());
                tokenMapper.updateByPrimaryKeySelective(token);
                watch.stop();
                log.debug("合约: {}在区块：{}已销毁", receipt.getContractAddress(), collectionBlock.getNum());
            }

            log.debug("结束分析区块的token交易，块高：{}，耗时统计：{}", collectionBlock.getNum(), watch.prettyPrint());
        } catch (Exception e) {
            log.error(String.format("当前区别：%d 的交易： %s 解析ERC交易异常", collectionBlock.getNum(), tx.getHash()), e);
            throw e;
        }
    }

}
