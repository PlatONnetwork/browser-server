package com.platon.browser.service.erc20;

import com.alaya.abi.solidity.datatypes.Event;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.ReadonlyTransactionManager;
import com.alaya.tx.gas.DefaultGasProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.Erc20Param;
import com.platon.browser.utils.CalculateUtils;
import com.platon.browser.utils.NetworkParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:36
 */
@Slf4j
@Component
public class Erc20ResolveServiceImpl extends Erc20ServiceImpl {

    @Resource
    private PlatOnClient platOnClient;
    @Resource
    private Erc20RetryService erc20RetryService;
    @Resource
    private BlockChainConfig blockChainConfig;
    @Resource
    private AddressCache addressCache;

    public void initContractAddressCache(List<Transaction> transactions) {
        Map<String, Erc20Token> erc20Tokens = new ConcurrentHashMap<>();
        transactions.stream().filter(transaction -> transaction.getOldErcTxes().size() > 0)
            .forEach(transaction -> {
                List<Erc20Param> erc20Params = new ArrayList<>();
                transaction.getOldErcTxes().forEach(esTokenTransferRecord -> {
                    // 存量的erc20参数，提高访问速度
                    Erc20Token erc20Token = erc20Tokens.get(esTokenTransferRecord.getContract());

                    BigDecimal totalSupply = BigDecimal.ZERO;
                    // 先缓存中获取
                    if (erc20Token == null) {
                        erc20Token = addressCache.getErc20Token(esTokenTransferRecord.getContract());
                        // 防止参数erc数据为null
                        if (null == erc20Token || StringUtils.isBlank(erc20Token.getName())) {
                            erc20Token = null;
                        } else {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    // 如果数据不存在再从从数据库中获取数据
                    if (erc20Token == null) {
                        erc20Token = erc20RetryService.getErc20Token(esTokenTransferRecord.getContract());
                        if (null != erc20Token) {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    //补充交易中的合约信息
                    if (erc20Token != null) {
                        esTokenTransferRecord.setDecimal(erc20Token.getDecimal());
                        esTokenTransferRecord.setName(erc20Token.getName());
                        esTokenTransferRecord.setSymbol(erc20Token.getSymbol());
                        totalSupply = erc20Token.getTotalSupply();
                    }
                    esTokenTransferRecord.setTxFee(transaction.getCost());
                    // 更新token交易数
                    addressCache.updateTokenAddress(esTokenTransferRecord.getFrom());
                    addressCache.updateTokenAddress(esTokenTransferRecord.getTto());
                    // 更新erc交易数
                    addressCache.updateErcTx(esTokenTransferRecord.getContract());

                    //对token交易的from和to进行补充，后续在StatisticsAddressConverter中进行入库。
                    if (esTokenTransferRecord.getFrom().equals(esTokenTransferRecord.getTto())) {
                        //如果from和to都是同一个则存储一份即可
                        Erc20TokenAddressRel erc20TokenAddressRelFrom =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getFrom())
                                        .balance(BigDecimal.ZERO).updateTime(new Date()).totalSupply(totalSupply).txCount(1)
                                        .build();
                        addressCache.putErc20TokenAddressRelMap(UUID.randomUUID().toString(),
                                erc20TokenAddressRelFrom);
                    } else {
                        //from和to不一样则都需要存储
                        Erc20TokenAddressRel erc20TokenAddressRelTo =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getTto())
                                        .balance(new BigDecimal(esTokenTransferRecord.getTValue())).updateTime(new Date()).totalSupply(totalSupply).txCount(1).build();
                        addressCache.putErc20TokenAddressRelMap("to" + UUID.randomUUID().toString(),
                                erc20TokenAddressRelTo);
                        Erc20TokenAddressRel erc20TokenAddressRelFrom =
                                Erc20TokenAddressRel.builder().decimal(esTokenTransferRecord.getDecimal())
                                        .contract(esTokenTransferRecord.getContract()).name(esTokenTransferRecord.getName())
                                        .symbol(esTokenTransferRecord.getSymbol()).address(esTokenTransferRecord.getFrom())
                                        .balance(new BigDecimal(esTokenTransferRecord.getTValue()).negate()).updateTime(new Date()).totalSupply(totalSupply).txCount(1)
                                        .build();
                        addressCache.putErc20TokenAddressRelMap("form" + UUID.randomUUID().toString(),
                                erc20TokenAddressRelFrom);
                    }


                    // 存储erc20参数到交易的info中
                    Erc20Param erc20Param = Erc20Param.builder().innerContractAddr(esTokenTransferRecord.getContract())
                            .innerContractName(esTokenTransferRecord.getName()).innerFrom(esTokenTransferRecord.getFrom())
                            .innerSymbol(esTokenTransferRecord.getSymbol()).innerTo(esTokenTransferRecord.getTto())
                            .innerDecimal(String.valueOf(esTokenTransferRecord.getDecimal()))
                            .innerValue(esTokenTransferRecord.getTValue()).build();
                    erc20Params.add(erc20Param);
                });
                transaction.setInfo(JSONObject.toJSONString(erc20Params));
            });
    }

    public List<String> getContractAddressFromEvents(TransactionReceipt transactionReceipt) {
        List<ExtendEvent.EventWrapper> eventWrappers = CalculateUtils.buildEvents(blockChainConfig);
        if (null == eventWrappers || eventWrappers.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> contractList = new ArrayList<>();
        ExtendEvent extendEvent = new ExtendEvent("", this.platOnClient.getWeb3jWrapper().getWeb3j(),
                new ReadonlyTransactionManager(this.platOnClient.getWeb3jWrapper().getWeb3j(), ""),
                new DefaultGasProvider(),
                NetworkParams.getChainId());
        for (ExtendEvent.EventWrapper eventWrapper : eventWrappers) {
            Event event = ExtendEvent.buildEvent(eventWrapper.getEventName(), eventWrapper.getEventDefineList());
            // 根据地址位置，判定是否为 indexed
            boolean indexed = eventWrapper.getEventDefineList().get(eventWrapper.getAddressIndex()).isIndexed();
            List<ExtendEvent.TokenContractResponse> tokenContractResponseList = extendEvent.getContractAddressList(event, transactionReceipt, eventWrapper.getAddressIndex(), indexed);
            if (tokenContractResponseList != null && !tokenContractResponseList.isEmpty()) {
                for (ExtendEvent.TokenContractResponse tokenContractResponse : tokenContractResponseList) {
                    contractList.add(tokenContractResponse.getAddress());
                }
            }
        }
        log.info("根据事件解析代币地址，获取到的合约地址为：" + JSON.toJSONString(contractList));
        return contractList;
    }

}