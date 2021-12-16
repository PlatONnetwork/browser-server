package com.platon.browser.analyzer.statistic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.analyzer.TransactionAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomAddress;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsAddressAnalyzer {

    @Resource
    private AddressCache addressCache;

    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    public void analyze(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})",
                  block.getNum(),
                  event.getTransactions().size(),
                  epochMessage.getConsensusEpochRound(),
                  epochMessage.getSettleEpochRound(),
                  epochMessage.getIssueEpochRound());
        List<String> addresses = addressCache.getAll().stream().map(Address::getAddress).collect(Collectors.toList());
        // 从数据库中查询出与缓存中对应的地址信息
        AddressExample condition = new AddressExample();
        condition.createCriteria().andAddressIn(addresses);
        List<Address> itemFromDb = addressMapper.selectByExampleWithBLOBs(condition);
        // 0区块初始化内置地址
        if (block.getNum().compareTo(0L) == 0 && CollUtil.isEmpty(itemFromDb)) {
            addressCache.getAll().forEach(address -> {
                ContractTypeEnum contractTypeEnum = TransactionAnalyzer.getGeneralContractAddressCache().get(address.getAddress());
                if (contractTypeEnum != null) {
                    switch (contractTypeEnum) {
                        case WASM:
                            address.setType(CustomAddress.TypeEnum.WASM.getCode());
                            break;
                        case EVM:
                            address.setType(CustomAddress.TypeEnum.EVM.getCode());
                            break;
                        case ERC20_EVM:
                            address.setType(CustomAddress.TypeEnum.ERC20_EVM.getCode());
                            break;
                        case ERC721_EVM:
                            address.setType(CustomAddress.TypeEnum.ERC721_EVM.getCode());
                            break;
                    }
                }
            });
            List<Address> list = CollUtil.newArrayList(addressCache.getAll());
            statisticBusinessMapper.batchInsert(list);
            log.info("初始化内置地址入库成功:{}", JSONUtil.toJsonStr(list));
            return;
        } else if (CollUtil.isNotEmpty(itemFromDb)) {
            // 查看交易列表中是否有bin属性为0x的交易,有则对to对应的合约地址进行设置
            event.getTransactions().forEach(tx -> {
                // 如果tx的bin为0x，表明这笔交易是销毁合约交易或调用已销毁合约交易, to地址必定是合约地址
                if ("0x".equals(tx.getBin())) {
                    itemFromDb.forEach(address -> {
                        if (address.getAddress().equalsIgnoreCase(tx.getTo())) {
                            if (StringUtils.isBlank(address.getContractDestroyHash())) {
                                // 如果当前地址缓存的销毁交易地址为空，则设置
                                address.setContractDestroyHash(tx.getHash());
                            }
                        }
                    });
                }
            });
            itemFromDb.forEach(address -> {
                Address addCache = addressCache.getAddress(address.getAddress());
                // 合约名称
                String contractName = address.getContractName();
                if (StringUtils.isBlank(contractName)) contractName = addCache.getContractName();
                address.setContractName(contractName);
                // 合约创建人，数据库的值优先
                String contractCreate = address.getContractCreate();
                if (StringUtils.isBlank(contractCreate)) contractCreate = addCache.getContractCreate();
                address.setContractCreate(contractCreate);
                // 合约创建交易hash，数据库的值优先
                String contractCreateHash = address.getContractCreatehash();
                if (StringUtils.isBlank(contractCreateHash)) contractCreateHash = addCache.getContractCreatehash();
                address.setContractCreatehash(contractCreateHash);
                // 合约销毁交易hash，数据库的值优先
                String contractDestroyHash = address.getContractDestroyHash();
                if (StringUtils.isBlank(contractDestroyHash)) contractDestroyHash = addCache.getContractDestroyHash();
                address.setContractDestroyHash(contractDestroyHash);
                // 合约bin代码数据
                String contractBin = address.getContractBin();
                if (StringUtils.isBlank(contractBin)) contractBin = addCache.getContractBin();
                address.setContractBin(contractBin);
            });
            int i = customAddressMapper.batchUpdateAddressInfo(itemFromDb);
            if (i > 0) {
                log.info("批量更新地址信息成功，成功数:{}，数据为：{}", i, JSONUtil.toJsonStr(itemFromDb));
            }
        }
        // 对比缓存和数据的数据，取出缓存中新增的地址
        List<String> dbList = itemFromDb.stream().map(Address::getAddress).collect(Collectors.toList());
        List<String> newAddressList = CollUtil.subtractToList(addresses, dbList);
        List<Address> newAddrList = new ArrayList<>();
        if (CollUtil.isNotEmpty(newAddressList)) {
            addressCache.getAll().forEach(address -> {
                ContractTypeEnum contractTypeEnum = TransactionAnalyzer.getGeneralContractAddressCache().get(address.getAddress());
                if (contractTypeEnum != null) {
                    switch (contractTypeEnum) {
                        case WASM:
                            address.setType(CustomAddress.TypeEnum.WASM.getCode());
                            break;
                        case EVM:
                            address.setType(CustomAddress.TypeEnum.EVM.getCode());
                            break;
                        case ERC20_EVM:
                            address.setType(CustomAddress.TypeEnum.ERC20_EVM.getCode());
                            break;
                        case ERC721_EVM:
                            address.setType(CustomAddress.TypeEnum.ERC721_EVM.getCode());
                            break;
                    }
                }
            });
            newAddressList.forEach(address -> {
                newAddrList.add(addressCache.getAddress(address));
            });
        }
        if (CollUtil.isNotEmpty(newAddrList)) {
            statisticBusinessMapper.batchInsert(newAddrList);
            log.info("新增地址成功{}", JSONUtil.toJsonStr(newAddrList));
        }
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
