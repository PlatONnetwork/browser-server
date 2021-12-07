package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.platon.browser.bean.AddressErcQty;
import com.platon.browser.bean.TokenQty;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.PointLog;
import com.platon.browser.dao.mapper.PointLogMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.service.elasticsearch.AbstractEsRepository;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.utils.AddressUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
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

    @Resource
    private PointLogMapper pointLogMapper;

    /**
     * 更新erc交易数
     *
     * @param :
     * @return: void
     * @date: 2021/12/6
     */
    @XxlJob("updateTokenQtyJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateTokenQty() throws Exception {
        try {
            int pageSize = Convert.toInt(XxlJobHelper.getJobParam(), 500);
            Map<String, TokenQty> tokenMap = new HashMap<>();
            Map<String, AddressErcQty> addressMap = new HashMap<>();
            PointLog erc20PointLog = pointLogMapper.selectByPrimaryKey(3);
            List<ErcTx> erc20List = getErcTxList(esErc20TxRepository, Convert.toLong(erc20PointLog.getPosition()), pageSize);
            if (CollUtil.isNotEmpty(erc20List)) {
                Map<String, List<ErcTx>> erc20Map = erc20List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
                //累计token的erc20交易数
                for (Map.Entry<String, List<ErcTx>> entry : erc20Map.entrySet()) {
                    TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                    tokenQty.setErc20TxQty(entry.getValue().size());
                }
                //累计地址的erc20交易数
                for (ErcTx ercTx : erc20List) {
                    AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                    AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                    if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo()) && !AddressUtil.isAddrZero(ercTx.getFrom())) {
                        fromAddressErcQty.setErc20TxQty(fromAddressErcQty.getErc20TxQty() + 1);
                        log.info("该erc20交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                    } else {
                        if (!AddressUtil.isAddrZero(fromAddressErcQty.getAddress())) {
                            fromAddressErcQty.setErc20TxQty(fromAddressErcQty.getErc20TxQty() + 1);
                        } else {
                            log.info("该erc20交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), fromAddressErcQty.getAddress());
                        }
                        if (!AddressUtil.isAddrZero(toAddressErcQty.getAddress())) {
                            toAddressErcQty.setErc20TxQty(toAddressErcQty.getErc20TxQty() + 1);
                        } else {
                            log.info("该erc20交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), toAddressErcQty.getAddress());
                        }
                    }
                }
                //记录最大的seq
                erc20PointLog.setPosition(CollUtil.getLast(erc20List).getSeq().toString());
            }
            PointLog erc721PointLog = pointLogMapper.selectByPrimaryKey(4);
            List<ErcTx> erc721List = getErcTxList(esErc721TxRepository, Convert.toLong(erc721PointLog.getPosition()), pageSize);
            if (CollUtil.isNotEmpty(erc721List)) {
                Map<String, List<ErcTx>> erc721Map = erc721List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
                //累计token的erc721交易数
                for (Map.Entry<String, List<ErcTx>> entry : erc721Map.entrySet()) {
                    TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                    tokenQty.setErc721TxQty(entry.getValue().size());
                    tokenQty.setTokenTxQty(tokenQty.getErc20TxQty() + tokenQty.getErc721TxQty());
                }
                //累计地址的erc721交易数
                for (ErcTx ercTx : erc721List) {
                    AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                    AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                    if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo()) && !AddressUtil.isAddrZero(ercTx.getFrom())) {
                        fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                        log.info("该erc721交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                    } else {
                        if (!AddressUtil.isAddrZero(fromAddressErcQty.getAddress())) {
                            fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                        } else {
                            log.info("该erc721交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), fromAddressErcQty.getAddress());
                        }
                        if (!AddressUtil.isAddrZero(toAddressErcQty.getAddress())) {
                            toAddressErcQty.setErc721TxQty(toAddressErcQty.getErc721TxQty() + 1);
                        } else {
                            log.info("该erc721交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), toAddressErcQty.getAddress());
                        }
                    }
                }
                //记录最大的seq
                erc721PointLog.setPosition(CollUtil.getLast(erc721List).getSeq().toString());
            }
            if (CollUtil.isNotEmpty(tokenMap.values())) {
                customTokenMapper.batchUpdateTokenQty(CollUtil.newArrayList(tokenMap.values()));
            }
            if (CollUtil.isNotEmpty(addressMap.values())) {
                customAddressMapper.batchUpdateAddressErcQty(CollUtil.newArrayList(addressMap.values()));
            }
            if (CollUtil.isNotEmpty(erc20List)) {
                pointLogMapper.updateByPrimaryKeySelective(erc20PointLog);
            }
            if (CollUtil.isNotEmpty(erc721List)) {
                pointLogMapper.updateByPrimaryKeySelective(erc721PointLog);
            }
            XxlJobHelper.handleSuccess("更新erc交易数成功");
        } catch (Exception e) {
            log.error("更新erc交易数异常", e);
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
     * 获取es信息
     *
     * @param abstractEsRepository:
     * @param maxSeq:
     * @param pageSize:
     * @return: java.util.List<com.platon.browser.elasticsearch.dto.ErcTx>
     * @date: 2021/12/6
     */
    private List<ErcTx> getErcTxList(AbstractEsRepository abstractEsRepository, long maxSeq, int pageSize) throws Exception {
        try {
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setAsc("seq");
            constructor.setResult(new String[]{"seq", "name", "contract", "hash", "from", "to", "value", "bn"});
            ESQueryBuilders esQueryBuilders = new ESQueryBuilders();
            esQueryBuilders.listBuilders().add(QueryBuilders.rangeQuery("seq").gt(maxSeq));
            constructor.must(esQueryBuilders);
            constructor.setUnmappedType("long");
            ESResult<ErcTx> queryResultFromES = abstractEsRepository.search(constructor, ErcTx.class, 1, pageSize);
            return queryResultFromES.getRsData();
        } catch (Exception e) {
            log.error("获取交易列表异常", e);
            throw e;
        }
    }

}
