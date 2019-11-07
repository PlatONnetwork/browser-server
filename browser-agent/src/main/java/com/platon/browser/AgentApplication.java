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
import com.platon.browser.common.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.common.service.epoch.EpochService;
import com.platon.browser.complement.handler.CollectionEventHandler;
import com.platon.browser.persistence.handler.ComplementEventHandler;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

@Slf4j
@EnableRetry
@Configuration
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
	private Long collectedNumber = 0L;

	private Long preBlockNum=0L;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String status = System.getProperty(AppStatus.class.getName());
		if(StringUtils.isNotBlank(status)&&AppStatus.valueOf(status)==AppStatus.STOP) return;
        // 进入一致性自检子流程
        consistencyService.synchronize();
		// 进入应用初始化子流程
		InitializationResult initialResult = initializationService.init();
		collectedNumber = initialResult.getCollectedBlockNumber();
		// 进入区块采集主流程
		while (true) {
			try {
				preBlockNum=collectedNumber;
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

				if(preBlockNum!=0L&&(collectedNumber-preBlockNum!=1)) throw new AssertionError();
			}catch (Exception e){
				log.error("程序因错误而停止:",e);
				break;
			}
		}
	}

	// 整合各模块事件处理器
	@Bean
	public ICollectionEventHandler collectionEventHandler(){
		return new CollectionEventHandler();
	}
	@Bean
	public IComplementEventHandler complementEventHandler(){
		return new ComplementEventHandler();
	}
}
