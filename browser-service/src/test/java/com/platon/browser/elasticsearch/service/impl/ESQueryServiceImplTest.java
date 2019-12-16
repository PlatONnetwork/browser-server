package com.platon.browser.elasticsearch.service.impl;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserServiceApplication;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.DelegationESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserServiceApplication.class, value = "spring.profiles.active=testdelete")
public class ESQueryServiceImplTest {

	@Autowired
	private BlockESRepository blockESRepository;
	
	@Autowired
	private TransactionESRepository transactionESRepository;
	
	@Autowired
	private DelegationESRepository delegationESRepository;
	
	@Test
	public void testBlockES() throws IOException {
		if(!blockESRepository.existsIndex()) {
			blockESRepository.createIndex(null);
		}
		if(blockESRepository.existsIndex()) {
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.setDesc("num");
			constructor.setFrom(1);
			constructor.setSize(10);
			constructor.mustNot(new ESQueryBuilders().term("num", "1"));
			constructor.should(new ESQueryBuilders().term("nodeId", "0x"));
			try {
				blockESRepository.search(constructor, Block.class, 1, 100);
			} catch (IOException e) {
				
			}
			blockESRepository.deleteIndex();
		}
	}
	
	@Test
	public void testTransactionES() throws IOException {
		if(!transactionESRepository.existsIndex()) {
			transactionESRepository.createIndex(null);
		}
		if(transactionESRepository.existsIndex()) {
			transactionESRepository.deleteIndex();
		}
	}
	
	@Test
	public void testDelegationES() throws IOException {
		if(!delegationESRepository.existsIndex()) {
			delegationESRepository.createIndex(null);
		}
		if(delegationESRepository.existsIndex()) {
			delegationESRepository.deleteIndex();
		}
	}
	
}
