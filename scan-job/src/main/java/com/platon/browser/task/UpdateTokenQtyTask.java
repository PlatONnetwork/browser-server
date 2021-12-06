package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.bean.AddressErcQty;
import com.platon.browser.bean.TokenQty;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.service.elasticsearch.AbstractEsRepository;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UpdateTokenQtyTask {

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private CustomTokenMapper customTokenMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void update() throws Exception {
        try {
            Map<String, TokenQty> tokenMap = new HashMap<>();
            Map<String, AddressErcQty> addressMap = new HashMap<>();
            List<ErcTx> erc20List = getErcTxList(esErc20TxRepository, 0, 100, 5000);
            Map<String, List<ErcTx>> erc20Map = erc20List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
            //累计token的erc20交易数
            for (Map.Entry<String, List<ErcTx>> entry : erc20Map.entrySet()) {
                TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                tokenQty.setErc20TxQty(entry.getValue().size());
            }
            List<ErcTx> erc721List = getErcTxList(esErc721TxRepository, 0, 100, 5000);
            Map<String, List<ErcTx>> erc721Map = erc721List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
            //累计token的erc721交易数
            for (Map.Entry<String, List<ErcTx>> entry : erc721Map.entrySet()) {
                TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                tokenQty.setErc721TxQty(entry.getValue().size());
                tokenQty.setTokenTxQty(tokenQty.getErc20TxQty() + tokenQty.getErc721TxQty());
            }
            //累计地址的erc20交易数
            for (ErcTx ercTx : erc20List) {
                AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo())) {
                    fromAddressErcQty.setErc20TxQty(fromAddressErcQty.getErc20TxQty() + 1);
                    log.info("该erc20交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                } else {
                    fromAddressErcQty.setErc20TxQty(fromAddressErcQty.getErc20TxQty() + 1);
                    toAddressErcQty.setErc20TxQty(toAddressErcQty.getErc20TxQty() + 1);
                }
            }
            //累计地址的erc721交易数
            for (ErcTx ercTx : erc721List) {
                AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo())) {
                    fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                    log.info("该erc721交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                } else {
                    fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                    toAddressErcQty.setErc721TxQty(toAddressErcQty.getErc721TxQty() + 1);
                }
            }
            if (CollUtil.isNotEmpty(tokenMap.values())) {
                customTokenMapper.batchUpdateTokenQty(CollUtil.newArrayList(tokenMap.values()));
            }
            if (CollUtil.isNotEmpty(addressMap.values())) {
                customAddressMapper.batchUpdateAddressErcQty(CollUtil.newArrayList(addressMap.values()));
            }
        } catch (Exception e) {
            log.error("更新交易数异常", e);
            throw e;
        }
    }

    /**
     * 获取AddressErcQty
     *
     * @param addressMap:
     * @param address:
     * @return: com.platon.browser.bean.AddressErcQty
     * @date: 2021/12/6
     */
    private AddressErcQty getAddressErcQty(Map<String, AddressErcQty> addressMap, String address) {
        if (addressMap.containsKey(address)) {
            return addressMap.get(address);
        } else {
            AddressErcQty addressErcQty = AddressErcQty.builder().address(address).erc20TxQty(0).erc721TxQty(0).build();
            addressMap.put(address, addressErcQty);
            return addressErcQty;
        }
    }

    /**
     * 获取TokenQty
     *
     * @param tokenMap:
     * @param contract:
     * @return: com.platon.browser.bean.TokenQty
     * @date: 2021/12/6
     */
    private TokenQty getTokenQty(Map<String, TokenQty> tokenMap, String contract) {
        if (tokenMap.containsKey(contract)) {
            return tokenMap.get(contract);
        } else {
            TokenQty tokenQty = TokenQty.builder().contract(contract).tokenTxQty(0).erc20TxQty(0).erc721TxQty(0).build();
            tokenMap.put(contract, tokenQty);
            return tokenQty;
        }
    }

    /**
     * 获取es信息 (minNum,maxNum]
     * minNum到maxNum区块之间的交易数要小于pageSize
     *
     * @param abstractEsRepository:
     * @param minNum:
     * @param maxNum:
     * @param pageSize:             每页大小
     * @return: java.util.List<com.platon.browser.elasticsearch.dto.ErcTx>
     * @date: 2021/12/6
     */
    private List<ErcTx> getErcTxList(AbstractEsRepository abstractEsRepository, long minNum, long maxNum, int pageSize) throws Exception {
        try {
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setAsc("seq");
            constructor.setResult(new String[]{"seq", "name", "contract", "hash", "from", "to", "value", "bn"});
            ESQueryBuilders esQueryBuilders = new ESQueryBuilders();
            esQueryBuilders.listBuilders().add(QueryBuilders.rangeQuery("num").gt(minNum).lte(maxNum));
            constructor.must(esQueryBuilders);
            constructor.setUnmappedType("long");
            ESResult<ErcTx> queryResultFromES = abstractEsRepository.search(constructor, ErcTx.class, 1, pageSize);
            return queryResultFromES.getRsData();
        } catch (Exception e) {
            log.error("更新地址交易数异常", e);
            throw e;
        }
    }

}
