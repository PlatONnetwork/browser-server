package com.platon.browser.complement.dao.mapper;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.Staking;
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
    @Transactional
    void slashNode(BusinessParam param);

    /**
     * 把节点标记为双签异常
     * @return
     */
    void setException(@Param("list") List<String> nodeIdList);

    /**
     * 查询标记为双签异常的节点
     * @return
     */
    List<Staking> getException(@Param("list") List<String> list);
}