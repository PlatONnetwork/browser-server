package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.param.BusinessParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EpochBusinessMapper {

    /**
     * 新结算周期
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void settle(BusinessParam param);

    /**
     * 查找需要更新质押奖励的节点
     *
     * @param preVerifierSet:
     * @return: java.util.List<com.platon.browser.dao.entity.Staking>
     * @date: 2022/6/16
     */
    List<Staking> findStaking(@Param("list") List<String> preVerifierSet);

    /**
     * 新结算周期--更新质押奖励
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void settleForStakingValue(@Param("list") List<Staking> updateStakingList);

    /**
     * 更新Gas估算周期数
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateGasEstimate(@Param("list") List<GasEstimate> estimateList);

    /**
     * 新选举周期数据变更（结算&共识周期往前推20个块）
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void slashNode(BusinessParam param);

    /**
     * 新共识周期数据变更
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void consensus(BusinessParam param);

    /**
     * 查询待惩罚的节点列表
     *
     * @param preValidatorList
     * @return
     */
    List<Staking> querySlashNode(@Param("list") List<String> preValidatorList);

    /**
     * 把节点标记为异常
     *
     * @return
     */
    void setException(@Param("list") List<String> nodeIdList);

    /**
     * 查询标记为异常的节点
     *
     * @return
     */
    List<Staking> getException(@Param("list") List<String> nodeIdList);

}