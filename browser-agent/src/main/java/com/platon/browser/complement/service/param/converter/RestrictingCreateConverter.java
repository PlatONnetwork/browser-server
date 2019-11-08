package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.restricting.RestrictingCreate;
import com.platon.browser.common.complement.dto.restricting.RestrictingItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.param.RestrictingCreateParam;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class RestrictingCreateConverter extends BusinessParamConverter<RestrictingCreate> {
	
	
	
    @Override
    public RestrictingCreate convert(CollectionEvent event, CollectionTransaction tx) {
    	RestrictingCreateParam txParam = tx.getTxParam(RestrictingCreateParam.class);
    	String account = txParam.getAccount();
    	
    	List<RestrictingItem> restrictingItems = txParam.getPlans().stream().map(plan -> { return RestrictingItem.builder()
				.address(account)
				.amount(new BigDecimal(plan.getAmount()))
				.epoch(plan.getEpoch().longValue())
				.number(tx.getNum())
				.build();
			}).collect(Collectors.toList());
    	
    	RestrictingCreate businessParam= RestrictingCreate.builder()
    			.itemList(restrictingItems)
                .build();
    	
        return businessParam;
    }
}
