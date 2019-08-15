package com.platon.browser;

import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dto.NodeBean;
import com.platon.browser.dto.StakingBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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

    @Test
    public void selectValidators(){
        //List<NodeBean> nodeBeans = customNodeMapper.selectVerifiers();

        List<String> nodeIds = new ArrayList<>();
        //nodeBeans.forEach(nodeBean -> nodeIds.add(nodeBean.getNodeId()));

        List<StakingBean> stakingBeans = customStakingMapper.selectByNodeIdList(nodeIds);

        //logger.debug("{}",nodeBeans);
    }

}
