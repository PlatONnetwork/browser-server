//package com.platon.browser.task;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.config.BlockChainConfig;
//import com.platon.browser.old.task.NetworkStatUpdateTask;
//import com.platon.browser.old.task.cache.NetworkStatTaskCache;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/9/9 20:29
// * @Description:
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class NetworkStatUpdateTaskTest extends TestBase {
//    private static Logger logger = LoggerFactory.getLogger(NetworkStatUpdateTaskTest.class);
//    @Spy
//    private NetworkStatUpdateTask target;
//    @Mock
//    private BlockChain bc;
//    @Mock
//    private BlockChainConfig chainConfig;
//    @Autowired
//    private NetworkStatTaskCache taskCache;
//
//    @Before
//    public void setup(){
//        ReflectionTestUtils.setField(target, "bc", bc);
//        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
//        ReflectionTestUtils.setField(target, "taskCache", taskCache);
//    }
//
//    @Test
//    public void testStart() throws  Exception{
//        when(bc.getCurBlock()).thenReturn(blocks.get(0));
//        Map<Integer, BigDecimal> foundationSubsidiesMap = new HashMap<>();
//        foundationSubsidiesMap.put(1,BigDecimal.ONE);
//        when(chainConfig.getFoundationSubsidies()).thenReturn(foundationSubsidiesMap);
//        when(bc.getAddIssueEpoch()).thenReturn(BigInteger.ONE);
//        when(bc.getChainConfig()).thenReturn(new BlockChainConfig());
//        target.start();
//        verify(target, times(1)).start();
//    }
//}
