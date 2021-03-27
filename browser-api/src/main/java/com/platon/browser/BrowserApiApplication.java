package com.platon.browser;

import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@EnableAsync
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class BrowserApiApplication implements ApplicationRunner {

    /**
     * spring boot启动主类
     *
     * @method main
     */
    public static void main(String[] args) {
        SpringApplication.run(BrowserApiApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (AppStatusUtil.isStopped()) {
            return;
        }
        // 把应用置为RUNNING运行状态,让定时任务可以执行业务逻辑
        AppStatusUtil.setStatus(AppStatus.RUNNING);
    }

}
