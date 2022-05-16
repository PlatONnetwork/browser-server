package com.platon.browser;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper"})
public class BrowserServiceApplication {

    /**
     * spring boot启动主类
     *
     * @method main
     */
    public static void main(String[] args) {
        SpringApplication.run(BrowserServiceApplication.class, args);
    }

}
