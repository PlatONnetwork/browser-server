package com.platon.browser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 委托活动 E阶段服务
 */
@Slf4j
@Service
public class DelegateActivityEStageService extends ServiceBase{
	@Value("${fileUrl}")
	private String fileUrl;
	@Override
	public String getFileUrl() {
		return fileUrl;
	}

	public void doService(){

	}
}
