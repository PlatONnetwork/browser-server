package com.platon;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 重要说明：
 * 重要说明：
 * 正常情况下不需要AgentApplicationTest.class这个文件，由于项目中的AgentApplication.class在启动后，会进入run方法中的for{}循环，开始自动工作，这个不符合这里单元测试所要求的场景。
 * 因此，单独写这个AgentApplicationTest.class以便来加载ApplicationContext，
 * 同时加上excludeFilters以便排除扫描到AgentApplication.class，
 * 但是发现这样不奏效，而是需要注释掉AgentApplication.class的@Configuration，@SpringBootApplication这两个注解才行。
 * >>>>!!!在测试完成后，需要把AgentApplication.class的@Configuration，@SpringBootApplication这两个注解打开!!!<<<
 *
 */
@Slf4j
@EnableRetry
@Configuration
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@ComponentScan(basePackages = {"com.platon.browser.**"},
		excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {com.platon.browser.AgentApplication.class})})
@MapperScan(basePackages = {"com.platon.browser", "com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper", "com.platon.browser.v0150.dao", "com.platon.browser.v0151.dao"})
public class AgentApplicationTest implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
