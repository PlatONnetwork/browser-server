package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.task.bean.AddressStatistics;
import com.platon.browser.task.bean.NetworkStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StatisticBusinessMapper {

    /**
     * 地址数据变更
     *
     * @param list:
     * @return: void
     * @date: 2021/12/6
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void addressChange(List<Address> list);

    /**
     * 统计数据变更
     *
     * @param param
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void networkChange(NetworkStat param);

    /**
     * 获得网络
     *
     * @return
     */
    NetworkStatistics getNetworkStatisticsFromNode();

    /**
     * 获得地址数
     *
     * @return
     */
    Integer getNetworkStatisticsFromAddress();

    /**
     * 获得投票中的提案
     *
     * @return
     */
    Integer getNetworkStatisticsFromProposal();

    /**
     * 获取提案总数
     */
    Integer getProposalQty();

    List<AddressStatistics> getAddressStatisticsFromStaking(@Param("list") List<String> list);

    List<AddressStatistics> getAddressStatisticsFromDelegation(@Param("list") List<String> list);

    @Transactional(rollbackFor = {Exception.class, Error.class})
    int batchUpdateFromTask(@Param("list") List<Address> list);


}