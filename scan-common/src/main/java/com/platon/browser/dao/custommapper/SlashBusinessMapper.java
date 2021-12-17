package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.param.BusinessParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/11/1
 * @Description:
 */
public interface SlashBusinessMapper {

    /**
     * 双签举报
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void slashNode(BusinessParam param);
    /**
     * 新选举周期更新节点提取质押需要经过的周期数
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateUnStakeFreezeDuration (BusinessParam param);

    /**
     * 把节点标记为双签异常
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void setException(@Param("nodeId") String nodeId,@Param("stakingBlockNum") long blockNum);

    /**
     * 查询标记为双签异常的节点
     * @return
     */
    List<Staking> getException(@Param("list") List<String> list);
}