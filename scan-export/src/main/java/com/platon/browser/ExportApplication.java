package com.platon.browser;

import com.platon.browser.service.ExportGallyService;
import com.platon.browser.service.ExportTpsService;
import com.platon.browser.utils.SleepUtil;
import lombok.extern.slf4j.Slf4j;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableRetry
@SpringBootApplication
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class ExportApplication implements ApplicationRunner {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
//	@Autowired
//	private ExportService exportService;
	@Autowired
	private ExportGallyService exportGallyService;
	@Autowired
	private ExportTpsService exportTpsService;
	

	public static void main(String[] args) {
		SpringApplication.run(ExportApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
//		EXECUTOR_SERVICE.submit(() -> exportService.exportTxHash());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportAddress());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportNodeId());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportRpPlanAddress());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportDelegationInfo());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportProposal());
//		EXECUTOR_SERVICE.submit(() -> exportService.exportVote());
//		while (
//			!exportService.isTxHashExportDone() ||
//			!exportService.isAddressExportDone() ||
//			!exportService.isRpplanExportDone() ||
//			!exportService.isDelegationExportDone()||
//			!exportService.isNodeExportDone()||
//			!exportService.isProposalExportDone()||
//			!exportService.isVoteExportDone()
//		) {
//			SleepUtil.sleep(1L);
//		}
//		EXECUTOR_SERVICE.submit(() -> exportService.exportDelegationReward());
//		EXECUTOR_SERVICE.submit(() -> exportGallyService.exportMatch());
//		EXECUTOR_SERVICE.submit(() -> exportGallyService.transfer());
		
//		EXECUTOR_SERVICE.submit(() -> exportGallyService.exportLegalTx());
//		EXECUTOR_SERVICE.submit(() -> exportGallyService.exportMatchNode());
		
		EXECUTOR_SERVICE.submit(() -> exportTpsService.exportData());
//		EXECUTOR_SERVICE.submit(() -> exportGallyService.exportContractData());
		while (
			!exportTpsService.isTxInfoExportDone() 
//			!exportGallyService.isExportLegalTxDone()
		) {
			SleepUtil.sleep(1L);
		}
		log.info("数据导出完成!");
		System.exit(0);
	}
}
