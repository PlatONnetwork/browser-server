package com.platon.browser.analyzer.statistic;

import com.platon.browser.analyzer.TransactionAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomAddress;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.param.statistic.AddressStatChange;
import com.platon.browser.dao.param.statistic.AddressStatItem;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatisticsAddressAnalyzer {

    @Resource
    private AddressCache addressCache;

    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;

    @Resource
    private AddressMapper addressMapper;

    public void analyze(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})", block.getNum(),
                event.getTransactions().size(), epochMessage.getConsensusEpochRound(), epochMessage.getSettleEpochRound(),
                epochMessage.getIssueEpochRound());
        // 使用缓存中的地址统计信息构造入库参数列表
        List<AddressStatItem> itemFromCache = new ArrayList<>();
        List<String> addresses = new ArrayList<>();
        this.addressCache.getAll().forEach(cache -> {
            AddressStatItem item = AddressStatItem.builder().address(cache.getAddress()).type(cache.getType())
                    .txQty(cache.getTxQty())
                    .erc20TxQty(cache.getErc20TxQty())
                    .erc721TxQty(cache.getErc721TxQty())
                    .transferQty(cache.getTransferQty())
                    .delegateQty(cache.getDelegateQty())
                    .stakingQty(cache.getStakingQty()).proposalQty(cache.getProposalQty())
                    .contractName(cache.getContractName()).contractCreate(cache.getContractCreate())
                    .contractCreatehash(cache.getContractCreatehash()).contractDestroyHash(cache.getContractDestroyHash())
                    .contractBin(cache.getContractBin()).haveReward(cache.getHaveReward()).build();
            // 检查当前地址是否是普通合约地址
            ContractTypeEnum contractTypeEnum = TransactionAnalyzer.getGeneralContractAddressCache().get(cache.getAddress());
            if (contractTypeEnum != null) {
                switch (contractTypeEnum) {
                    case WASM:
                        item.setType(CustomAddress.TypeEnum.WASM.getCode());
                        break;
                    case EVM:
                        item.setType(CustomAddress.TypeEnum.EVM.getCode());
                        break;
                    case ERC20_EVM:
                        item.setType(CustomAddress.TypeEnum.ERC20_EVM.getCode());
                        break;
                    case ERC721_EVM:
                        item.setType(CustomAddress.TypeEnum.ERC721_EVM.getCode());
                        break;
                }
            }
            itemFromCache.add(item);
            addresses.add(cache.getAddress());
        });
        // 清空地址缓存 ******************缓存清空操作在CollectionEventHandler的finally语句块执行，防止中间出错出现脏缓存*********************
        // this.addressCache.cleanAll();
        // 从数据库中查询出与缓存中对应的地址信息
        AddressExample condition = new AddressExample();
        condition.createCriteria().andAddressIn(addresses);
        List<Address> itemFromDb = this.addressMapper.selectByExampleWithBLOBs(condition);
        // 地址与详情进行映射
        Map<String, Address> dbMap = new HashMap<>();
        itemFromDb.forEach(item -> dbMap.put(item.getAddress(), item));
        // 合并数据库与缓存中的值
        Map<String, AddressStatItem> cacheMap = new HashMap<>();
        itemFromCache.forEach(fromCache -> {
            cacheMap.put(fromCache.getAddress(), fromCache);
            Address fromDb = dbMap.get(fromCache.getAddress());
            if (null != fromDb) {
                log.info("地址[{}]的交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getTxQty() + fromCache.getTxQty(), fromDb.getTxQty(), fromCache.getTxQty());
                fromCache.setTxQty(fromDb.getTxQty() + fromCache.getTxQty()); // 交易数量
                log.info("地址[{}]的erc20交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getErc20TxQty() + fromCache.getErc20TxQty(), fromDb.getErc20TxQty(), fromCache.getErc20TxQty());
                fromCache.setErc20TxQty(fromDb.getErc20TxQty() + fromCache.getErc20TxQty()); // token交易数量
                log.info("地址[{}]的erc721交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getErc721TxQty() + fromCache.getErc721TxQty(), fromDb.getErc721TxQty(), fromCache.getErc721TxQty());
                fromCache.setErc721TxQty(fromDb.getErc721TxQty() + fromCache.getErc721TxQty()); // token交易数量
                log.info("地址[{}]的转账交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getTransferQty() + fromCache.getTransferQty(), fromDb.getTransferQty(), fromCache.getTransferQty());
                fromCache.setTransferQty(fromDb.getTransferQty() + fromCache.getTransferQty()); // 转账数量
                log.info("地址[{}]的委托交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getDelegateQty() + fromCache.getDelegateQty(), fromDb.getDelegateQty(), fromCache.getDelegateQty());
                fromCache.setDelegateQty(fromDb.getDelegateQty() + fromCache.getDelegateQty()); // 委托数量
                log.info("地址[{}]的质押交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getStakingQty() + fromCache.getStakingQty(), fromDb.getStakingQty(), fromCache.getStakingQty());
                fromCache.setStakingQty(fromDb.getStakingQty() + fromCache.getStakingQty()); // 质押数量
                log.info("地址[{}]的提案交易数[{}]=数据库[{}]+缓存[{}]", fromDb.getAddress(), fromDb.getProposalQty() + fromCache.getProposalQty(), fromDb.getProposalQty(), fromCache.getProposalQty());
                fromCache.setProposalQty(fromDb.getProposalQty() + fromCache.getProposalQty()); // 提案数量
                fromCache.setHaveReward(fromDb.getHaveReward().add(fromCache.getHaveReward())); // 已领取委托奖励总额
                // 合约创建人，数据库的值优先
                String contractCreate = fromDb.getContractCreate();
                if (StringUtils.isBlank(contractCreate))
                    contractCreate = fromCache.getContractCreate();
                fromCache.setContractCreate(contractCreate);
                // 合约创建交易hash，数据库的值优先
                String contractCreateHash = fromDb.getContractCreatehash();
                if (StringUtils.isBlank(contractCreateHash))
                    contractCreateHash = fromCache.getContractCreatehash();
                fromCache.setContractCreatehash(contractCreateHash);
                // 合约销毁交易hash，数据库的值优先
                String contractDestroyHash = fromDb.getContractDestroyHash();
                if (StringUtils.isBlank(contractDestroyHash))
                    contractDestroyHash = fromCache.getContractDestroyHash();
                fromCache.setContractDestroyHash(contractDestroyHash);
                // 合约bin代码数据
                String contractBin = fromDb.getContractBin();
                if (StringUtils.isBlank(contractBin))
                    contractBin = fromCache.getContractBin();
                fromCache.setContractBin(contractBin);
                // 合约名称
                String contractName = fromCache.getContractName();
                if (StringUtils.isBlank(contractName))
                    contractName = fromDb.getContractName();
                fromCache.setContractName(contractName);

                fromCache.setType(fromDb.getType()); // 不能让缓存覆盖数据库中的地址类型
            }
        });

        // 查看交易列表中是否有bin属性为0x的交易,有则对to对应的合约地址进行设置
        event.getTransactions().forEach(tx -> {
            // 如果tx的bin为0x，表明这笔交易是销毁合约交易或调用已销毁合约交易, to地址必定是合约地址
            if ("0x".equals(tx.getBin())) {
                AddressStatItem item = cacheMap.get(tx.getTo());
                if (item != null && StringUtils.isBlank(item.getContractDestroyHash())) {
                    // 如果当前地址缓存的销毁交易地址为空，则设置
                    item.setContractDestroyHash(tx.getHash());
                }
            }

        });

        // 使用合并后的信息构造地址入库参数
        AddressStatChange addressStatChange = AddressStatChange.builder().addressStatItemList(itemFromCache).build();
        this.statisticBusinessMapper.addressChange(addressStatChange);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
