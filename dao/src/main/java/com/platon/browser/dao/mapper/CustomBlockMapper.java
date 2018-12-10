package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockPage;
import org.apache.ibatis.annotations.Param;

import javax.annotation.PostConstruct;
import java.util.List;

public interface CustomBlockMapper {
    List<Block> selectByPage(@Param("page") BlockPage page);

    List<Block> selectBlockByNumber( @Param("chainId")String chainId, @Param("start")long start,@Param("end")long end);
}