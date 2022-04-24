package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.AddressErcQty;
import com.platon.browser.bean.TokenQty;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomTokenMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.PointLogMapper;
import com.platon.browser.dao.mapper.TxErc20BakMapper;
import com.platon.browser.dao.mapper.TxErc721BakMapper;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.TaskUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
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
    private CustomTokenMapper customTokenMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Resource
    private PointLogMapper pointLogMapper;

    @Resource
    private TxErc20BakMapper txErc20BakMapper;

    @Resource
    private TxErc721BakMapper txErc721BakMapper;

    /**
     * 更新erc交易数
     * 每5分钟执行一次
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
            long oldErc20Position = Convert.toLong(erc20PointLog.getPosition());
            TaskUtil.console("当前页数为[{}]，erc20断点为[{}]", pageSize, oldErc20Position);
            TxErc20BakExample txErc20BakExample = new TxErc20BakExample();
            txErc20BakExample.setOrderByClause("id asc limit " + pageSize);
            txErc20BakExample.createCriteria().andIdGreaterThan(oldErc20Position);
            List<TxErc20Bak> erc20List = txErc20BakMapper.selectByExample(txErc20BakExample);
            if (CollUtil.isNotEmpty(erc20List)) {
                TaskUtil.console("找到erc20交易[{}]条", erc20List.size());
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
                        TaskUtil.console("该erc20交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                    } else {
                        if (!AddressUtil.isAddrZero(fromAddressErcQty.getAddress())) {
                            fromAddressErcQty.setErc20TxQty(fromAddressErcQty.getErc20TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc20交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), fromAddressErcQty.getAddress());
                        }
                        if (!AddressUtil.isAddrZero(toAddressErcQty.getAddress())) {
                            toAddressErcQty.setErc20TxQty(toAddressErcQty.getErc20TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc20交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), toAddressErcQty.getAddress());
                        }
                    }
                }
                //记录最大的seq
                erc20PointLog.setPosition(CollUtil.getLast(erc20List).getId().toString());
            } else {
                TaskUtil.console("当前erc20断点[{}]未找到erc20交易", oldErc20Position);
            }
            PointLog erc721PointLog = pointLogMapper.selectByPrimaryKey(4);
            long oldErc721Position = Convert.toLong(erc721PointLog.getPosition());
            TaskUtil.console("当前页码为[{}]，erc721断点为[{}]", pageSize, oldErc721Position);
            TxErc721BakExample txErc721BakExample = new TxErc721BakExample();
            txErc721BakExample.setOrderByClause("id asc limit " + pageSize);
            txErc721BakExample.createCriteria().andIdGreaterThan(oldErc721Position);
            List<TxErc721Bak> erc721List = txErc721BakMapper.selectByExample(txErc721BakExample);
            if (CollUtil.isNotEmpty(erc721List)) {
                TaskUtil.console("找到erc721交易[{}]条", erc721List.size());
                Map<String, List<ErcTx>> erc721Map = erc721List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
                //累计token的erc721交易数
                for (Map.Entry<String, List<ErcTx>> entry : erc721Map.entrySet()) {
                    TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                    tokenQty.setErc721TxQty(entry.getValue().size());
                }
                //累计地址的erc721交易数
                for (ErcTx ercTx : erc721List) {
                    AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                    AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                    if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo()) && !AddressUtil.isAddrZero(ercTx.getFrom())) {
                        fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                        TaskUtil.console("该erc721交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                    } else {
                        if (!AddressUtil.isAddrZero(fromAddressErcQty.getAddress())) {
                            fromAddressErcQty.setErc721TxQty(fromAddressErcQty.getErc721TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc721交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), fromAddressErcQty.getAddress());
                        }
                        if (!AddressUtil.isAddrZero(toAddressErcQty.getAddress())) {
                            toAddressErcQty.setErc721TxQty(toAddressErcQty.getErc721TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc721交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), toAddressErcQty.getAddress());
                        }
                    }
                }
                //记录最大的seq
                erc721PointLog.setPosition(CollUtil.getLast(erc721List).getId().toString());
            } else {
                TaskUtil.console("当前erc721断点[{}]未找到erc721交易", oldErc721Position);
            }
            if (CollUtil.isNotEmpty(tokenMap.values())) {
                for (Map.Entry<String, TokenQty> entry : tokenMap.entrySet()) {
                    entry.getValue().setTokenTxQty(entry.getValue().getErc20TxQty() + entry.getValue().getErc721TxQty());
                }
                List<TokenQty> list = CollUtil.newArrayList(tokenMap.values());
                customTokenMapper.batchUpdateTokenQty(list);
                TaskUtil.console("更新token表的erc交易数，涉及的token数为[{}]，修改数据为{}", list.size(), JSONUtil.toJsonStr(list));
            }
            if (CollUtil.isNotEmpty(addressMap.values())) {
                List<AddressErcQty> list = CollUtil.newArrayList(addressMap.values());
                customAddressMapper.batchUpdateAddressErcQty(list);
                TaskUtil.console("更新地址表的erc交易数，涉及的address数为[{}]，修改数据为{}", list.size(), JSONUtil.toJsonStr(list));
            }
            if (CollUtil.isNotEmpty(erc20List)) {
                pointLogMapper.updateByPrimaryKeySelective(erc20PointLog);
                TaskUtil.console("更新erc交易数，erc20断点为[{}]->[{}]", oldErc20Position, erc20PointLog.getPosition());
            }
            if (CollUtil.isNotEmpty(erc721List)) {
                pointLogMapper.updateByPrimaryKeySelective(erc721PointLog);
                TaskUtil.console("更新erc交易数，erc721断点为[{}]->[{}]", oldErc721Position, erc721PointLog.getPosition());
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

}
