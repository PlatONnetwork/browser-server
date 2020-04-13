package com.platon.browser.bootstrap.queue.gasestimate.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.queue.gasestimate.event.GasEstimateEvent;
import com.platon.browser.common.queue.gasestimate.handler.GasEstimateEventHandler;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import com.platon.browser.exception.BeanCreateOrUpdateException;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GasEstimateEventHandlerTest extends AgentTestBase {

	@Mock
	private GasEstimateLogMapper gasEstimateLogMapper;
	@Mock
	private EpochBusinessMapper epochBusinessMapper;
	@Spy
	private GasEstimateEventHandler target;
	
	@Before
    public void setup() {
        ReflectionTestUtils.setField(target, "gasEstimateLogMapper", gasEstimateLogMapper);
        ReflectionTestUtils.setField(target, "epochBusinessMapper", epochBusinessMapper);
    }
	
	 @Test
	 public void test() throws InterruptedException, ExecutionException, BeanCreateOrUpdateException, IOException {
		 GasEstimateEvent gasEstimateEvent = new GasEstimateEvent();
		 List<GasEstimate> estimateList = new ArrayList<>();
		 GasEstimate gasEstimate = new GasEstimate();
		 gasEstimate.setAddr("1");
		 estimateList.add(gasEstimate);
		 gasEstimateEvent.setEstimateList(estimateList);
		 gasEstimateEvent.setSeq(1l);
		 target.onEvent(gasEstimateEvent, 1, false);
		 gasEstimateEvent.getEstimateList();
		 gasEstimateEvent.getSeq();
		 ReflectionTestUtils.setField(target, "epochBusinessMapper", null);
		 target.onEvent(gasEstimateEvent, 1, false);
	 }
	
}
