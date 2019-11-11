package com.platon.browser.task;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.platon.browser.common.enums.AddressTypeEnum;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.entity.AddressStatistics;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.AddressExample;
import com.platon.browser.dao.mapper.AddressMapper;

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
	
    @Scheduled(cron = "0/1  * * * * ?")
    private void cron () throws InterruptedException {
    	// 只有程序正常运行才执行任务
		if(!AppStatusUtil.isRunning()) return;
    	
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
    	addressExample.setOrderByClause("create_time limit "+start+","+size);
    	addressExample.createCriteria().andTypeEqualTo(AddressTypeEnum.ACCOUNT.getCode());
    	List<Address> addressList = addressMapper.selectByExample(addressExample);
    	
    	if( addressList.size() == 0) {
    		stop = true;
    		return stop;
    	}
    	
		if(addressList.size() < size) {
			stop = true;
		}
		
		List<String> addressStringList = addressList.stream().map(Address::getAddress).collect(Collectors.toList());
		
		//查询该地址发起的质押（有效的质押和赎回的质押）
		List<AddressStatistics> stakingList = statisticBusinessMapper.getAddressStatisticsFromStaking(addressStringList);
		
		//查询该地址发起的委托
		List<AddressStatistics> delegationList = statisticBusinessMapper.getAddressStatisticsFromDelegation(addressStringList);
		
		//汇总结果
		Map<String, AddressStatistics> stakingMap = stakingList.stream().collect(Collectors.toMap(AddressStatistics::getStakingAddr, v -> v, (v1, v2) ->{
			v1.setStakingHes(v1.getStakingHes().add(v2.getStakingHes()));
			v1.setStakingLocked(v1.getStakingLocked().add(v2.getStakingLocked()));
			v1.setStakingReduction(v1.getStakingReduction().add(v2.getStakingReduction()));
			return v1;
		}));
		Map<String, AddressStatistics> delegationMap = delegationList.stream().collect(Collectors.toMap(AddressStatistics::getDelegateAddr, v -> v, (v1, v2) -> {
			v1.setDelegateHes(v1.getDelegateHes().add(v2.getDelegateHes()));
			v1.setDelegateLocked(v1.getDelegateLocked().add(v2.getDelegateLocked()));
			v1.setDelegateReleased(v1.getDelegateReleased().add(v2.getDelegateReleased()));
			v1.getNodeIdSet().add(v2.getNodeId());

			return v1;
		}));
		
		addressList.stream().forEach(item ->{
			AddressStatistics staking= stakingMap.get(item.getAddress());
			AddressStatistics delegation= delegationMap.get(item.getAddress());
			if(staking != null ) {
				item.setStakingValue(staking.getStakingHes().add(staking.getStakingLocked()));
				item.setRedeemedValue(staking.getStakingReduction());
			}else {
				item.setStakingValue(BigDecimal.ZERO);
				item.setRedeemedValue(BigDecimal.ZERO);
			}
			
			if(delegation != null ) {
				item.setDelegateHes(delegation.getDelegateHes());
				item.setDelegateLocked(delegation.getDelegateLocked());
				item.setDelegateValue(delegation.getDelegateLocked().add(delegation.getDelegateHes()));
				item.setDelegateReleased(delegation.getDelegateReleased());
				delegation.getNodeIdSet().add(delegation.getNodeId());
				item.setCandidateCount(delegation.getNodeIdSet().size());
			}else {
				item.setDelegateHes(BigDecimal.ZERO);
				item.setDelegateLocked(BigDecimal.ZERO);
				item.setDelegateValue(BigDecimal.ZERO);
				item.setDelegateReleased(BigDecimal.ZERO);
				item.setCandidateCount(0);
			}
		});
		
		statisticBusinessMapper.batchUpdateFromTask(addressList);
		return stop;
    }
    
    
}
