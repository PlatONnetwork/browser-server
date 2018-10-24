package com.platon.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/*@ImportResource(locations={"classpath:spring/spring-redis.xml","classpath:juweb-sdk-client.xml","classpath:spring-ice-broker.xml"})*/
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ComponentScan({"com.platon.browser"})
public class ServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ServiceApplication.class);
    }
}