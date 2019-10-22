package com.platon.browser.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
	
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
		tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
		return tomcatFactory;
	}

	class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
		public void customize(Connector connector) {
			Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
			// 设置最大连接数
			protocol.setMaxConnections(20000);
			// 设置最大线程数
			protocol.setMaxThreads(2000);
			protocol.setConnectionTimeout(30000);
		}
	}

}