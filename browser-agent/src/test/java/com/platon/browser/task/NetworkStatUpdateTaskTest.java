package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.entity.NetworkStatistics;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatUpdateTaskTest extends AgentTestBase {
    private static final Logger log = LoggerFactory.getLogger(NetworkStatUpdateTask.class);
    @Spy
    private AccountService accountService;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Spy
    private NetworkStatUpdateTask target;

    private BlockChainConfig chainConfig = new BlockChainConfig();
    private NetworkStatCache networkStatCache=new NetworkStatCache();
    private Map<Integer, BigDecimal> subsidies = new HashMap<>();

    @Before
    public void setup() throws Exception {
        subsidies.put(1,BigDecimal.valueOf(62215742));
        subsidies.put(2,BigDecimal.valueOf(55965742));
        subsidies.put(3,BigDecimal.valueOf(49559492));
        subsidies.put(4,BigDecimal.valueOf(42993086));
        subsidies.put(5,BigDecimal.valueOf(36262520));
        subsidies.put(6,BigDecimal.valueOf(29363689));
        subsidies.put(7,BigDecimal.valueOf(22292388));
        subsidies.put(8,BigDecimal.valueOf(15044304));
        subsidies.put(9,BigDecimal.valueOf(7615018));


        networkStatCache.setNetworkStat(new NetworkStat());
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);

        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));
        PlatOnClient platOnClient = new PlatOnClient();
        ReflectionTestUtils.setField(platOnClient, "currentValidWeb3j", web3j);

        ReflectionTestUtils.setField(chainConfig, "client", platOnClient);
        chainConfig.init();
        chainConfig.setAddIssueRate(BigDecimal.valueOf(0.025));
        chainConfig.setIncentiveRateFromIssue(BigDecimal.valueOf(0.8));
        chainConfig.setElectionBackwardBlockCount(BigInteger.valueOf(20));
        chainConfig.setDefaultStakingLockedAmount(BigDecimal.valueOf(1500000));

        chainConfig.setInitIssueAmount(BigDecimal.valueOf(10000000000L));
        chainConfig.setFoundationSubsidies(subsidies);

        ReflectionTestUtils.setField(accountService, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "accountService", accountService);
        ReflectionTestUtils.setField(target, "statisticBusinessMapper", statisticBusinessMapper);


        when(statisticBusinessMapper.getNetworkStatisticsFromNode()).thenReturn(NetworkStatistics.builder().build());
        when(statisticBusinessMapper.getNetworkStatisticsFromAddress()).thenReturn(0);
        when(statisticBusinessMapper.getNetworkStatisticsFromProposal()).thenReturn(0);
    }

    @Test
    public void test() throws BlockNumberException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        networkStatCache.getNetworkStat().setCurNumber(1880L);
        // 发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补贴
        // ******************手算******************
        //增发轮数
        BigInteger issueRound = EpochUtil.getEpoch(BigInteger.valueOf(networkStatCache.getNetworkStat().getCurNumber()),chainConfig.getAddIssuePeriodBlockCount());
        //激励池余额
        BigDecimal incentiveBalance = accountService.getInciteBalance(BigInteger.valueOf(networkStatCache.getNetworkStat().getCurNumber()));
        //基金会补充部分金额
        BigDecimal foundationAmount = subsidies.get(issueRound.intValue());
        foundationAmount=(foundationAmount==null)?BigDecimal.ZERO:Convert.toVon(foundationAmount, Convert.Unit.LAT);
        //指数值
        BigDecimal powValue = chainConfig.getAddIssueRate().add(BigDecimal.ONE).pow(issueRound.intValue());
        //初始发行金额
        BigDecimal initIssueAmount = Convert.toVon(chainConfig.getInitIssueAmount().toString(), Convert.Unit.LAT);
        //手算结果
        BigDecimal handCalc = initIssueAmount
                .multiply(powValue)
                .subtract(incentiveBalance)
                .add(foundationAmount);

        // ******************程序算******************
        target.cron();
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        BigDecimal compCalc = networkStat.getIssueValue();
        assertEquals(0, handCalc.compareTo(compCalc));

        log.debug("{}", JSON.toJSONString(networkStat,true));
    }
}
