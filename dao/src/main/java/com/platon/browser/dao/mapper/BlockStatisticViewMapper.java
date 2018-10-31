package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.BlockStatisticView;
import com.platon.browser.dao.entity.BlockStatisticViewExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlockStatisticViewMapper {
    long countByExample(BlockStatisticViewExample example);

    int deleteByExample(BlockStatisticViewExample example);

    int insert(BlockStatisticView record);

    int insertSelective(BlockStatisticView record);

    List<BlockStatisticView> selectByExample(BlockStatisticViewExample example);

    int updateByExampleSelective(@Param("record") BlockStatisticView record, @Param("example") BlockStatisticViewExample example);

    int updateByExample(@Param("record") BlockStatisticView record, @Param("example") BlockStatisticViewExample example);
}