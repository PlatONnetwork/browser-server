package com.platon.browser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class BrowserApiApplication {

	/**
	 * spring boot启动主类
	 * @method main
	 */
	public static void main(String[] args) {
		SpringApplication.run(BrowserApiApplication.class, args);
	}

}
