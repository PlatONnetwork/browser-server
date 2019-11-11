package com.platon.browser.complement.dao.mapper;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.Node;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface StakeBusinessMapper {
    /**
     * 发起质押
     * @param param
     */
    @Transactional
    void create(BusinessParam param);
    /**
     * 增持质押
     */
    @Transactional
    void increase(BusinessParam param);
    /**
     * 修改质押信息
     */
    @Transactional
    void modify(BusinessParam param );

    /**
     * 退出质押
     */
    @Transactional
    void exit(BusinessParam param);
    
    /**
     * 查询质押总金额
     */
    @Transactional
    BigDecimal queryStakingValue(BusinessParam param);
    
	int updateNodeForTask(@Param("list") List<Node> updateNodeList);
}