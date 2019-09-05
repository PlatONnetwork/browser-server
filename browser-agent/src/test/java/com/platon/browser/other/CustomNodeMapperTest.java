package com.platon.browser.other;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.CustomStaking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 14:18
 * @Description:
 */
@RunWith(SpringRunner.class)
public class CustomNodeMapperTest extends TestBase {

    protected static Logger logger = LoggerFactory.getLogger(CustomNodeMapperTest.class);

    @Autowired
    private CustomNodeMapper customNodeMapper;
    @Autowired
    private CustomStakingMapper customStakingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Test
    public void selectValidators(){
        //List<CustomNode> nodeBeans = customNodeMapper.selectVerifiers();

        List<String> nodeIds = new ArrayList<>();
        //nodeBeans.forEach(nodeBean -> nodeIds.add(nodeBean.getNodeId()));

        List<CustomStaking> stakingBeans = customStakingMapper.selectByNodeIdList(nodeIds);

        //logger.debug("{}",nodeBeans);
    }

    @Test
    public void insertSelective(){
        List<Node> nodes = nodeMapper.selectByExample(null);
        nodes.forEach(node -> node.setStatBlockQty(600l));
        int count = customNodeMapper.batchInsertOrUpdateSelective(new HashSet<>(nodes),Node.Column.values());
        logger.error("update count:{}",count);
    }

}
