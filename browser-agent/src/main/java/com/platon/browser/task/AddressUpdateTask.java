package com.platon.browser.task;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.platon.browser.common.enums.AddressTypeEnum;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.AddressMapper;

import jnr.ffi.Struct.int16_t;
import lombok.extern.slf4j.Slf4j;


/**
 * 地址表补充
 * <pre>
 * 需求：补充的字段
 * staking_value     质押的金额
 * delegate_value    委托的金额
 * redeemed_value    赎回中的金额
 * candidate_count   已委托的验证人
 * delegate_hes      未锁定委托
 * delegate_locked   已锁定委托
 * delegate_released 赎回中的
 * 
 * 注意事项
 * 地址表数量比较巨大
 * <pre/>
 * @author chendai
 */
@Component
@Slf4j
public class AddressUpdateTask {
	
	@Autowired
	private StatisticBusinessMapper statisticBusinessMapper;
	@Autowired
	private AddressMapper addressMapper;
    @Value("${task.address-batch-size}")
    private int batchSize;
	
//    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
    	
		try {
			int start = 0;
			while (!batchUpdate(start, batchSize)) {
				start += batchSize;
			}
			
		} catch (Exception e) {
            log.error("on AddressUpdateTask error",e);
		}
    }
    
    /**
     * 执行任务
     * @param start 开始的块高
     * @param size 执行的批次
     * @return
     */
    private boolean batchUpdate(int start, int size) {
    	boolean stop = false;
    	
    	//查询待补充的地址
    	AddressExample addressExample = new AddressExample();
    	addressExample.setOrderByClause("create_time desc limit "+start+","+size);
    	addressExample.createCriteria().andTypeEqualTo(AddressTypeEnum.ACCOUNT.getCode());
    	List<Address> addressList = addressMapper.selectByExample(addressExample);
		if(addressList.size() == size || addressList.size() == 0) {
			stop = true;
		}
		
		//查询该地址发起的质押（有效的质押和赎回的质押）
    	
    	
    	
		//质押和质押锁定的金额
		
		return stop;
    }
    
    
}
