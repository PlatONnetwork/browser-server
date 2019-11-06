package com.platon.browser;

import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.bootstrap.service.ConsistencyService;
import com.platon.browser.bootstrap.service.InitializationService;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.collection.queue.publisher.BlockEventPublisher;
import com.platon.browser.collection.service.block.BlockService;
import com.platon.browser.collection.service.transaction.ReceiptService;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.service.epoch.EpochService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

@Slf4j
@EnableRetry
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser.dao.mapper","com.platon.browser.persistence.dao.mapper"})
public class AgentApplication implements ApplicationRunner {
	public static void main(String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}

	// 区块服务
	@Autowired
	private BlockService blockService;
	// 交易服务
	@Autowired
	private ReceiptService receiptService;
	// 区块事件发布服务
	@Autowired
	private BlockEventPublisher blockEventPublisher;
	@Autowired
	private EpochService epochService;
    @Autowired
    private ConsistencyService consistencyService;
    @Autowired
	private InitializationService initializationService;

	// 已采集的最高块号
	// TODO: 启动时需要使用初始化数据初始化区块号
	private Long collectedNumber = 0L;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String status = System.getProperty(AppStatus.class.getName());
		if(StringUtils.isNotBlank(status)&&AppStatus.valueOf(status)==AppStatus.STOP) return;

        // 启动(mysql/es/redis)一致性检查
        consistencyService.synchronize();

		// 启动初始化子流程
		InitializationResult initialResult = initializationService.init();
		collectedNumber = initialResult.getCollectedBlockNumber();

		while (true) {
			try {
				collectedNumber++;
				// 检查区块号是否合法
				blockService.checkBlockNumber(collectedNumber);
				// 异步获取区块
				CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(collectedNumber);
				// 异步获取交易回执
				CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(collectedNumber);
				// 获取周期切换消息
				EpochMessage epochMessage = epochService.getEpochMessage(collectedNumber);
				blockEventPublisher.publish(blockCF, receiptCF,epochMessage);
			}catch (Exception e){
				log.error("程序因错误而停止:",e);
				break;
			}
		}
	}
}
