package com.platon.browser.complement.converter.statistic;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.complement.dao.param.statistic.AddressStatChange;
import com.platon.browser.complement.dao.param.statistic.AddressStatItem;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.ContractTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class StatisticsAddressConverter {

    @Autowired
    private AddressCache addressCache;
    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private Erc20TokenMapper erc20TokenMapper;
    @Autowired
    private CustomErc20TokenMapper customErc20TokenMapper;
    @Autowired
    private CustomErc20TokenAddressRelMapper customErc20TokenAddressRelMapper;
    @Autowired
    private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    public void convert(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})", block.getNum(),
                event.getTransactions().size(), epochMessage.getConsensusEpochRound(), epochMessage.getSettleEpochRound(),
                epochMessage.getIssueEpochRound());
        // 使用缓存中的地址统计信息构造入库参数列表
        List<AddressStatItem> itemFromCache = new ArrayList<>();
        List<String> addresses = new ArrayList<>();
        this.addressCache.getAll().forEach(cache -> {
            AddressStatItem item = AddressStatItem.builder().address(cache.getAddress()).type(cache.getType())
                .txQty(cache.getTxQty()).tokenQty(cache.getTokenQty()).transferQty(cache.getTransferQty())
                .delegateQty(cache.getDelegateQty())
                .stakingQty(cache.getStakingQty()).proposalQty(cache.getProposalQty())
                .contractName(cache.getContractName()).contractCreate(cache.getContractCreate())
                .contractCreatehash(cache.getContractCreatehash()).contractDestroyHash(cache.getContractDestroyHash())
                .contractBin(cache.getContractBin()).haveReward(cache.getHaveReward()).build();
            // 检查当前地址是否是普通合约地址
            ContractTypeEnum contractTypeEnum =
                CollectionTransaction.getGeneralContractAddressCache().get(cache.getAddress());
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
                }
            }
            itemFromCache.add(item);
            addresses.add(cache.getAddress());
        });
        // 清空地址缓存
        this.addressCache.cleanAll();
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
                fromCache.setTxQty(fromDb.getTxQty() + fromCache.getTxQty()); // 交易数量
                fromCache.setTokenQty(fromDb.getTokenQty() + fromCache.getTokenQty()); // token交易数量
                fromCache.setTransferQty(fromDb.getTransferQty() + fromCache.getTransferQty()); // 转账数量
                fromCache.setDelegateQty(fromDb.getDelegateQty() + fromCache.getDelegateQty()); // 委托数量
                fromCache.setStakingQty(fromDb.getStakingQty() + fromCache.getStakingQty()); // 质押数量
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

    public void erc20TokenConvert(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("erc20TokenConvert ~ block({}), transactions({}), consensus({}), settlement({}), issue({})",
                block.getNum(), event.getTransactions().size(), epochMessage.getConsensusEpochRound(),
                epochMessage.getSettleEpochRound(), epochMessage.getIssueEpochRound());
        }
        Collection<Erc20Token> erc20TokenList = this.addressCache.getAllErc20Token();
        if (erc20TokenList.isEmpty()) {
            return;
        }

        Map<String, Erc20Token> erc20TokenMap = new HashMap<>();

        List<Erc20Token> erc20TokenUpdateList = new ArrayList<>();
        List<String> addresses = new ArrayList<>();
        erc20TokenList.forEach(cacheErc20Token -> {
            erc20TokenMap.put(cacheErc20Token.getAddress(), cacheErc20Token);
            addresses.add(cacheErc20Token.getAddress());
        });
        this.addressCache.cleanErc20TokenCache();

        // 排除地址重复的数据
        Erc20TokenExample tokenCondition = new Erc20TokenExample();
        tokenCondition.createCriteria().andAddressIn(addresses);
        List<Erc20Token> tokenList = this.erc20TokenMapper.selectByExample(tokenCondition);

        // 过滤重复的数据，DB 中已经存在的，则不进行再次插入
        // 重复的数据实施更新，添加txCount数量
        tokenList.forEach(dbToken -> {
            Erc20Token erc20Token = erc20TokenMap.remove(dbToken.getAddress());
            if (erc20Token.getTxCount() != 0 || erc20Token.getHolder() != 0) {
                erc20TokenUpdateList.add(erc20Token);
            }
        });

        // 将增量新增的代币合约录入DB
        List<Erc20Token> params = new ArrayList<>();
        erc20TokenMap.values().forEach(t -> {
            // token在浏览器默认不显示
            t.setScanShow(0);
            params.add(t);
        });

        // batch save data.
        int result = 0;
        if (null != params && params.size() != 0) {
            result = this.erc20TokenMapper.batchInsert(params);
        }
        if (!erc20TokenUpdateList.isEmpty()) {
            this.customErc20TokenMapper.batchUpdate(erc20TokenUpdateList);
        }
        if (log.isDebugEnabled()) {
            log.debug("erc20TokenConvert ~ 处理耗时:{} ms",
                System.currentTimeMillis() - startTime + " 参数条数：{" + params.size() + "}，成功数量：{" + result + "}");
        }

    }

    public void erc20AddressConvert(CollectionEvent event, Block block, EpochMessage epochMessage) {
        long startTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("erc20AddressConvert ~ block({}), transactions({}), consensus({}), settlement({}), issue({})",
                    block.getNum(), event.getTransactions().size(), epochMessage.getConsensusEpochRound(),
                    epochMessage.getSettleEpochRound(), epochMessage.getIssueEpochRound());
        }
        Map<String, Erc20TokenAddressRel> erc20TokenAddressRelMap = this.addressCache.getErc20TokenAddressRelMap();
        if (erc20TokenAddressRelMap.isEmpty()) {
            return;
        }
        List<Erc20TokenAddressRel> queryList = new ArrayList<>(erc20TokenAddressRelMap.size());
        queryList.addAll(erc20TokenAddressRelMap.values());
        this.addressCache.cleanErc20TokenAddressRelMap();

        /**
         *查询已经存在的数据
         */
        List<Erc20TokenAddressRel> existsList = this.customErc20TokenAddressRelMapper.selectExistData(queryList);

        List<Erc20TokenAddressRel> updateParams = new ArrayList<>();

        Map<String, Erc20TokenAddressRel> insertParams = new HashMap<>();

        Set<String> balanceList = new HashSet<>();
        //进行数据匹配然后切换
        for (int i = 0; i < queryList.size(); i++) {
            Erc20TokenAddressRel qT = queryList.get(i);
            balanceList.add(this.fetchGroupKey(qT));
            for (int j = 0; j < existsList.size(); j++) {
                //数据匹配，则1、进行余额计算， 2、移除队列
                Erc20TokenAddressRel eT = existsList.get(j);
                if (eT.getAddress().equals(qT.getAddress()) && eT.getContract().equals(qT.getContract())) {
                    //如果余额为正数则进行扣减
//                    if (eT.getBalance().add(qT.getBalance()).compareTo(BigDecimal.ZERO) > 0) {
//                        eT.setBalance(eT.getBalance().add(qT.getBalance()));
//                    } else {
//                        //余额为负数则查询余额
//                        eT.setBalance(new BigDecimal(this.getAddressBalance(eT.getContract(), eT.getAddress())));
//                    }
                    eT.setTxCount(eT.getTxCount() + 1);
                    eT.setUpdateTime(new Date());
                    if (!updateParams.contains(eT)) {
                        updateParams.add(eT);
                    }
                    qT = null;
                    break;
                }
            }
            if (qT != null) {
                //对重复的相同的合约和地址的数据进行汇总总数
                String key = this.fetchGroupKey(qT);
                //插入的合约与地址映射关系不应该存在重复的情况，故需要以合约和地址两个参数维度进行去重
                Erc20TokenAddressRel temp = insertParams.get(key);
                if (temp == null) {
                    temp = qT;
                    //第一次插入的时候需要交易数累加
                    this.addressCache.updateErcHolder(temp.getContract());
                    insertParams.put(key, temp);
                } else {
                    temp.setTxCount(temp.getTxCount() + 1);
                }
            }
        }
        //余额丢到redis，由另一个队列进行查询补充
        this.redisTemplate.opsForSet().add(BrowserConst.ERC_BALANCE_KEY, balanceList.toArray(new String[]{}));
        //移除之后的队列就可以直接插入
        int result = 0;
        if (insertParams.size() > 0)
            result = this.erc20TokenAddressRelMapper.batchInsert(new ArrayList<>(insertParams.values()));
        if (updateParams.size() > 0) result = this.customErc20TokenAddressRelMapper.updateAddressData(updateParams);

        if (log.isDebugEnabled()) {
            log.debug("erc20AddressConvert ~ 处理耗时:{} ms",
                    System.currentTimeMillis() - startTime + " 参数条数：{" + insertParams.size() + "}，成功数量：{" + result + "}");
        }
    }

    private String fetchGroupKey(Erc20TokenAddressRel erc20TokenAddressRel) {
        return erc20TokenAddressRel.getContract() + BrowserConst.ERC_SPILT + erc20TokenAddressRel.getAddress();
    }

}
