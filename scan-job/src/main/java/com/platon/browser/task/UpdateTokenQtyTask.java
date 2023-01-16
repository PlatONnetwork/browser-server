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
import com.platon.browser.dao.mapper.TxErc1155BakMapper;
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

    @Resource
    private TxErc1155BakMapper txErc1155BakMapper;

    /**
     * 根据：
     * 表tx_erc_20_bak / tx_erc_721_bak / tx_erc_1155_bak 中的合约交易记录，
     * 增量统计：（统计过的每类交易记录ID，将被记录到print_log表中）
     * 1. 每个erc token的交易记录总数（记录到：token.token_tx_qty）
     * 2. 每个账户地址涉及的各类erc token交易记录数（记录到：address.erc20_tx_qty / address.erc721_tx_qty / address.erc1155_tx_qty）
     *
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
            //
            // 重要：
            // 合约上交易数量的缓存key：合约地址; value:TokenQty，合约交易数量。
            Map<String, TokenQty> tokenMap = new HashMap<>();

            //
            // 重要：
            // 钱包地址上，ERC交易数量map，key:钱包地址；value:token交易数量
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
                //按合约地址分组，map<合约地址，List<合约交易>>
                Map<String, List<ErcTx>> erc20Map = erc20List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
                //累计token的erc20交易次数
                for (Map.Entry<String, List<ErcTx>> entry : erc20Map.entrySet()) {
                    //ERC20合约交易数量的缓存
                    TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                    //设置合约ERC20的交易次数
                    tokenQty.setErc20TxQty(entry.getValue().size());
                }
                //统计某地址上的erc20交易次数
                for (ErcTx ercTx : erc20List) {
                    //统计每个账户地址上的erc20交易次数
                    //每笔交易都有from/to，则from/to地址上的合约交易数量都要+1（特殊情况：0地址，from/to相同）
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
            // 1155
            PointLog erc1155PointLog = pointLogMapper.selectByPrimaryKey(10);
            long oldErc1155Position = Convert.toLong(erc1155PointLog.getPosition());
            TaskUtil.console("当前页码为[{}]，erc1155断点为[{}]", pageSize, oldErc1155Position);
            TxErc1155BakExample txErc1155BakExample = new TxErc1155BakExample();
            txErc1155BakExample.setOrderByClause("id");
            txErc1155BakExample.createCriteria().andIdGreaterThan(oldErc1155Position).andIdLessThanOrEqualTo(oldErc1155Position + pageSize);
            List<TxErc1155Bak> erc1155List = txErc1155BakMapper.selectByExample(txErc1155BakExample);
            if (CollUtil.isNotEmpty(erc1155List)) {
                TaskUtil.console("找到1155交易[{}]条", erc1155List.size());
                Map<String, List<ErcTx>> erc1155Map = erc1155List.stream().collect(Collectors.groupingBy(ErcTx::getContract));
                //累计token的erc1155交易数
                for (Map.Entry<String, List<ErcTx>> entry : erc1155Map.entrySet()) {
                    TokenQty tokenQty = getTokenQty(tokenMap, entry.getKey());
                    tokenQty.setErc1155TxQty(entry.getValue().size());
                }
                //累计地址的erc1155交易数
                for (ErcTx ercTx : erc1155List) {
                    AddressErcQty fromAddressErcQty = getAddressErcQty(addressMap, ercTx.getFrom());
                    AddressErcQty toAddressErcQty = getAddressErcQty(addressMap, ercTx.getTo());
                    if (ercTx.getFrom().equalsIgnoreCase(ercTx.getTo()) && !AddressUtil.isAddrZero(ercTx.getFrom())) {
                        fromAddressErcQty.setErc1155TxQty(fromAddressErcQty.getErc1155TxQty() + 1);
                        TaskUtil.console("该erc1155交易[{}]的from[{}]和to[{}]地址一致，erc交易数只算一次", ercTx.getHash(), ercTx.getFrom(), ercTx.getTo());
                    } else {
                        if (!AddressUtil.isAddrZero(fromAddressErcQty.getAddress())) {
                            fromAddressErcQty.setErc1155TxQty(fromAddressErcQty.getErc1155TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc1155交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), fromAddressErcQty.getAddress());
                        }
                        if (!AddressUtil.isAddrZero(toAddressErcQty.getAddress())) {
                            toAddressErcQty.setErc1155TxQty(toAddressErcQty.getErc1155TxQty() + 1);
                        } else {
                            TaskUtil.console("该erc1155交易[{}]下，零地址[{}]不统计交易数", ercTx.getHash(), toAddressErcQty.getAddress());
                        }
                    }
                }
                //记录最大的seq
                erc1155PointLog.setPosition(CollUtil.getLast(erc1155List).getId().toString());
            } else {
                TaskUtil.console("当前erc1155断点[{}]未找到erc1155交易", oldErc1155Position);
            }
            if (CollUtil.isNotEmpty(tokenMap.values())) {
                for (Map.Entry<String, TokenQty> entry : tokenMap.entrySet()) {
                    entry.getValue().setTokenTxQty(entry.getValue().getErc20TxQty() + entry.getValue().getErc721TxQty() + entry.getValue().getErc1155TxQty());
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
            if (CollUtil.isNotEmpty(erc1155List)) {
                pointLogMapper.updateByPrimaryKeySelective(erc1155PointLog);
                TaskUtil.console("更新erc交易数，erc1155断点为[{}]->[{}]", oldErc1155Position, erc1155PointLog.getPosition());
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
            AddressErcQty addressErcQty = AddressErcQty.builder().address(address).erc20TxQty(0).erc721TxQty(0).erc1155TxQty(0).build();
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
            TokenQty tokenQty = TokenQty.builder().contract(contract).tokenTxQty(0).erc20TxQty(0).erc721TxQty(0).erc1155TxQty(0).build();
            tokenMap.put(contract, tokenQty);
            return tokenQty;
        }
    }

}
