package com.platon.browser.task;//package com.platon.browser.task;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.config.BlockChainConfig;
//import com.platon.browser.dto.CustomStaking;
//import com.platon.browser.engine.cache.CacheHolder;
//import com.platon.browser.engine.cache.NodeCache;
//import com.platon.browser.engine.stage.BlockChainStage;
//import com.platon.browser.task.cache.StakingTaskCache;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/9/9 20:29
// * @Description:
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class StakingUpdateTaskTest extends TestBase {
//    @Spy
//    private StakingUpdateTask target;
//    @Mock
//    private BlockChainConfig chainConfig;
//    @Mock
//    private CacheHolder cacheHolder;
//    @Mock
//    private StakingTaskCache taskCache;
//
//
//    @Before
//    public void setup(){
//        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
//        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
//        ReflectionTestUtils.setField(target, "taskCache", taskCache);
//    }
//
//    @Test
//    public void testStart() throws  Exception{
//        BlockChainStage stageData = new BlockChainStage();
//        when(cacheHolder.getStageData()).thenReturn(stageData);
//        when(cacheHolder.getNodeCache()).thenReturn(mock(NodeCache.class));
//        when(chainConfig.getKeyBase()).thenReturn("https://keybase.io/");
//        when(chainConfig.getKeyBaseApi()).thenReturn("_/api/1.0/user/autocomplete.json?q=");
//        Set<CustomStaking> customStakingSet = new HashSet<>(stakings);
//        customStakingSet.forEach(cc->{
//            cc.setExternalName("");
//            cc.setStakingIcon("");
//            cc.setExternalId("2A0CD8DCB4BF2BE0");
//        });
//        doReturn(customStakingSet).when(target).getAllStaking();
//        target.start();
//        verify(target, times(1)).start();
//    }
//}
