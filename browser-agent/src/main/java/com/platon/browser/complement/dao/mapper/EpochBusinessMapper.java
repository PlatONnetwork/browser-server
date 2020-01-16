package com.platon.browser.complement.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.Staking;

/*
 * @Auther: dongqile
 * @Date:  2019/11/5
 * @Description:
 */
public interface EpochBusinessMapper {
    /**
     * 新结算周期
     */
    @Transactional
    void settle ( BusinessParam param);

    /**
     * 新选举周期数据变更（结算&共识周期往前推20个块）
     */
    void slashNode (BusinessParam param);


    /**
     * 新共识周期数据变更
     */
    @Transactional
    void consensus(BusinessParam param);
    
    /**
     * 查询待惩罚的节点列表
     * @param preValidatorList
     * @return
     */
	List<Staking> querySlashNode(@Param("list") List<String> preValidatorList);
}