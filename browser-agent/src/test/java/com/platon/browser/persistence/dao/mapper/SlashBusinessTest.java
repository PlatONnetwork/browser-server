package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.AgentApplication;
import com.platon.browser.TestBase;
import com.platon.browser.complement.dao.mapper.SlashBusinessMapper;
import com.platon.browser.complement.dao.param.slash.Report;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 举报相关入库测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgentApplication.class, value = "spring.profiles.active=test")
@SpringBootApplication
public class SlashBusinessTest extends TestBase {


    @Autowired
    private SlashBusinessMapper slashBusinessMapper;

    @Autowired
    private NOptBakMapper nOptBakMapper;

    @Autowired
    private StakingMapper stakingMapper;

    @Autowired
    private NodeMapper nodeMapper;


    @Autowired
    private SlashMapper slashMapper;


    @Test
    public void reportDuplicateSignMapper () {
        Report reportDuplicateSignParam = reportDuplicateSignParam();
        slashBusinessMapper.report(reportDuplicateSignParam);
        //node更新数据验证
        Node node = nodeMapper.selectByPrimaryKey(reportDuplicateSignParam.getNodeId());
        assertEquals(node.getStatus().intValue(), reportDuplicateSignParam.getCodeStatus());
        assertEquals(node.getStakingReductionEpoch().intValue(), reportDuplicateSignParam.getSettingEpoch());
        assertEquals(node.getStakingReduction(), reportDuplicateSignParam.getCodeCurStakingLocked());
        //staking更新数据验证
        Staking staking = getStaking(reportDuplicateSignParam.getNodeId(), reportDuplicateSignParam.getStakingBlockNum().longValue());
        assertEquals(staking.getStatus().intValue(), reportDuplicateSignParam.getCodeStatus());
        assertEquals(staking.getStakingReductionEpoch().intValue(), reportDuplicateSignParam.getSettingEpoch());
        assertEquals(staking.getStakingReduction(), reportDuplicateSignParam.getCodeCurStakingLocked());
        //slash插入数据验证
        Slash slash = slashMapper.selectByPrimaryKey(reportDuplicateSignParam.getTxHash());
        assertTrue(!StringUtils.isEmpty(slash));
        //opt插入验证
        NOptBakExample nodeOptExample = new NOptBakExample();
        nodeOptExample.createCriteria().andNodeIdEqualTo(reportDuplicateSignParam.getNodeId())
                .andBNumEqualTo(reportDuplicateSignParam.getStakingBlockNum().longValue());
        List <NOptBak> nodeOptList = nOptBakMapper.selectByExample(nodeOptExample);
        assertEquals(reportDuplicateSignParam.getNodeId(), nodeOptList.get(0).getNodeId());
    }


    public Staking getStaking ( String nodeId, long stakingBlockNumer ) {
        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(nodeId);
        stakingKey.setStakingBlockNum(stakingBlockNumer);
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        return staking;
    }


}