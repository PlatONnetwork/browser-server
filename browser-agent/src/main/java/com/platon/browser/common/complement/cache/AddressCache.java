package com.platon.browser.common.complement.cache;

import com.platon.browser.common.collection.dto.ComplementInfo;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.complement.dao.param.delegate.DelegateRewardClaim;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dto.CustomErc20Token;
import com.platon.browser.dto.ERCData;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.param.claim.Reward;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地址统计缓存
 */
@Component
public class AddressCache {
    // 当前地址缓存，此缓存会在StatisticsAddressConverter执行完业务逻辑后被清除，
    // 所以这是上一次执行StatisticsAddressConverter业务时到当前的累计地址缓存，并不是全部的
    private Map<String, Address> addressMap = new ConcurrentHashMap<>();

    private Map<String, Erc20Token> erc20TokenMap = new ConcurrentHashMap<>();

    private Map<String, Erc20TokenAddressRel> erc20TokenAddressRelMap = new ConcurrentHashMap<>();

    private Map<String, Erc20Token> preErc20TokenMap = new ConcurrentHashMap<>();

    // 全量EVM合约地址缓存
    private Set<String> evmContractAddressCache = new HashSet<>();

    public Set<String> getEvmContractAddressCache() {
        return this.evmContractAddressCache;
    }

    public boolean isEvmContractAddress(String address) {
        return this.evmContractAddressCache.contains(address);
    }

    // 全量WASM合约地址缓存
    private Set<String> wasmContractAddressCache = new HashSet<>();

    public Set<String> getWasmContractAddressCache() {
        return this.wasmContractAddressCache;
    }

    public boolean isWasmContractAddress(String address) {
        return this.wasmContractAddressCache.contains(address);
    }

    // 全量EVM-erc20合约地址缓存
    private Set<String> evmErc20ContractAddressCache = new HashSet<>();

    public Set<String> getEvmErc20ContractAddressCache() {
        return this.evmErc20ContractAddressCache;
    }

    public boolean isEvmErc20ContractAddress(String address) {
        return this.evmErc20ContractAddressCache.contains(address);
    }

    public Integer getTypeData(String address) {
        if (InnerContractAddrEnum.getAddresses().contains(address)) {
            return Transaction.ToTypeEnum.INNER_CONTRACT.getCode();
        } else if (this.isEvmContractAddress(address)) {
            return Transaction.ToTypeEnum.EVM_CONTRACT.getCode();
        } else if (this.isWasmContractAddress(address)) {
            return Transaction.ToTypeEnum.WASM_CONTRACT.getCode();
        } else if (this.isEvmErc20ContractAddress(address)) {
            return Transaction.ToTypeEnum.ERC20_CONTRACT.getCode();
        } else {
            return Transaction.ToTypeEnum.ACCOUNT.getCode();
        }
    }

    public void update(Transaction tx) {
        String from = tx.getFrom();
        String to = tx.getTo();
        String contractAddress = tx.getContractAddress();
        /**
         * 合约创建的from信息不能被覆盖成合约账户类型
         */
        switch (tx.getTypeEnum()) {
            case EVM_CONTRACT_CREATE:
            case WASM_CONTRACT_CREATE:
            case ERC20_CONTRACT_CREATE:
                this.updateContractFromAddress(tx, from);
                this.updateAddress(tx, contractAddress);
                break;
            default:
                this.updateAddress(tx, from);
                this.updateAddress(tx, to);
                break;
        }
    }

    // 先初始化合约map，防止后续合约交易找不到对应的合约而统计错误
    public void updateFirst(String addr, ComplementInfo ci) {
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            switch (ContractTypeEnum.getEnum(ci.getContractType())) {
                case EVM:
                    address.setType(AddressTypeEnum.EVM_CONTRACT.getCode());
                    this.evmContractAddressCache.add(addr);
                    break;
                case WASM:
                    address.setType(AddressTypeEnum.WASM_CONTRACT.getCode());
                    this.wasmContractAddressCache.add(addr);
                    break;
                case ERC20_EVM:
                    address.setType(AddressTypeEnum.ERC20_EVM_CONTRACT.getCode());
                    this.evmErc20ContractAddressCache.add(addr);
                    break;
                default:
                    break;
            }
            this.addressMap.put(addr, address);
        }
    }

    public Collection<Address> getAll() {
        return this.addressMap.values();
    }

    public Collection<Erc20Token> getAllErc20Token() {
        return this.erc20TokenMap.values();
    }

    public Erc20Token getErc20Token(String contractAddress) {
        Erc20Token erc20Token = this.erc20TokenMap.get(contractAddress);
        // 防止参数erc数据为null
        if (null == erc20Token) {
            erc20Token = this.preErc20TokenMap.get(contractAddress);
        }
        return erc20Token;
    }

    public void cleanAll() {
        this.addressMap.clear();
    }

    public synchronized void cleanErc20TokenCache() {
        this.preErc20TokenMap.clear();
        this.preErc20TokenMap.putAll(this.erc20TokenMap);
        this.erc20TokenMap.clear();
    }

    public Map<String, Erc20TokenAddressRel> getErc20TokenAddressRelMap() {
        return this.erc20TokenAddressRelMap;
    }

    public Erc20TokenAddressRel putErc20TokenAddressRelMap(String key, Erc20TokenAddressRel erc20TokenAddressRel) {
        return this.erc20TokenAddressRelMap.put(key, erc20TokenAddressRel);
    }

    public void cleanErc20TokenAddressRelMap() {
        this.erc20TokenAddressRelMap.clear();
    }

    private void updateAddress(Transaction tx, String addr) {
        if (addr == null)
            return;
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            this.addressMap.put(addr, address);
        }

        address.setTxQty(address.getTxQty() + 1);
        switch (tx.getTypeEnum()) {
            case TRANSFER: // 转账交易
                address.setTransferQty(address.getTransferQty() + 1);
                break;
            case STAKE_CREATE:// 创建验证人
            case STAKE_INCREASE:// 增加自有质押
            case STAKE_MODIFY:// 编辑验证人
            case STAKE_EXIT:// 退出验证人
            case REPORT:// 举报验证人
                address.setStakingQty(address.getStakingQty() + 1);
                break;
            case DELEGATE_CREATE:// 发起委托
            case DELEGATE_EXIT:// 撤销委托
            case CLAIM_REWARDS:// 领取委托奖励
                address.setDelegateQty(address.getDelegateQty() + 1);
                break;
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_PARAMETER:// 创建参数提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明
                address.setProposalQty(address.getProposalQty() + 1);
                break;
            case EVM_CONTRACT_CREATE:
                // 如果地址是EVM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.EVM_CONTRACT.getCode());
                this.evmContractAddressCache.add(addr);
                address.setContractBin(tx.getBin());
                break;
            case ERC20_CONTRACT_CREATE:
                // 如果地址是EVM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.ERC20_EVM_CONTRACT.getCode());
                this.evmErc20ContractAddressCache.add(addr);
                address.setContractBin(tx.getBin());
                break;
            case WASM_CONTRACT_CREATE:
                // 如果地址是WASM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.WASM_CONTRACT.getCode());
                this.wasmContractAddressCache.add(addr);
                address.setContractBin(tx.getBin());
                break;
            default:
        }
    }

    private void updateContractFromAddress(Transaction tx, String addr) {
        if (addr == null)
            return;
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            this.addressMap.put(addr, address);
        }

        address.setTxQty(address.getTxQty() + 1);
    }

    public void updateTokenAddress(String addr) {
        if (addr == null)
            return;
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            this.addressMap.put(addr, address);
        }
        address.setTokenQty(address.getTokenQty() + 1);
    }

    private Address createDefaultAddress(String addr) {
        Address address = new Address();
        address.setAddress(addr);
        // 设置地址类型
        if (InnerContractAddrEnum.getAddresses().contains(addr)) {
            // 内置合约地址
            address.setType(AddressTypeEnum.INNER_CONTRACT.getCode());
        } else {
            // 先默认置为账户地址，具体是什么类型，由调用此方法的后续逻辑决定并设置
            address.setType(AddressTypeEnum.ACCOUNT.getCode());
        }

        ContractDescEnum cde = ContractDescEnum.getMap().get(addr);
        if (cde != null) {
            address.setContractName(cde.getContractName());
            address.setContractCreate(cde.getCreator());
            address.setContractCreatehash(cde.getContractHash());
        } else {
            address.setContractName("");
            address.setContractCreate("");
            address.setContractCreatehash("");
        }

        address.setTxQty(0);
        address.setTokenQty(0);
        address.setTransferQty(0);
        address.setStakingQty(0);
        address.setDelegateQty(0);
        address.setProposalQty(0);
        address.setHaveReward(BigDecimal.ZERO);
        return address;
    }

    /**
     * 初始化默认参数填入erc数据中
     *
     * @param addr
     * @return
     */
    public synchronized Erc20Token createDefaultErc20(String addr) {
        Erc20Token erc20Token = this.erc20TokenMap.get(addr);
        if (erc20Token == null) {
            erc20Token = Erc20Token.builder().address(addr).createTime(new Date()).symbol("")
                    .totalSupply(BigDecimal.ZERO).name("").decimal(0).status(CustomErc20Token.StatusEnum.HIDE.getCode())
                    .creator("").txHash("").blockTimestamp(new Date()).type("").txCount(0).holder(0).build();
        }
        return erc20Token;
    }

    /**
     * 创建erc20合约时候调用初始化参数
     * 
     * @param addr
     * @param creator
     * @param txHash
     * @param time
     * @param type
     * @param ercData
     * @return
     */
    public Erc20Token createFirstErc20(String addr, String creator, String txHash, Date time, String type,
        ERCData ercData) {
        Erc20Token erc20Token = this.createDefaultErc20(addr);
        erc20Token.setAddress(addr);
        erc20Token.setCreateTime(new Date());
        erc20Token.setSymbol(ercData.getSymbol());
        erc20Token.setTotalSupply(new BigDecimal(ercData.getTotalSupply()));
        erc20Token.setName(ercData.getName());
        erc20Token.setDecimal(ercData.getDecimal());
        erc20Token.setCreator(creator);
        erc20Token.setTxHash(txHash);
        erc20Token.setBlockTimestamp(time);
        erc20Token.setType(type);
        erc20Token.setTxCount(0);
        erc20Token.setHolder(0);
        erc20Token.setStatus(0);
        this.erc20TokenMap.put(addr, erc20Token);
        return erc20Token;
    }

    /**
     * 对地址进行更新加1
     *
     * @param addr
     */
    public synchronized void updateErcTx(String addr) {
        Erc20Token erc20Token = this.createDefaultErc20(addr);
        erc20Token.setTxCount(erc20Token.getTxCount() + 1);
        this.erc20TokenMap.put(addr, erc20Token);
    }

    /**
     * 对地址进行持有加1
     *
     * @param addr
     */
    public synchronized void updateErcHolder(String addr) {
        Erc20Token erc20Token = this.createDefaultErc20(addr);
        erc20Token.setHolder(erc20Token.getHolder() + 1);
        this.erc20TokenMap.put(addr, erc20Token);
    }

    /**
     * 初始化EVM地址缓存
     *
     * @param addressList 地址实体列表
     */
    public void initEvmContractAddressCache(List<Address> addressList) {
        if (addressList.isEmpty())
            return;
        this.evmContractAddressCache.clear();
        addressList.forEach(address -> {
            if (address.getType() == AddressTypeEnum.EVM_CONTRACT.getCode()) {
                this.evmContractAddressCache.add(address.getAddress());
            }
        });
    }

    /**
     * 初始化EVM-ERC20地址缓存
     *
     * @param addressList
     *            地址实体列表
     */
    public void initEvmErc20ContractAddressCache(List<Address> addressList) {
        if (addressList.isEmpty())
            return;
        this.evmErc20ContractAddressCache.clear();
        addressList.forEach(address -> {
            if (address.getType() == AddressTypeEnum.ERC20_EVM_CONTRACT.getCode()) {
                this.evmErc20ContractAddressCache.add(address.getAddress());
            }
        });
    }

    /**
     * 初始化WASM地址缓存
     * 
     * @param addressList
     *            地址实体列表
     */
    public void initWasmContractAddressCache(List<Address> addressList) {
        if (addressList.isEmpty())
            return;
        this.wasmContractAddressCache.clear();
        addressList.forEach(address -> {
            if (address.getType() == AddressTypeEnum.WASM_CONTRACT.getCode()) {
                this.wasmContractAddressCache.add(address.getAddress());
            }
        });
    }

    /**
     * 第一次启动初始化
     */
    public void initOnFirstStart() {
        for (ContractDescEnum contractDescEnum : ContractDescEnum.values()) {
            this.addressMap.put(contractDescEnum.getAddress(),
                this.createDefaultAddress(contractDescEnum.getAddress()));
        }
    }

    /**
     * 领取奖励交易更新已领取的委托奖励字段
     * 
     * @param drc
     */
    public void update(DelegateRewardClaim drc) {
        // 统计当前交易from地址的【已领取委托奖励】
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Reward reward : drc.getRewardList()) {
            totalAmount = totalAmount.add(reward.getReward());
        }
        this.update(drc.getAddress(), totalAmount);
    }

    /**
     * 撤销委托交易更新已领取的委托奖励字段
     * 
     * @param de
     */
    public void update(DelegateExit de) {
        this.update(de.getTxFrom(), de.getDelegateReward());
    }

    private void update(String address, BigDecimal amount) {
        Address cache = this.addressMap.get(address);
        if (cache == null) {
            cache = this.createDefaultAddress(address);
            this.addressMap.put(address, cache);
        }
        cache.setHaveReward(cache.getHaveReward().add(amount));
    }
}
