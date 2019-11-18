package com.platon.browser.complement.converter.proposal;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.proposal.ProposalVoteConverter;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @description: 委托参数转换器测试类
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalVoteConverterTest extends AgentTestBase {

	@Mock
	private ProposalBusinessMapper proposalBusinessMapper;
	@Mock
	private NetworkStatCache networkStatCache;
	@Mock
	private ProposalMapper proposalMapper;
	@Mock
	private NodeCache nodeCache;

	@Spy
	private ProposalVoteConverter target;

	@Before
	public void setup()throws Exception{
		ReflectionTestUtils.setField(target,"proposalBusinessMapper",proposalBusinessMapper);
		ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
		ReflectionTestUtils.setField(target,"proposalMapper",proposalMapper);
		ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
		NodeItem nodeItem = NodeItem.builder()
				.nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
				.nodeName("integration-node1")
				.stakingBlockNum(new BigInteger("88602"))
				.build();
		when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
		when(proposalMapper.selectByPrimaryKey(anyString())).thenReturn(proposalList.get(0));
		when(networkStatCache.getAndIncrementNodeOptSeq()).thenReturn(1l);
	}

	@Test
	public void convert(){
		Block block = blockList.get(0);
		CollectionEvent collectionEvent = CollectionEvent.builder()
				.block(block)
				.build();
		Transaction tx = new Transaction();
		for(CollectionTransaction collectionTransaction : transactionList){
			if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.PROPOSAL_VOTE)){
				tx = collectionTransaction;
			}
		}
		target.convert(collectionEvent,tx);
	}
}
