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

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableRetry
@SpringBootApplication
@MapperScan(basePackages = "com.platon.browser.dao.mapper")
public class ExportApplication implements ApplicationRunner {
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);
	@Autowired
	private ExportService exportService;

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
//			!ExportService.isTxHashExportDone() ||
//			!ExportService.isAddressExportDone() ||
//			!ExportService.isRpplanExportDone() ||
//			!ExportService.isDelegationExportDone()||
//			!ExportService.isNodeExportDone()||
//			!ExportService.isProposalExportDone()||
//			!ExportService.isVoteExportDone()
//		) {
//			SleepUtil.sleep(1L);
//		}
//		EXECUTOR_SERVICE.submit(() -> exportService.exportDelegationReward());
		
//		EXECUTOR_SERVICE.submit(() -> {
//			try {
//				exportService.exportBalance();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
//		EXECUTOR_SERVICE.submit(() -> {
//			try {
//				exportService.exportBenBalance();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
		
//		EXECUTOR_SERVICE.submit(() -> {
//			try {
//				exportService.exportNodeInfo();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
		
		
		while (
//			!ExportService.isDelegationRewardExportDone() ||
			!ExportService.isTxInfoExportDone()||
			!ExportService.isStakingExportDone()
		) {
//			while(exportService.checkDatabaseNumer()){
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			EXECUTOR_SERVICE.submit(() -> {
//				try {
//					exportService.exportNodeInfo();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			});
//			SleepUtil.sleep(300L);
			
			while (exportService.checkNumer().compareTo(BigInteger.ZERO) == 0) {
				log.debug("wait block");
			}
//			EXECUTOR_SERVICE.submit(() -> {
//				try {
//					exportService.exportStaking();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			});
			EXECUTOR_SERVICE.submit(() -> {
				try {
					exportService.exportAllStaking();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			EXECUTOR_SERVICE.submit(() -> exportService.exportAllTx());
			SleepUtil.sleep(300L);
			exportService.addCount();
			
		}
		log.info("数据导出完成!");
		System.exit(0);
	}
}
