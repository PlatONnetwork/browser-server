package com.platon.browser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class BrowserCommonApplication {

	/**
	 * spring boot启动主类
	 * @method main
	 */
	public static void main(String[] args) {
		SpringApplication.run(BrowserCommonApplication.class, args);
	}

}
