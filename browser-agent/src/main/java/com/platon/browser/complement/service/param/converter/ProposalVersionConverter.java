package com.platon.browser.complement.service.param.converter;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.proposal.ProposalVersion;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.param.VersionDeclareParam;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalVersionConverter extends BusinessParamConverter<ProposalVersion> {
	
    @Override
    public ProposalVersion convert(CollectionEvent event, CollectionTransaction tx) {
    	VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);

    	ProposalVersion businessParam= ProposalVersion.builder()    		
                .build();
        return businessParam;
    }
}
