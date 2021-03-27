package com.platon.browser;

import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@EnableAsync
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class BrowserApiApplication implements ApplicationRunner {

    @Resource
    private NetworkStatMapper networkStatMapper;

    /**
     * 0出块等待的循环访问时间
     */
    @Value("${platon.zeroBlockNumber.wait-time:1}")
    private Integer zeroBlockNumberWaitTime;

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
        // 0出块等待
        while (true) {
            long count = networkStatMapper.countByExample(null);
            if (count > 0) {
                log.error("开始出块");
                break;
            }
            Thread.sleep(1000L * zeroBlockNumberWaitTime);
            log.error("正在等待出块...");
        }
        // 把应用置为RUNNING运行状态,让定时任务可以执行业务逻辑
        AppStatusUtil.setStatus(AppStatus.RUNNING);
    }

}
