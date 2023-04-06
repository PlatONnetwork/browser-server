package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.param.BusinessParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    void slashNode(Slash param);

    /**
     * 新选举周期更新节点提取质押需要经过的周期数
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void updateUnStakeFreezeDuration(BusinessParam param);

    /**
     * 把节点标记为双签异常
     *
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void slashByDoubleSigned(@Param("nodeId") String nodeId, @Param("stakingBlockNum") long blockNum);

    /**
     * 从staking表中过滤
     * 标记为双签异常的节点id
     * @param tobeSlashedNodeIdList, 将被处罚的节点（这些节点被处罚的原因，不一定都是因为双签，所以需要过滤下）
     * @return
     */
    Set<String> filterNodeIdThoseDoubleSigned(List<String> tobeSlashedNodeIdList);

}
