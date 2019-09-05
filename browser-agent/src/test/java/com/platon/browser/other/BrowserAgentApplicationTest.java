package com.platon.browser.other;

import com.platon.browser.task.BlockSyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BrowserAgentApplicationTest {
//public class BrowserAgentApplication {

	@Autowired
	private BlockSyncTask blockSyncTask;

	public static void main(String[] args) {
		SpringApplication.run(BrowserAgentApplicationTest.class, args);
	}

}
