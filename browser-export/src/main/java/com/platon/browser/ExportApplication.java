package com.platon.browser;

import com.platon.browser.service.ExportService;
import com.platon.browser.util.SleepUtil;
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
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
	@Autowired
	private ExportService exportService;

	public static void main(String[] args) {
		SpringApplication.run(ExportApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		EXECUTOR_SERVICE.submit(() -> exportService.exportTxHash());
		EXECUTOR_SERVICE.submit(() -> exportService.exportAddress());
		EXECUTOR_SERVICE.submit(() -> exportService.exportNodeId());
		EXECUTOR_SERVICE.submit(() -> exportService.exportRpPlanAddress());
		EXECUTOR_SERVICE.submit(() -> exportService.exportDelegationInfo());
		EXECUTOR_SERVICE.submit(() -> exportService.exportProposal());
		EXECUTOR_SERVICE.submit(() -> exportService.exportVote());
		while (!ExportService.isTxHashExportDone() || !ExportService.isAddressExportDone()
				|| !ExportService.isRpplanExportDone() || !ExportService.isDelegationExportDone()
				|| !ExportService.isNodeExportDone()|| !ExportService.isProposalExportDone()|| !ExportService.isVoteExportDone()) {
			SleepUtil.sleep(1L);
		}
		log.info("数据导出完成!");
		System.exit(0);
	}
}
