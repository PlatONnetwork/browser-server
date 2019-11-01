package com.platon.browser.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.persistence.dao.mapper.CreateStakingMapper;
import com.platon.browser.persistence.dao.mapper.ModifyStakingMapper;
import com.platon.browser.persistence.dao.param.CreateStakingParam;
import com.platon.browser.persistence.dao.param.ModifyStakingParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class StakingMapperTest extends TestBase {

    @Autowired
    private CreateStakingMapper createStakingMapper;

    @Autowired
    private ModifyStakingMapper modifyStakingMapper;


    @Autowired
    private NodeOptMapper nodeOptMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Test
    public void createStakingMapper(){

        List<Node> nodes = nodeMapper.selectByExample(null);

        CreateStakingParam createStakingParam = stakingParam();
        //删除数据
        deleteCreateStaking(createStakingParam);
        //数据插入
        createStakingMapper.createStaking(createStakingParam);
        //staking数据插入验证
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(createStakingParam.getNodeId());
        stakingKey.setStakingBlockNum(createStakingParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        assertEquals(createStakingParam.getNodeId(), staking.getNodeId());
        //node数据插入验证
        Node node = nodeMapper.selectByPrimaryKey(createStakingParam.getNodeId());
        assertEquals(createStakingParam.getStakingBlockNum(),node.getStakingBlockNum());
        //opt数据插入验证
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(createStakingParam.getNodeId())
                .andBNumEqualTo(createStakingParam.getStakingBlockNum().longValue());
        List <NodeOpt> nodeOptList = nodeOptMapper.selectByExample(nodeOptExample);
        assertEquals(nodeOptList.get(0).getNodeId(),createStakingParam.getNodeId());
    }

    public void deleteCreateStaking(CreateStakingParam param){
        //删除staking数据
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(param.getNodeId());
        stakingKey.setStakingBlockNum(param.getStakingBlockNum().longValue());
        stakingMapper.deleteByPrimaryKey(stakingKey);
        //删除node数据
        nodeMapper.deleteByPrimaryKey(param.getNodeId());
        //删除opt数据
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(param.getNodeId()).andBNumEqualTo(param.getStakingBlockNum().longValue());
        nodeOptMapper.deleteByExample(nodeOptExample);
    }

    @Test
    public void modifyStakingMapper(){
        ModifyStakingParam modifyStakingParam = new ModifyStakingParam();
        modifyStakingParam.setNodeName("testNode02");
        modifyStakingParam.setExternalId("externalId02");
        modifyStakingParam.setBenefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61");
        modifyStakingParam.setWebSite("web_site01");
        modifyStakingParam.setDetails("details01");
        modifyStakingParam.setIsInit(2);
        modifyStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        modifyStakingParam.setStakingBlockNum(new BigInteger("200"));
        modifyStakingParam.setBNum(new BigInteger("300"));
        modifyStakingParam.setTime(new Date(System.currentTimeMillis()));
        modifyStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        modifyStakingMapper.modifyStaking(modifyStakingParam);
    }


}