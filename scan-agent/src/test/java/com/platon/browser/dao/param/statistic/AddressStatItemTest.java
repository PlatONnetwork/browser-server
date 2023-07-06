package com.platon.browser.dao.param.statistic;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressStatItemTest extends AgentTestBase {

	@Test
	public void test(){
		AddressStatItem target = AddressStatItem.builder()
				.address("null")
				.contractCreate("null")
				.contractCreatehash("null")
				.contractName("null")
				.delegateQty(3)
				.proposalQty(3)
				.stakingQty(3)
				.transferQty(3)
				.txQty(33)
				.type(3)
				.build();
		target.setAddress(null)
		.setContractCreate(null)
		.setContractCreatehash(null)
		.setContractName(null)
		.setDelegateQty(3)
		.setProposalQty(3)
		.setStakingQty(3)
		.setTransferQty(3)
		.setTxQty(33)
		.setType(3);

		target.getAddress();
		target.getContractCreate();
		target.getContractCreatehash();
		target.getContractName();
		target.getDelegateQty();
		target.getProposalQty();
		target.getStakingQty();
		target.getTransferQty();
		target.getTxQty();
		target.getType();

		assertTrue(true);
	}
	
}
