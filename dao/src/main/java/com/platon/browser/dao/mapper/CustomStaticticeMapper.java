package com.platon.browser.dao.mapper;

import com.platon.browser.common.dto.agent.StaticticsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
* 
* User: dongqile
* Date: 2018/12/5
* Time: 21:10
*/
public interface CustomStaticticeMapper {
    /*
   *  合约、账户统计
   */
    List<StaticticsDto> selectNodeInfo( @Param("chainId") String chainId);
}