package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.entity.NOptBak;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomNOptBakMapper {

    int batchInsertOrUpdateSelective(@Param("list") List<NodeOpt> list);

    /**
     * 获取节点操作记录最新序号
     *
     * @param :
     * @return: long
     * @date: 2021/12/2
     */
    long getLastNodeOptSeq();

}