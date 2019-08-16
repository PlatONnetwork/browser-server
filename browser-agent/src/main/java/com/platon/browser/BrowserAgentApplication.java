package com.platon.browser;

import com.platon.browser.task.BlockSyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class BrowserAgentApplication implements ApplicationRunner {

	@Autowired
	private BlockSyncTask blockSyncTask;

	public static void main(String[] args) {
		SpringApplication.run(BrowserAgentApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		blockSyncTask.init();
		blockSyncTask.start();
	}
}
