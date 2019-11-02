package com.platon.browser.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.persistence.dao.mapper.*;
import com.platon.browser.persistence.dao.param.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    private AddStakingMapper addStakingMapper;

    @Autowired
    private WithdrewStakingMapper withdrewStakingMapper;

    @Autowired
    private ReportDuplicateSignMapper reportDuplicateSignMapper;

    @Autowired
    private NodeOptMapper nodeOptMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private DelegationMapper delegationMapper;

    @Autowired
    private SlashMapper slashMapper;

    @Test
    public void createStakingMapper(){
        CreateStakingParam createStakingParam = stakingParam();
        //删除数据
        deleteCreateStaking(createStakingParam);
        //数据插入
        createStakingMapper.createStaking(createStakingParam);
        //staking数据插入验证
        Staking staking = getStaking(createStakingParam.getNodeId(),createStakingParam.getStakingBlockNum().longValue());
        assertEquals(createStakingParam.getNodeId(), staking.getNodeId());
        //node数据插入验证
        Node node = nodeMapper.selectByPrimaryKey(createStakingParam.getNodeId());
        assertEquals(createStakingParam.getStakingBlockNum(),new BigInteger(node.getStakingBlockNum().toString()));
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
        ModifyStakingParam modifyStakingParam = modifyStakingParam();
        modifyStakingMapper.modifyStaking(modifyStakingParam);
        //staking数据更新验证
        Staking staking = getStaking(modifyStakingParam.getNodeId(),modifyStakingParam.getStakingBlockNum().longValue());
        assertEquals(modifyStakingParam.getNodeName(), staking.getNodeName());
        assertEquals(modifyStakingParam.getExternalId(),staking.getExternalId());
        assertEquals(modifyStakingParam.getBenefitAddr(),staking.getBenefitAddr());
        assertEquals(modifyStakingParam.getWebSite(),staking.getWebSite());
        assertEquals(modifyStakingParam.getDetails(),staking.getDetails());
        assertEquals(modifyStakingParam.getIsInit(),staking.getIsInit().intValue());
        //node数据更新验证
        Node node = nodeMapper.selectByPrimaryKey(modifyStakingParam.getNodeId());
        assertEquals(modifyStakingParam.getNodeName(),node.getNodeName());
        assertEquals(modifyStakingParam.getExternalId(),node.getExternalId());
        assertEquals(modifyStakingParam.getBenefitAddr(),node.getBenefitAddr());
        assertEquals(modifyStakingParam.getDetails(),node.getDetails());
        assertEquals(modifyStakingParam.getWebSite(),node.getWebSite());
        assertEquals(modifyStakingParam.getIsInit(),node.getIsInit().intValue());
        //opt插入数据验证
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(modifyStakingParam.getNodeId())
                .andBNumEqualTo(modifyStakingParam.getStakingBlockNum().longValue());
        List <NodeOpt> nodeOptList = nodeOptMapper.selectByExample(nodeOptExample);
        assertEquals(modifyStakingParam.getNodeId(),nodeOptList.get(0).getNodeId());
    }

    @Test
    public void addStakingMapper(){
        AddStakingParam addStakingParam = addStakingParam();
        Staking stakingAfter = getStaking(addStakingParam.getNodeId(),addStakingParam.getStakingBlockNum().longValue());
        Node nodeAfter = nodeMapper.selectByPrimaryKey(addStakingParam.getNodeId());

        addStakingMapper.addStaking(addStakingParam);
        //staking数据更新验证
        Staking staking = getStaking(addStakingParam.getNodeId(),addStakingParam.getStakingBlockNum().longValue());
        assertEquals(stakingAfter.getStakingHes(),staking.getStakingHes().subtract(addStakingParam.getAmount()));
        //node数据更新验证

        Node nodeBefore = nodeMapper.selectByPrimaryKey(addStakingParam.getNodeId());
        assertEquals(nodeAfter.getTotalValue().longValue(),nodeBefore.getTotalValue().subtract(addStakingParam.getAmount()).longValue());
        assertEquals(nodeAfter.getStakingHes().longValue(),nodeBefore.getStakingHes().subtract(addStakingParam.getAmount()).longValue());

        //opt插入数据验证
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(addStakingParam.getNodeId())
                .andBNumEqualTo(addStakingParam.getStakingBlockNum().longValue());
        List <NodeOpt> nodeOptList = nodeOptMapper.selectByExample(nodeOptExample);
        assertEquals(addStakingParam.getNodeId(),nodeOptList.get(0).getNodeId());
    }

    @Test
    public void withdrewStakingMapper(){
        WithdrewStakingParam withdrewStakingParam = withdrewStakingParam();
        withdrewStakingMapper.withdrewStaking(withdrewStakingParam);
        //delegation数据更新验证
        DelegationKey delegationKey = new Delegation();
        delegationKey.setNodeId(withdrewStakingParam.getNodeId());
        delegationKey.setStakingBlockNum(withdrewStakingParam.getStakingBlockNum().longValue());
        Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);
        assertEquals(delegation.getDelegateHes().longValue(),0);
        assertEquals(delegation.getDelegateLocked().longValue(),0);
        assertEquals(delegation.getDelegateReleased().longValue(),0);
        //node数据更新验证
        Node node = nodeMapper.selectByPrimaryKey(withdrewStakingParam.getNodeId());
        assertEquals(withdrewStakingParam.getStakingReductionEpoch(),node.getStakingReductionEpoch().intValue());
        //staking数据验证
        Staking staking = getStaking(withdrewStakingParam.getNodeId(),withdrewStakingParam.getStakingBlockNum().longValue());
        assertEquals(withdrewStakingParam.getStakingReductionEpoch(),staking.getStakingReductionEpoch().intValue());
        //opt数据验证
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(withdrewStakingParam.getNodeId())
                .andBNumEqualTo(withdrewStakingParam.getStakingBlockNum().longValue());
        List <NodeOpt> nodeOptList = nodeOptMapper.selectByExample(nodeOptExample);
        assertEquals(withdrewStakingParam.getNodeId(),nodeOptList.get(0).getNodeId());
    }

    @Test
    public void reportDuplicateSignMapper(){
        ReportDuplicateSignParam reportDuplicateSignParam = reportDuplicateSignParam();
        reportDuplicateSignMapper.reportDuplicateSign(reportDuplicateSignParam);
        //node更新数据验证
        Node node = nodeMapper.selectByPrimaryKey(reportDuplicateSignParam.getNodeId());
        assertEquals(node.getStatus().intValue(),reportDuplicateSignParam.getCodeStatus());
        assertEquals(node.getStakingReductionEpoch().intValue(),reportDuplicateSignParam.getSettingEpoch());
        assertEquals(node.getStakingReduction(),reportDuplicateSignParam.getCodeCurStakingLocked());
        //staking更新数据验证
        Staking staking = getStaking(reportDuplicateSignParam.getNodeId(),reportDuplicateSignParam.getStakingBlockNum().longValue());
        assertEquals(staking.getStatus().intValue(),reportDuplicateSignParam.getCodeStatus());
        assertEquals(staking.getStakingReductionEpoch().intValue(),reportDuplicateSignParam.getSettingEpoch());
        assertEquals(staking.getStakingReduction(),reportDuplicateSignParam.getCodeCurStakingLocked());
        //slash插入数据验证
        Slash slash = slashMapper.selectByPrimaryKey(reportDuplicateSignParam.getTxHash());
        assertTrue(!StringUtils.isEmpty(slash));
        //opt插入验证
        NodeOptExample nodeOptExample = new NodeOptExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(reportDuplicateSignParam.getNodeId())
                .andBNumEqualTo(reportDuplicateSignParam.getStakingBlockNum().longValue());
        List <NodeOpt> nodeOptList = nodeOptMapper.selectByExample(nodeOptExample);
        assertEquals(reportDuplicateSignParam.getNodeId(),nodeOptList.get(0).getNodeId());
    }

    public Staking getStaking(String nodeId, long stakingBlockNumer){
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(nodeId);
        stakingKey.setStakingBlockNum(stakingBlockNumer);
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        return staking;
    }
}