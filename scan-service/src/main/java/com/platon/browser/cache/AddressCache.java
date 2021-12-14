package com.platon.browser.cache;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.enums.ContractDescEnum;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.v0152.analyzer.ErcCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地址统计缓存，谨慎使用
 * 在A区块由BlockEventHandler进入CollectionEventHandler，还没有来得及清除缓存的时候，A+1区块进入到BlockEventHandler或CollectionEventHandler中就会读取到脏数据
 *
 * @date 2021/4/20
 */
@Slf4j
@Component
public class AddressCache {

    @Resource
    private ErcCache ercCache;

    // 当前地址缓存，此缓存会在StatisticsAddressConverter执行完业务逻辑后被清除，
    // 所以这是上一次执行StatisticsAddressConverter业务时到当前的累计地址缓存，并不是全部的
    private Map<String, Address> addressMap = new ConcurrentHashMap<>();

    // 全量EVM合约地址缓存
    private Set<String> evmContractAddressCache = new HashSet<>();

    public Set<String> getEvmContractAddressCache() {
        return this.evmContractAddressCache;
    }

    public boolean isEvmContractAddress(String address) {
        return this.evmContractAddressCache.contains(address);
    }

    public boolean isErc20ContractAddress(String address) {
        return ercCache.getErc20AddressCache().contains(address);
    }

    public boolean isErc721ContractAddress(String address) {
        return ercCache.getErc721AddressCache().contains(address);
    }

    // 全量WASM合约地址缓存
    private Set<String> wasmContractAddressCache = new HashSet<>();

    public Set<String> getWasmContractAddressCache() {
        return this.wasmContractAddressCache;
    }

    public boolean isWasmContractAddress(String address) {
        return this.wasmContractAddressCache.contains(address);
    }

    public Integer getTypeData(String address) {
        if (InnerContractAddrEnum.getAddresses().contains(address)) return Transaction.ToTypeEnum.INNER_CONTRACT.getCode();
        if (this.isEvmContractAddress(address)) return Transaction.ToTypeEnum.EVM_CONTRACT.getCode();
        if (this.isWasmContractAddress(address)) return Transaction.ToTypeEnum.WASM_CONTRACT.getCode();
        if (isErc20ContractAddress(address)) return Transaction.ToTypeEnum.ERC20_CONTRACT.getCode();
        if (isErc721ContractAddress(address)) return Transaction.ToTypeEnum.ERC721_CONTRACT.getCode();
        return Transaction.ToTypeEnum.ACCOUNT.getCode();
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
            case ERC721_CONTRACT_CREATE:
                this.updateContractFromAddress(from);
                this.updateAddress(tx, contractAddress);
                break;
            default:
                this.updateAddress(tx, from);
                this.updateAddress(tx, to);
                break;
        }
    }

    // 先初始化合约map，防止后续合约交易找不到对应的合约而统计错误
    public void updateFirst(String addr, ContractTypeEnum contractTypeEnum) {
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            switch (contractTypeEnum) {
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
                    break;
                case ERC721_EVM:
                    address.setType(AddressTypeEnum.ERC721_EVM_CONTRACT.getCode());
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

    public void cleanAll() {
        this.addressMap.clear();
    }

    public void updateAddress(Transaction tx, String addr) {
        if (addr == null) return;
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            this.addressMap.put(addr, address);
        }
        switch (tx.getTypeEnum()) {
            case TRANSFER: // 转账交易
            case STAKE_CREATE:// 创建验证人
            case STAKE_INCREASE:// 增加自有质押
            case STAKE_MODIFY:// 编辑验证人
            case STAKE_EXIT:// 退出验证人
            case REPORT:// 举报验证人
            case DELEGATE_CREATE:// 发起委托
            case DELEGATE_EXIT:// 撤销委托
            case CLAIM_REWARDS:// 领取委托奖励
            case PROPOSAL_TEXT:// 创建文本提案
            case PROPOSAL_UPGRADE:// 创建升级提案
            case PROPOSAL_PARAMETER:// 创建参数提案
            case PROPOSAL_VOTE:// 提案投票
            case PROPOSAL_CANCEL:// 取消提案
            case VERSION_DECLARE:// 版本声明
            case EVM_CONTRACT_CREATE:
                // 如果地址是EVM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.EVM_CONTRACT.getCode());
                this.evmContractAddressCache.add(addr);
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
            case ERC20_CONTRACT_CREATE:
                // 如果地址是EVM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.ERC20_EVM_CONTRACT.getCode());
                address.setContractBin(tx.getBin());
                break;
            case ERC721_CONTRACT_CREATE:
                // 如果地址是EVM合约创建的回执里返回的合约地址
                address.setContractCreatehash(tx.getHash());
                address.setContractCreate(tx.getFrom());
                // 覆盖createDefaultAddress()中设置的值
                address.setType(AddressTypeEnum.ERC721_EVM_CONTRACT.getCode());
                address.setContractBin(tx.getBin());
                break;
            default:
        }
    }

    private void updateContractFromAddress(String addr) {
        if (addr == null) return;
        Address address = this.addressMap.get(addr);
        if (address == null) {
            address = this.createDefaultAddress(addr);
            this.addressMap.put(addr, address);
        }
    }

    /**
     * 创建默认的地址
     *
     * @param addr:
     * @return: com.platon.browser.dao.entity.Address
     * @date: 2021/12/2
     */
    public Address createDefaultAddress(String addr) {
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
        address.setErc20TxQty(0);
        address.setErc721TxQty(0);
        address.setTransferQty(0);
        address.setStakingQty(0);
        address.setDelegateQty(0);
        address.setProposalQty(0);
        address.setHaveReward(BigDecimal.ZERO);
        return address;
    }

    /**
     * 初始化EVM地址缓存
     *
     * @param addressList 地址实体列表
     */
    public void initEvmContractAddressCache(List<Address> addressList) {
        if (addressList.isEmpty()) return;
        this.evmContractAddressCache.clear();
        addressList.forEach(address -> {
            if (address.getType() == AddressTypeEnum.EVM_CONTRACT.getCode()) {
                this.evmContractAddressCache.add(address.getAddress());
            }
        });
    }

    /**
     * 初始化WASM地址缓存
     *
     * @param addressList 地址实体列表
     */
    public void initWasmContractAddressCache(List<Address> addressList) {
        if (addressList.isEmpty()) return;
        this.wasmContractAddressCache.clear();
        addressList.forEach(address -> {
            if (address.getType() == AddressTypeEnum.WASM_CONTRACT.getCode()) {
                this.wasmContractAddressCache.add(address.getAddress());
            }
        });
    }

    /**
     * 初始化内置地址,第一次启动初始化
     */
    public void initOnFirstStart() {
        log.info("初始化内置地址");
        for (ContractDescEnum contractDescEnum : ContractDescEnum.values()) {
            this.addressMap.put(contractDescEnum.getAddress(), this.createDefaultAddress(contractDescEnum.getAddress()));
        }
    }

    /**
     * 获取缓存地址对象
     *
     * @param address 地址
     * @return: com.platon.browser.dao.entity.Address
     * @date: 2021/6/29
     */
    public Address getAddress(String address) {
        Address cache = this.addressMap.get(address);
        return cache;
    }

    /**
     * 添加地址
     *
     * @param address:
     * @return: void
     * @date: 2021/12/14
     */
    public void addAddress(Address address) {
        this.addressMap.put(address.getAddress(), address);
    }

}
