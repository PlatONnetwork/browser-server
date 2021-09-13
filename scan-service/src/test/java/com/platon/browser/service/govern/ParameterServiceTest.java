package com.platon.browser.service.govern;

import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dao.custommapper.CustomConfigMapper;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.ParamItem;
import com.platon.contracts.ppos.dto.resp.ParamValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.platon.protocol.core.RemoteCall;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @Auther: dongqile
 * @Date: 2019/12/3
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ParameterServiceTest {

    @Mock
    private ConfigMapper configMapper;

    @Mock
    private PlatOnClient platOnClient;

    @Mock
    private BlockChainConfig chainConfig;

    @Mock
    private CustomConfigMapper customConfigMapper;

    @Mock
    private ProposalContract proposalContract;

    @Spy
    private ParameterService target;

    @Before
    public void setup()throws Exception{
        String v = "[\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"minimum amount of stake, range：[1000000000000000000000000, 10000000000000000000000000) \",\n" +
                "      \"Module\":\"staking\",\n" +
                "      \"Name\":\"stakeThreshold\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"29601\",\n" +
                "      \"StaleValue\":\"1000000000000000000000000\",\n" +
                "      \"Value\":\"1500000000000000000000000\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"minimum amount of stake increasing funds, delegation funds, or delegation withdrawing funds, range：[10000000000000000000, 10000000000000000000000) \",\n" +
                "      \"Module\":\"staking\",\n" +
                "      \"Name\":\"operatingThreshold\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"10000000000000000000\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"maximum amount of validator, range：[4, 201]\",\n" +
                "      \"Module\":\"staking\",\n" +
                "      \"Name\":\"maxValidators\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"30\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"quantity of epoch for skake withdrawal, range：(MaxEvidenceAge, 112]\",\n" +
                "      \"Module\":\"staking\",\n" +
                "      \"Name\":\"unStakeFreezeDuration\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"5\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"quantity of base point(1BP=1?). Node's stake will be deducted(BPs*staking amount*1?) it the node sign block duplicatlly, range：(0, 10000]\",\n" +
                "      \"Module\":\"slashing\",\n" +
                "      \"Name\":\"slashFractionDuplicateSign\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"100\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"quantity of base point(1bp=1%). Bonus(BPs*deduction amount for sign block duplicatlly*%) to the node who reported another's duplicated-signature, range：(0, 80]\",\n" +
                "      \"Module\":\"slashing\",\n" +
                "      \"Name\":\"duplicateSignReportReward\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"50\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"quantity of epoch. During these epochs after a node duplicated-sign, others can report it, range：(0, UnStakeFreezeDuration)\",\n" +
                "      \"Module\":\"slashing\",\n" +
                "      \"Name\":\"maxEvidenceAge\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"1\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"quantity of block, the total bonus amount for these blocks will be deducted from a inefficient node's stake, range：[0, 60101)\",\n" +
                "      \"Module\":\"slashing\",\n" +
                "      \"Name\":\"slashBlocksReward\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"20\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"ParamItem\":{\n" +
                "      \"Desc\":\"maximum gas limit per block, range：[4712388, 210000000]\",\n" +
                "      \"Module\":\"block\",\n" +
                "      \"Name\":\"maxBlockGasLimit\"\n" +
                "    },\n" +
                "    \"ParamValue\":{\n" +
                "      \"ActiveBlock\":\"0\",\n" +
                "      \"StaleValue\":\"\",\n" +
                "      \"Value\":\"100800000\"\n" +
                "    }\n" +
                "  }\n" +
                "]";

        List<GovernParam> list = JSONArray.parseArray(v,GovernParam.class);
        ReflectionTestUtils.setField(target,"configMapper",configMapper);
        ReflectionTestUtils.setField(target,"platOnClient",platOnClient);
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"customConfigMapper",customConfigMapper);
        when(platOnClient.getProposalContract()).thenReturn(proposalContract);
        RemoteCall <CallResponse<List<GovernParam>>> baseResponseRemoteCall = mock(RemoteCall.class);
        when(proposalContract.getParamList("")).thenReturn(baseResponseRemoteCall);
        CallResponse <List<GovernParam>> baseResponse = mock(CallResponse.class);
        when(baseResponseRemoteCall.send()).thenReturn(baseResponse);
        baseResponse.setData(list);

        when(chainConfig.getStakeThreshold()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getDelegateThreshold()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getSettlementValidatorCount()).thenReturn(BigInteger.TEN);
        when(chainConfig.getUnStakeRefundSettlePeriodCount()).thenReturn(BigInteger.TEN);
        when(chainConfig.getDuplicateSignSlashRate()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getDuplicateSignRewardRate()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getEvidenceValidEpoch()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getSlashBlockRewardCount()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getMaxBlockGasLimit()).thenReturn(BigDecimal.TEN);


        List <Config> configList = new ArrayList <>();
        Config config = new Config();
        config.setInitValue("100000000");
        config.setActiveBlock(1L);
        config.setId(1);
        config.setName("stakeThreshold");
        config.setModule("testModule");
        config.setRangeDesc("testDec");
        config.setStaleValue("10000000");
        config.setValue("10000000");
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        configList.add(config);

        when(configMapper.selectByExample(null)).thenReturn(configList);

        ProposalContract proposalContract = mock(ProposalContract.class);
        when(platOnClient.getProposalContract()).thenReturn(proposalContract);
        RemoteCall remoteCall = mock(RemoteCall.class);
        when(proposalContract.getParamList(any())).thenReturn(remoteCall);
        CallResponse response = mock(CallResponse.class);
        when(remoteCall.send()).thenReturn(response);

        List<GovernParam> governParamList = new ArrayList<>();
        GovernParam gp = new GovernParam();
        ParamItem pi = new ParamItem();
        pi.setName("maxValidators");
        pi.setModule("staking");
        pi.setDesc("staking");
        gp.setParamItem(pi);
        ParamValue pv = new ParamValue();
        pv.setActiveBlock("3333");
        pv.setStaleValue("5555");
        pv.setValue("3333");
        gp.setParamValue(pv);
        governParamList.add(gp);
        when(response.getData()).thenReturn(governParamList);
    }

    @Test
    public void initConfigTableTest()throws Exception{
        target.initConfigTable();
    }


    @Test
    public void overrideBlockChainConfigTest()throws Exception{
        target.overrideBlockChainConfig();
    }


    @Test
    public void rotateConfigTest()throws Exception{
        target.rotateConfig(new ArrayList <>());
    }


    @Test
    public void getValueInBlockChainConfigTestStake(){
        target.getValueInBlockChainConfig("stakeThreshold");
    }

    @Test
    public void getValueInBlockChainConfigTestOperating(){
        target.getValueInBlockChainConfig("operatingThreshold");
    }

    @Test
    public void getValueInBlockChainConfigTestMax(){
        target.getValueInBlockChainConfig("maxValidators");
    }


    @Test
    public void getValueInBlockChainConfigTestUnStake(){
        target.getValueInBlockChainConfig("unStakeFreezeDuration");
    }

    @Test
    public void getValueInBlockChainConfigTestSlash(){
        target.getValueInBlockChainConfig("slashFractionDuplicateSign");
    }

    @Test
    public void getValueInBlockChainConfigTestDup(){
        target.getValueInBlockChainConfig("duplicateSignReportReward");
    }

    @Test
    public void getValueInBlockChainConfigTestAge(){
        target.getValueInBlockChainConfig("maxEvidenceAge");
    }

    @Test
    public void getValueInBlockChainConfigTestSlashBlock(){
        target.getValueInBlockChainConfig("slashBlocksReward");
    }

    @Test
    public void getValueInBlockChainConfigTestMaxBloc(){
        target.getValueInBlockChainConfig("maxBlockGasLimit");
    }
}