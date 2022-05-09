package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.AddressQty;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.PointLogMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.task.bean.AddressStatistics;
import com.platon.browser.utils.AddressUtil;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.TaskUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


/**
 * 地址表补充
 * <pre>
 * 需求：补充的字段
 * staking_value     质押的金额
 * delegate_value    委托的金额
 * redeemed_value    赎回中的金额
 * candidate_count   已委托的验证人
 * delegate_hes      未锁定委托
 * delegate_locked   已锁定委托
 * delegate_released 赎回中的
 *
 * 注意事项
 * 地址表数量比较巨大
 * <pre/>
 * @author chendai
 */
@Component
@Slf4j
public class AddressUpdateTask {

    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private PointLogMapper pointLogMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    @Resource
    private TxBakMapper txBakMapper;

    /**
     * 用于地址表更新
     */
    private AtomicLong addressStart = new AtomicLong(0L);

    /**
     * 地址表信息补充
     * 每5秒执行一次
     *
     * @param :
     * @return: void
     * @date: 2021/12/7
     */
    @XxlJob("addressUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void addressUpdate() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        try {
            int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 1000);
            batchUpdate(addressStart.intValue(), batchSize);
            XxlJobHelper.handleSuccess(StrUtil.format("地址表信息补充成功,当前标识为[{}]", addressStart.get()));
        } catch (Exception e) {
            log.error("地址表信息补充异常", e);
            throw e;
        }
    }

    /**
     * 执行任务
     *
     * @param start 开始的块高
     * @param size  执行的批次
     * @return
     */
    protected void batchUpdate(int start, int size) {
        //查询待补充的地址
        AddressExample addressExample = new AddressExample();
        addressExample.setOrderByClause("create_time limit " + start + "," + size);
        List<Address> addressList = addressMapper.selectByExample(addressExample);
        if (CollUtil.isEmpty(addressList)) {
            addressStart.set(0L);
            return;
        } else {
            addressStart.set(addressStart.get() + addressList.size());
        }
        List<String> addressStringList = addressList.stream().map(Address::getAddress).collect(Collectors.toList());
        //查询该地址发起的质押（有效的质押和赎回的质押）
        List<AddressStatistics> stakingList = statisticBusinessMapper.getAddressStatisticsFromStaking(addressStringList);
        //查询该地址发起的委托
        List<AddressStatistics> delegationList = statisticBusinessMapper.getAddressStatisticsFromDelegation(addressStringList);
        //汇总结果
        Map<String, AddressStatistics> stakingMap = stakingList.stream().collect(Collectors.toMap(AddressStatistics::getStakingAddr, v -> v, (v1, v2) -> {
            v1.setStakingHes(v1.getStakingHes().add(v2.getStakingHes()));
            v1.setStakingLocked(v1.getStakingLocked().add(v2.getStakingLocked()));
            v1.setStakingReduction(v1.getStakingReduction().add(v2.getStakingReduction()));
            return v1;
        }));
        Map<String, AddressStatistics> delegationMap = delegationList.stream().collect(Collectors.toMap(AddressStatistics::getDelegateAddr, v -> v, (v1, v2) -> {
            v1.setDelegateHes(v1.getDelegateHes().add(v2.getDelegateHes()));
            v1.setDelegateLocked(v1.getDelegateLocked().add(v2.getDelegateLocked()));
            v1.setDelegateReleased(v1.getDelegateReleased().add(v2.getDelegateReleased()));
            v1.getNodeIdSet().add(v2.getNodeId());
            return v1;
        }));
        List<Address> updateAddressList = new ArrayList<>();
        addressList.forEach(item -> {
            AddressStatistics staking = stakingMap.get(item.getAddress());
            AddressStatistics delegation = delegationMap.get(item.getAddress());
            boolean hasChange = false;
            BigDecimal stakingValue = staking == null ? BigDecimal.ZERO : staking.getStakingHes().add(staking.getStakingLocked());
            if (stakingValue.compareTo(item.getStakingValue()) != 0) {
                item.setStakingValue(stakingValue);
                hasChange = true;
            }

            BigDecimal stakingReduction = staking == null ? BigDecimal.ZERO : staking.getStakingReduction();
            if (stakingReduction.compareTo(item.getRedeemedValue()) != 0) {
                item.setRedeemedValue(stakingReduction);
                hasChange = true;
            }

            BigDecimal delegateHes = delegation == null ? BigDecimal.ZERO : delegation.getDelegateHes();
            if (delegateHes.compareTo(item.getDelegateHes()) != 0) {
                item.setDelegateHes(delegateHes);
                hasChange = true;
            }

            BigDecimal delegateLocked = delegation == null ? BigDecimal.ZERO : delegation.getDelegateLocked();
            if (delegateLocked.compareTo(item.getDelegateLocked()) != 0) {
                item.setDelegateLocked(delegateLocked);
                hasChange = true;
            }

            BigDecimal delegateValue = delegation == null ? BigDecimal.ZERO : delegation.getDelegateLocked().add(delegation.getDelegateHes());
            if (delegateValue.compareTo(item.getDelegateValue()) != 0) {
                item.setDelegateValue(delegateValue);
                hasChange = true;
            }

            BigDecimal delegateReleased = delegation == null ? BigDecimal.ZERO : delegation.getDelegateReleased();
            if (delegateReleased.compareTo(item.getDelegateReleased()) != 0) {
                item.setDelegateReleased(delegateReleased);
                hasChange = true;
            }

            if (delegation != null) {
                delegation.getNodeIdSet().add(delegation.getNodeId());
            }
            int candidateCount = delegation == null ? 0 : delegation.getNodeIdSet().size();
            if (candidateCount != item.getCandidateCount()) {
                item.setCandidateCount(candidateCount);
                hasChange = true;
            }

            if (hasChange) {
                updateAddressList.add(item);
            }
        });

        if (!updateAddressList.isEmpty()) {
            statisticBusinessMapper.batchUpdateFromTask(updateAddressList);
        }
    }

    /**
     * 更新地址交易数
     * 每30秒执行一次
     *
     * @param :
     * @return: void
     * @date: 2021/12/6
     */
    @XxlJob("updateAddressQtyJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateQty() throws Exception {
        try {
            int pageSize = Convert.toInt(XxlJobHelper.getJobParam(), 500);
            PointLog pointLog = pointLogMapper.selectByPrimaryKey(2);
            long oldPosition = Convert.toLong(pointLog.getPosition());
            TaskUtil.console("当前页数为[{}]，断点为[{}]", pageSize, oldPosition);
            List<TxBak> transactionList = getTransactionList(oldPosition, pageSize);
            if (CollUtil.isNotEmpty(transactionList)) {
                String minId = CollUtil.getFirst(transactionList).getId().toString();
                String maxId = CollUtil.getLast(transactionList).getId().toString();
                pointLog.setPosition(maxId);
                TaskUtil.console("查找到[{}]条交易,交易id为[{}-{}]", transactionList.size(), minId, maxId);
                Map<String, AddressQty> map = checkAddress(transactionList);
                TaskUtil.console("更新前的数据为{}", JSONUtil.toJsonStr(map.values()));
                for (TxBak txBak : transactionList) {
                    if (!AddressUtil.isAddrZero(txBak.getFrom())) {
                        AddressQty from = map.get(txBak.getFrom());
                        addQty(Transaction.TypeEnum.getEnum(txBak.getType()), from);
                    } else {
                        TaskUtil.console("交易[{}]from[{}]为零地址", txBak.getHash(), txBak.getFrom());
                    }
                    if (!AddressUtil.isAddrZero(txBak.getTo())) {
                        AddressQty to = map.get(txBak.getTo());
                        addQty(Transaction.TypeEnum.getEnum(txBak.getType()), to);
                    } else {
                        TaskUtil.console("交易[{}]to[{}]为零地址", txBak.getHash(), txBak.getTo());
                    }
                }
                List<AddressQty> list = CollUtil.newArrayList(map.values());
                customAddressMapper.batchUpdateAddressQty(list);
                pointLogMapper.updateByPrimaryKeySelective(pointLog);
                TaskUtil.console("更新后的数据为{}", JSONUtil.toJsonStr(map.values()));
                TaskUtil.console("更新地址交易数，断点(交易id)为[{}]->[{}]，更新[{}]个地址", oldPosition, pointLog.getPosition(), list.size());
            } else {
                XxlJobHelper.handleSuccess(StrUtil.format("最新断点[{}]未找到交易列表，更新地址交易数完成", oldPosition));
            }
        } catch (Exception e) {
            log.error("更新地址交易数异常", e);
            throw e;
        }
    }

    /**
     * 地址增加交易数
     *
     * @param typeEnum:
     * @param addressQty:
     * @return: void
     * @date: 2021/12/15
     */
    private void addQty(Transaction.TypeEnum typeEnum, AddressQty addressQty) {
        addressQty.setTxQty(addressQty.getTxQty() + 1);
        switch (typeEnum) {
            case TRANSFER:
                addressQty.setTransferQty(addressQty.getTransferQty() + 1);
                break;
            case EVM_CONTRACT_CREATE:
                break;
            case CONTRACT_EXEC:
                break;
            case WASM_CONTRACT_CREATE:
                break;
            case OTHERS:
                break;
            case MPC:
                break;
            case ERC20_CONTRACT_CREATE:
                break;
            case ERC20_CONTRACT_EXEC:
                break;
            case ERC721_CONTRACT_CREATE:
                break;
            case ERC721_CONTRACT_EXEC:
                break;
            case CONTRACT_EXEC_DESTROY:
                break;
            case STAKE_CREATE:
                addressQty.setStakingQty(addressQty.getStakingQty() + 1);
                break;
            case STAKE_MODIFY:
                addressQty.setStakingQty(addressQty.getStakingQty() + 1);
                break;
            case STAKE_INCREASE:
                addressQty.setStakingQty(addressQty.getStakingQty() + 1);
                break;
            case STAKE_EXIT:
                addressQty.setStakingQty(addressQty.getStakingQty() + 1);
                break;
            case DELEGATE_CREATE:
                addressQty.setDelegateQty(addressQty.getDelegateQty() + 1);
                break;
            case DELEGATE_EXIT:
                addressQty.setDelegateQty(addressQty.getDelegateQty() + 1);
                break;
            case PROPOSAL_TEXT:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case PROPOSAL_UPGRADE:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case PROPOSAL_PARAMETER:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case PROPOSAL_VOTE:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case VERSION_DECLARE:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case PROPOSAL_CANCEL:
                addressQty.setProposalQty(addressQty.getProposalQty() + 1);
                break;
            case REPORT:
                break;
            case RESTRICTING_CREATE:
                break;
            case CLAIM_REWARDS:
                addressQty.setDelegateQty(addressQty.getDelegateQty() + 1);
                break;
            default:
                break;
        }
    }

    /**
     * 校验地址
     *
     * @param transactionList:
     * @return: java.util.Map<java.lang.String, com.platon.browser.bean.AddressQty>
     * @date: 2021/12/15
     */
    private Map<String, AddressQty> checkAddress(List<TxBak> transactionList) throws Exception {
        Set<String> addressSet = new HashSet<>();
        Set<String> froms = transactionList.stream().map(TxBak::getFrom).filter(from -> !AddressUtil.isAddrZero(from)).collect(Collectors.toSet());
        Set<String> tos = transactionList.stream().map(TxBak::getTo).filter(to -> !AddressUtil.isAddrZero(to)).collect(Collectors.toSet());
        addressSet.addAll(froms);
        addressSet.addAll(tos);
        AddressExample example = new AddressExample();
        example.createCriteria().andAddressIn(new ArrayList<>(addressSet));
        List<Address> addressList = addressMapper.selectByExample(example);
        if (addressList.size() != addressSet.size()) {
            Set<String> address1 = addressList.stream().map(Address::getAddress).collect(Collectors.toSet());
            String msg = StrUtil.format("交易解出来的地址在数据库查找不到，缺失的地址为{}", JSONUtil.toJsonStr(CollUtil.subtractToList(addressSet, address1)));
            XxlJobHelper.log(msg);
            log.error(msg);
            throw new Exception("更新地址交易数异常");
        }
        Map<String, AddressQty> map = new HashMap();
        addressList.forEach(address -> {
            AddressQty addressQty = AddressQty.builder()
                                              .address(address.getAddress())
                                              .txQty(address.getTxQty())
                                              .transferQty(address.getTransferQty())
                                              .delegateQty(address.getDelegateQty())
                                              .stakingQty(address.getStakingQty())
                                              .proposalQty(address.getProposalQty())
                                              .build();
            map.put(address.getAddress(), addressQty);
        });
        return map;
    }

    /**
     * 获取交易列表
     *
     * @param maxId:
     * @param pageSize:
     * @return: java.util.List<com.platon.browser.elasticsearch.dto.Transaction>
     * @date: 2021/12/6
     */
    private List<TxBak> getTransactionList(long maxId, int pageSize) throws Exception {
        try {
            TxBakExample example = new TxBakExample();
            example.createCriteria().andIdGreaterThan(maxId);
            example.setOrderByClause("id asc limit " + pageSize);
            List<TxBak> list = txBakMapper.selectByExample(example);
            return list;
        } catch (Exception e) {
            log.error("获取交易列表异常", e);
            throw e;
        }
    }

}