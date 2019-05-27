package com.platon.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BrowserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrowserApiApplication.class, args);
	}

}
