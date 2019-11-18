package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnConsensusConverterTest extends AgentTestBase {
	
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private EpochBusinessMapper epochBusinessMapper;
	
    @Spy
    private OnConsensusConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"epochBusinessMapper",epochBusinessMapper);
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(blockChainConfig.getConsensusPeriodBlockCount());
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setCurValidatorList(validatorList);
        CollectionEvent collectionEvent = CollectionEvent.builder()
                .block(block)
                .epochMessage(epochMessage)
                .build();
        target.convert(collectionEvent,block);
    }
}
