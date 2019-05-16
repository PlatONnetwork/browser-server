package com.platon.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApiApplication.class, args);
	}

}
