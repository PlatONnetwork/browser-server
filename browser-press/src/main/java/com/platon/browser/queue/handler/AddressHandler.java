package com.platon.browser.queue.handler;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.queue.event.AddressEvent;
import com.platon.browser.queue.publisher.TokenHolderPublisher;
import com.platon.browser.queue.publisher.TokenInventoryPublisher;
import com.platon.browser.queue.publisher.TokenPublisher;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class AddressHandler extends AbstractHandler<AddressEvent> {

    @Autowired
    private AddressMapper addressMapper;

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Setter
    @Getter
    @Value("${disruptor.queue.address.batch-size}")
    private volatile int batchSize;

    @Value("${platon.addressMaxCount}")
    private long addressMaxCount;

    @Value("${platon.tokenERC20MaxCount}")
    private long tokenERC20MaxCount;

    @Value("${platon.tokenERC721MaxCount}")
    private long tokenERC721MaxCount;

    private Set<Address> stage = new HashSet<>();

    @Resource
    private TokenPublisher tokenPublisher;

    @Resource
    private TokenHolderPublisher tokenHolderPublisher;

    @Resource
    private TokenInventoryPublisher tokenInventoryPublisher;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(AddressEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        this.stage.addAll(event.getAddressList());
        log.info("stat:{},batchSize:{},getTotalCount():{},addressMaxCount:{}", this.stage.size(), this.batchSize, this.getTotalCount(), this.addressMaxCount);
        if (this.stage.size() < this.batchSize) {
            // 如果暂存数量小于批次
            if (this.getTotalCount() > this.addressMaxCount) {
                // 且当前地址数未达到指定数量
                return;
            }
        }
        Map<String, Address> addressMap = new HashMap<>();
        this.stage.forEach(address -> addressMap.put(address.getAddress(), address));
        addressMap.values().forEach(address -> {
            address.setBalance(BigDecimal.ZERO);
            address.setCandidateCount(0);
            address.setCreateTime(new Date());
            address.setDelegateHes(BigDecimal.ZERO);
            address.setDelegateLocked(BigDecimal.ZERO);
            address.setDelegateQty(0);
            address.setDelegateReleased(BigDecimal.ZERO);
            address.setDelegateValue(BigDecimal.ZERO);
            address.setProposalQty(0);
            address.setRedeemedValue(BigDecimal.ZERO);
            address.setRestrictingBalance(BigDecimal.ZERO);
            address.setStakingQty(0);
            address.setStakingValue(BigDecimal.ZERO);
            address.setTransferQty(0);
            address.setTxQty(0);
            address.setErc20TxQty(0);
            address.setErc721TxQty(0);
            address.setUpdateTime(new Date());
            List<Integer> list = new ArrayList() {{
                add(1);
                add(5);
                add(6);
            }};
            address.setType(list.get((int) (Math.random() * list.size())));
            address.setContractName("");
            address.setContractCreate("");
            address.setContractCreatehash("");
            address.setHaveReward(BigDecimal.ZERO);
        });

        AddressExample example = new AddressExample();
        example.createCriteria().andAddressIn(new ArrayList<>(addressMap.keySet()));
        List<Address> existList = this.addressMapper.selectByExample(example);
        List<String> existAddresses = new ArrayList<>();
        existList.forEach(address -> existAddresses.add(address.getAddress()));

        addressMap.keySet().removeAll(existAddresses);
        List<Address> addressList = new ArrayList<>(addressMap.values());

        try {
            if (this.getTotalCount() < this.addressMaxCount) {
                if (!addressList.isEmpty())
                    this.addressMapper.batchInsert(addressList);
                long endTime = System.currentTimeMillis();
                this.printTps("地址", addressList.size(), startTime, endTime);
            }
        } catch (Exception e) {
            log.error("insert address error", e);
        }
        // 构造【token】数据
        List<Token> tokenList = new ArrayList<>();
        List<Address> erc20List = addressList.stream().filter(address -> address.getType() == 5).collect(Collectors.toList());
        erc20List.forEach(v -> {
            Token copy = new Token();
            copy.setAddress(v.getAddress());
            copy.setType("erc20");
            copy.setIsSupportErc165(false);
            copy.setIsSupportErc20(true);
            copy.setIsSupportErc721(false);
            copy.setIsSupportErc721Enumeration(false);
            copy.setIsSupportErc721Metadata(false);
            copy.setTokenTxQty(1);
            copy.setHolder(1);
            copy.setName("erc20Test");
            copy.setSymbol("aLAT");
            copy.setTotalSupply(new BigDecimal("100000000000"));
            copy.setDecimal(18);
            tokenList.add(copy);
        });
        List<Address> erc721List = addressList.stream().filter(address -> address.getType() == 6).collect(Collectors.toList());
        erc721List.forEach(v -> {
            Token copy = new Token();
            copy.setAddress(v.getAddress());
            copy.setType("erc721");
            copy.setIsSupportErc165(false);
            copy.setIsSupportErc20(false);
            copy.setIsSupportErc721(true);
            copy.setIsSupportErc721Enumeration(true);
            copy.setIsSupportErc721Metadata(true);
            copy.setTokenTxQty(1);
            copy.setHolder(1);
            copy.setName("erc721Test");
            copy.setSymbol("aLAT");
            copy.setTotalSupply(new BigDecimal("100000000000"));
            copy.setDecimal(18);
            tokenList.add(copy);
        });
        List<Address> addrList = addressList.stream().filter(address -> address.getType() == 1).collect(Collectors.toList());
        List<TokenHolder> tokenHolderList = new ArrayList<>();
        List<TokenInventory> tokenInventoryList = new ArrayList<>();
        if (CollUtil.isNotEmpty(tokenList) && CollUtil.isNotEmpty(addrList)) {
            tokenList.forEach(v -> {
                TokenHolder tokenHolder = new TokenHolder();
                tokenHolder.setTokenAddress(v.getAddress());
                tokenHolder.setAddress(addrList.get((int) (Math.random() * addrList.size())).getAddress());
                tokenHolder.setBalance(new BigDecimal("0"));
                tokenHolder.setCreateTime(new Date());
                tokenHolder.setUpdateTime(new Date());
                tokenHolder.setTokenTxQty(1);
                tokenHolderList.add(tokenHolder);
                TokenInventory tokenInventory = new TokenInventory();
                tokenInventory.setName("");
                tokenInventory.setDescription("");
                tokenInventory.setImage("");
                tokenInventory.setCreateTime(new Date());
                tokenInventory.setUpdateTime(new Date());
                tokenInventory.setTokenTxQty(1);
                tokenInventory.setTokenOwnerTxQty(1);
                tokenInventory.setTokenAddress(v.getAddress());
                List<BigInteger> list = new ArrayList() {{
                    add(new BigInteger("1000000"));
                    add(new BigInteger("1000001"));
                    add(new BigInteger("1000002"));
                    add(new BigInteger("1000003"));
                    add(new BigInteger("1000004"));
                    add(new BigInteger("1000005"));
                }};
                BigInteger tokenId = list.get((int) (Math.random() * list.size()));
                tokenInventory.setTokenId(tokenId);
                tokenInventory.setOwner(addrList.get((int) (Math.random() * addrList.size())).getAddress());
                tokenInventoryList.add(tokenInventory);
            });
        }
        if (tokenPublisher.getTotalCount() < (tokenERC20MaxCount + tokenERC721MaxCount)) {
            tokenPublisher.publish(tokenList);
        }
        if (tokenHolderPublisher.getTotalCount() < (tokenERC20MaxCount + tokenERC721MaxCount)) {
            tokenHolderPublisher.publish(tokenHolderList);
        }
        if (tokenInventoryPublisher.getTotalCount() < tokenERC721MaxCount) {
            tokenInventoryPublisher.publish(tokenInventoryList);
        }
        this.stage.clear();
    }

}