package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.persistence.dao.param.CreateStakingParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

public interface CreateStakingMapper {
    /**
     * 发起质押
     * @param param
     */
    @Transactional
    void createStaking( CreateStakingParam param);
}