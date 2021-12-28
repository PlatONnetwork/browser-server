package com.platon.browser;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import javax.sql.DataSource;

@EnableApolloConfig
@EnableCaching
@Slf4j
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@EnableAsync
@MapperScan(basePackages = {"com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper"})
public class BrowserApiApplication implements ApplicationRunner {

    @Resource
    private NetworkStatMapper networkStatMapper;

    /**
     * 0出块等待的循环访问时间
     */
    @Value("${platon.zeroBlockNumber.wait-time:1}")
    private Integer zeroBlockNumberWaitTime;

    @Resource
    DataSource dataSource;

    /**
     * spring boot启动主类
     *
     * @method main
     */
    public static void main(String[] args) {
        SpringApplication.run(BrowserApiApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (AppStatusUtil.isStopped()) {
            return;
        }
        dataSourceLog();
        zeroBlockNumberWait();
        // 把应用置为RUNNING运行状态,让定时任务可以执行业务逻辑
        AppStatusUtil.setStatus(AppStatus.RUNNING);
    }

    /**
     * 0出块等待
     *
     * @param
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/7
     */
    private void zeroBlockNumberWait() {
        try {
            while (true) {
                long count = networkStatMapper.countByExample(null);
                if (count > 0) {
                    log.info("开始出块...");
                    break;
                }
                Thread.sleep(1000L * zeroBlockNumberWaitTime);
                log.info("正在等待出块...");
            }
        } catch (Exception e) {
            log.error("0出块等待异常", e);
        }
    }

    /**
     * 打印连接池信息
     *
     * @param
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/2
     */
    private void dataSourceLog() {
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        log.info("数据源:{},最大连接数:{},最小连接池数量:{},初始化连接数:{},获取连接时最大等待时间:{},启用公平锁:{}", dataSource.getClass(), druidDataSource.getMaxActive(), druidDataSource.getMinIdle(), druidDataSource.getInitialSize(),
                 // 配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁
                 druidDataSource.getMaxWait(), druidDataSource.isUseUnfairLock());
    }

}
