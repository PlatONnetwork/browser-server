package com.platon.browser;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.application.CollectionBlockEventPublisher;
import com.platon.browser.collection.service.BlockService;
import com.platon.browser.collection.service.TransactionService;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.complement.queue.callback.CollectionBlockCallback;
import com.platon.browser.queue.event.collection.EpochMessage;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Slf4j
@EnableRetry
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
public class AgentApplication implements ApplicationRunner {
	public static void main(String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}

	// 区块服务
	@Autowired
	private BlockService blockService;
	// 交易服务
	@Autowired
	private TransactionService transactionService;
	// 区块发布服务
	@Autowired
	private CollectionBlockEventPublisher collectionBlockEventPublisher;
	@Autowired
	private CollectionBlockCallback collectionBlockCallback;
	@Autowired
	private PlatOnClient platOnClient;
	@Autowired
	private SpecialContractApi specialContractApi;
	// 已采集的最高块号
	private Long collectedNumber = 0L;

	@Override
	public void run(ApplicationArguments args) {
		String status = System.getProperty(AppStatus.class.getName());
		if(StringUtils.isNotBlank(status)&&AppStatus.valueOf(status)==AppStatus.STOP) return;

		for (;true;){
			try {
				collectedNumber++;
				// 异步获取区块
				CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(collectedNumber);
				// 异步获取交易回执
				CompletableFuture<ReceiptResult> receiptCF = transactionService.getReceiptAsync(collectedNumber);
				// 构造
				EpochMessage epochMessage = new EpochMessage(BigInteger.valueOf(collectedNumber),platOnClient,specialContractApi);
				collectionBlockEventPublisher.publish(blockCF, receiptCF,epochMessage,collectionBlockCallback);
			}catch (Exception e){
				break;
			}
		}
	}
}
