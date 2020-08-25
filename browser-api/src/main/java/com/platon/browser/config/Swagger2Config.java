package com.platon.browser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Profile({ "dev", "test" })
public class Swagger2Config {

	@Bean
	public Docket controllerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder().title("标题：PlatScan_接口文档")
						.version("版本号:1.0").build())
				.select().apis(RequestHandlerSelectors.basePackage("com.platon.browser.controller"))
				.paths(PathSelectors.any())
				.build();
	}
}