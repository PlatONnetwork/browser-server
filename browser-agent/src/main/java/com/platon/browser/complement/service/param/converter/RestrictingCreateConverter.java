package com.platon.browser.complement.service.param.converter;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.restricting.RestrictingCreate;
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

    	RestrictingCreate businessParam= RestrictingCreate.builder()    		
                .build();
        return businessParam;
    }
}
