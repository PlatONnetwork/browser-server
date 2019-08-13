package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Staking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomStakingMapper {
    List<Staking> selectVerifiers();
    List<Staking> selectValidators();
}
