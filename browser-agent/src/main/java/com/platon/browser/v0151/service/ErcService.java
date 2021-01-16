package com.platon.browser.v0151.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Erc服务
 */
@Slf4j
@Service
public class ErcService {
    @Resource
    private ErcDetectService ercDetectService;

    /**
     * 分析合约地址
     * @param contractAddress
     * @throws Exception
     */
    public void analyze(String contractAddress) throws Exception {
        ercDetectService.isSupportErc20(contractAddress);
    }
}
