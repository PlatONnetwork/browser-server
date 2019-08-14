package com.platon.browser.dao.mapper;

import com.platon.browser.dto.NodeBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomNodeMapper {

    List<NodeBean> selectAll();
    List<NodeBean> selectVerifiers();
    List<NodeBean> selectValidators();

}
