package com.platon.browser.dao.custommapper;

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
     * @date 2021/4/1
     */
    List<Node> batchFindNodeNameByNodeId(@Param("nodeIds") Set<String> nodeIds);

    Page<Node> findAliveStakingList(Integer status1, Integer isSettle1, boolean isUnion, Integer status2, Integer isSettle2);

    /**
     * 批量更新节点区块数统计信息
     *
     * @param nodeList
     * @return int
     * @date 2021/6/2
     */
    int updateNodeSettleStatis(@Param("list") List<Node> nodeList);

}