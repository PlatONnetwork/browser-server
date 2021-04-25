package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface CustomNodeMapper {

    /**
     * 根据nodeId查询nodeName
     *
     * @param nodeId
     * @return
     * @method findNameById
     */
    String findNameById(String nodeId);

    /**
     * 根据条件查询列表
     *
     * @param example
     * @return
     * @method selectByExample
     */
    Page<Node> selectListByExample(NodeExample example);


    int selectCountByActive();

    /**
     * 根据nodeIds查询nodeName
     *
     * @param nodeIds
     * @return java.util.List<java.lang.String>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/1
     */
    List<Node> batchFindNodeNameByNodeId(@Param("nodeIds") Set<String> nodeIds);

    Page<Node> findAliveStakingList(Integer status1, Integer isSettle1, boolean isUnion, Integer status2, Integer isSettle2);

}