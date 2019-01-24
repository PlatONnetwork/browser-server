package com.platon.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ImportResource(locations={"classpath:jobs.xml"})
@EnableScheduling
@SpringBootApplication
public class AgentApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }


}

