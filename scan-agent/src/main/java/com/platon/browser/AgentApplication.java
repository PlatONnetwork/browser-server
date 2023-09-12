package com.platon.browser;

import com.platon.browser.bean.EpochMessage;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.bootstrap.service.ConsistencyService;
import com.platon.browser.bootstrap.service.InitializationService;
import com.platon.browser.client.RetryableClient;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.publisher.BlockEventPublisher;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.epoch.EpochService;
import com.platon.browser.service.receipt.ReceiptService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.CommonUtil;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 重要说明：
 * 如果单元测试需要加载spring context，不要用AgentApplication.class来加载，因为这个class加载运行后会进入一个for循环。
 * 可以在test目录，写一个继承自ApplicationRunner的、run方法空的类。参考com.platon.AgentApplicationTest
 * 并且，在执行这样的单元测试时，需要注释掉AgentApplication.class的@Configuration，@SpringBootApplication这两个注解才行（测试完成后，记得取消此注释）
 *
 */
@Slf4j
@EnableRetry
@Configuration// 执行单元测试，需要注释掉 @Configuration，@SpringBootApplication
@EnableScheduling  // 执行单元测试，需要注释掉 @Configuration，@SpringBootApplication
@SpringBootApplication// 执行单元测试，需要注释掉 @Configuration，@SpringBootApplication
@EnableEncryptableProperties
// @ServletComponentScan
@ComponentScan(basePackages = {"com.platon.browser.**"})
@MapperScan(basePackages = {"com.platon.browser", "com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper", "com.platon.browser.v0150.dao", "com.platon.browser.v0151.dao"})
public class AgentApplication implements ApplicationRunner {

    /**
     * 区块服务
     */
    @Resource
    private BlockService blockService;

    /**
     * 交易服务
     */
    @Resource
    private ReceiptService receiptService;

    /**
     * 区块事件发布服务
     */
    @Resource
    private BlockEventPublisher blockEventPublisher;

    /**
     * 周期服务
     */
    @Resource
    private EpochService epochService;

    /**
     * 启动一致性检查服务
     */
    @Resource
    private ConsistencyService consistencyService;

    /**
     * 启动初始化服务
     */
    @Resource
    private InitializationService initializationService;

    @Resource
    private RetryableClient retryableClient;

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception{
        if (AppStatusUtil.isRunningJUnitTest()){
            return;
        }
        if (AppStatusUtil.isStopped()) {
            return;
        }
        String traceId = CommonUtil.createTraceId();
        CommonUtil.putTraceId(traceId);
        // 把应用置为BOOTING开机状态
        AppStatusUtil.setStatus(AppStatus.BOOTING);
        retryableClient.zeroBlockNumberWait();
        InitializationResult initialResult = initializationService.init(traceId);
        consistencyService.post(traceId);
        // 启动自检和初始化完成后,把应用置为RUNNING运行状态,让定时任务可以执行业务逻辑
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        // 已采最高块号
        // 初始第一次采集时: collectedNumber = -1
        long collectedNumber = initialResult.getCollectedBlockNumber();
        // 前一个块号
        long preBlockNum;
        CommonUtil.removeTraceId();
        // 进入区块采集主流程
        while (true) {
            try {
                traceId = CommonUtil.createTraceId();
                CommonUtil.putTraceId(traceId);
                preBlockNum = collectedNumber++;
                // 经过上面这行代码执行后，初始第一次采集时: preBlockNum = -1，collectedNumber = 0

                // 检查区块号是否合法
                blockService.checkBlockNumber(collectedNumber);
                // 异步获取区块
                CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(collectedNumber);
                // 异步获取交易回执
                CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(collectedNumber);
                // 获取周期切换消息
                EpochMessage epochMessage = epochService.getEpochMessage(collectedNumber);
                blockEventPublisher.publish(blockCF, receiptCF, epochMessage, traceId);
                if (preBlockNum != 0L && (collectedNumber - preBlockNum != 1)) {
                    log.error("采集数据异常,当前区块{},前一个区块{}", collectedNumber, preBlockNum);
                    throw new AssertionError();
                }
                CommonUtil.removeTraceId();
            } catch (Exception e) {
                log.error("程序因错误而停止:", e);
                break;
            } finally {
                CommonUtil.removeTraceId();
            }
        }
    }

}
