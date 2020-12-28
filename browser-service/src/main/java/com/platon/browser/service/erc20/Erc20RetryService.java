package com.platon.browser.service.erc20;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Erc20RetryService {
    @Resource
    private Erc20TokenMapper erc20TokenMapper;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Erc20Token getErc20Token (String contractAddress ) {
        Erc20Token erc20Token = this.erc20TokenMapper.selectByAddress(contractAddress);
        if (null == erc20Token) {
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException("合约查询线程被中断！");
            }
            throw new BusinessException("合约【"+contractAddress+"】未入库，稍后重试...");
        }
        return erc20Token;
    }
}