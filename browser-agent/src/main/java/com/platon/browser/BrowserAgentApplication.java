package com.platon.browser;

import com.platon.browser.task.BlockSyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class BrowserAgentApplication {

	@Autowired
	private BlockSyncTask blockSyncTask;

	public static void main(String[] args) {
		SpringApplication.run(BrowserAgentApplication.class, args);
	}

	@PostConstruct
	private void start() throws InterruptedException {
		blockSyncTask.init();
		blockSyncTask.start();
	}
}
