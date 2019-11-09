package com.platon.browser.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 地址表补充
 * <pre>
 * 需要补充的字段
 * staking_value     质押的金额
 * delegate_value    委托的金额
 * redeemed_value    赎回中的金额
 * candidate_count   已委托的验证人
 * delegate_hes      未锁定委托
 * delegate_locked   已锁定委托
 * delegate_released 赎回中的
 * <pre/>
 * @author chendai
 */
@Component
@Slf4j
public class AddressUpdateTask {
	
	@Autowired
	private StatisticBusinessMapper statisticBusinessMapper;
	
    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
    	
		try {
			// 所有质押 <发起质押地址-质押实体列表> 映射
			
			
			
		} catch (Exception e) {
            log.error("on AddressUpdateTask error",e);
		}
    }
}
