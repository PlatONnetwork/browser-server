package com.platon.browser.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.AddressQty;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.entity.PointLog;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.PointLogMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilders;
import com.platon.browser.task.bean.AddressStatistics;
import com.platon.browser.utils.AppStatusUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    private EsTransactionRepository esTransactionRepository;

    @Resource
    private PointLogMapper pointLogMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    /**
     * 地址表信息补充
     *
     * @param :
     * @return: void
     * @date: 2021/12/7
     */
    @XxlJob("addressUpdateJobHandler")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void addressUpdate() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) return;
        try {
            int start = 0;
            int batchSize = Convert.toInt(XxlJobHelper.getJobParam(), 1000);
            while (!batchUpdate(start, batchSize)) {
                start += batchSize;
            }
            XxlJobHelper.handleSuccess("地址表信息补充成功");
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
    protected boolean batchUpdate(int start, int size) {
        boolean stop = false;

        //查询待补充的地址
        AddressExample addressExample = new AddressExample();
        addressExample.setOrderByClause("create_time limit " + start + "," + size);
        List<Address> addressList = addressMapper.selectByExample(addressExample);

        if (addressList.isEmpty()) {
            stop = true;
            return stop;
        }

        if (addressList.size() < size) {
            stop = true;
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
        return stop;
    }

    /**
     * 更新地址交易数
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
            List<Transaction> transactionList = getTransactionList(Convert.toLong(pointLog.getPosition()), pageSize);
            if (CollUtil.isNotEmpty(transactionList)) {
                Map<String, AddressQty> map = new HashMap();
                transactionList.forEach(transaction -> {
                    AddressQty contract = getAddressQtyFromMap(map, transaction.getContractAddress());
                    AddressQty from = getAddressQtyFromMap(map, transaction.getFrom());
                    AddressQty to = getAddressQtyFromMap(map, transaction.getTo());
                    Set<Integer> txType = Stream.of(Transaction.TypeEnum.EVM_CONTRACT_CREATE.getCode(),
                                                    Transaction.TypeEnum.WASM_CONTRACT_CREATE.getCode(),
                                                    Transaction.TypeEnum.ERC20_CONTRACT_CREATE.getCode(),
                                                    Transaction.TypeEnum.ERC721_CONTRACT_CREATE.getCode()).collect(Collectors.toSet());
                    if (!txType.contains(transaction.getType())) {
                        from.setTxQty(from.getTxQty() + 1);
                        to.setTxQty(to.getTxQty() + 1);
                    }
                    switch (transaction.getTypeEnum()) {
                        case TRANSFER:
                            from.setTransferQty(from.getTransferQty() + 1);
                            to.setTransferQty(to.getTransferQty() + 1);
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
                            from.setTxQty(from.getTxQty() + 1);
                            if (ObjectUtil.isNotNull(contract)) {
                                contract.setTxQty(contract.getTxQty() + 1);
                            }
                            break;
                        case ERC721_CONTRACT_EXEC:
                            break;
                        case CONTRACT_EXEC_DESTROY:
                            break;
                        case STAKE_CREATE:
                            break;
                        case STAKE_MODIFY:
                            break;
                        case STAKE_INCREASE:
                            break;
                        case STAKE_EXIT:
                            break;
                        case DELEGATE_CREATE:
                            break;
                        case DELEGATE_EXIT:
                            break;
                        case PROPOSAL_TEXT:
                            break;
                        case PROPOSAL_UPGRADE:
                            break;
                        case PROPOSAL_PARAMETER:
                            break;
                        case PROPOSAL_VOTE:
                            break;
                        case VERSION_DECLARE:
                            from.setProposalQty(from.getProposalQty() + 1);
                            to.setProposalQty(to.getProposalQty() + 1);
                            break;
                        case PROPOSAL_CANCEL:
                            break;
                        case REPORT:
                            from.setStakingQty(from.getStakingQty() + 1);
                            to.setStakingQty(to.getStakingQty() + 1);
                            break;
                        case RESTRICTING_CREATE:
                            break;
                        case CLAIM_REWARDS:
                            from.setDelegateQty(from.getDelegateQty() + 1);
                            to.setDelegateQty(to.getDelegateQty() + 1);
                            break;
                        default:
                            break;
                    }
                });
                customAddressMapper.batchUpdateAddressQty(CollUtil.newArrayList(map.values()));
                pointLog.setPosition(CollUtil.getLast(transactionList).getId().toString());
                pointLogMapper.updateByPrimaryKeySelective(pointLog);
            }
        } catch (Exception e) {
            log.error("更新地址交易数异常", e);
            throw e;
        }
    }

    private AddressQty getAddressQtyFromMap(Map<String, AddressQty> map, String address) {
        if (StrUtil.isBlank(address)) {
            return null;
        }
        if (map.containsKey(address)) {
            return map.get(address);
        } else {
            AddressQty addressQty = AddressQty.builder().address(address).txQty(0).tokenQty(0).transferQty(0).delegateQty(0).stakingQty(0).proposalQty(0).build();
            map.put(address, addressQty);
            return addressQty;
        }
    }

    /**
     * 获取交易列表
     *
     * @param maxId:
     * @param pageSize:
     * @return: java.util.List<com.platon.browser.elasticsearch.dto.Transaction>
     * @date: 2021/12/6
     */
    private List<Transaction> getTransactionList(long maxId, int pageSize) throws IOException {
        try {
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setAsc("id");
            constructor.setResult(new String[]{"seq", "id", "num", "hash", "type", "from", "to", "contractAddress"});
            ESQueryBuilders esQueryBuilders = new ESQueryBuilders();
            esQueryBuilders.listBuilders().add(QueryBuilders.rangeQuery("id").gt(maxId));
            constructor.must(esQueryBuilders);
            constructor.setUnmappedType("long");
            ESResult<Transaction> queryResultFromES = esTransactionRepository.search(constructor, Transaction.class, 1, pageSize);
            return queryResultFromES.getRsData();
        } catch (Exception e) {
            log.error("获取交易列表异常", e);
            throw e;
        }
    }

}