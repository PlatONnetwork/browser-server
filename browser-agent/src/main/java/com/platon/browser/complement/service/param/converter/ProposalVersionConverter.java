package com.platon.browser.complement.service.param.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.proposal.ProposalVersion;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.param.VersionDeclareParam;
import com.platon.browser.persistence.dao.mapper.ProposalBusinessMapper;

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
 
        String desc = CustomNodeOpt.TypeEnum.VERSION.getTpl()
                .replace("NODE_NAME",txParam.getNodeName())
                .replace("VERSION",String.valueOf(txParam.getVersion()))
                .replace("ACTIVE_NODE", txParam.getActiveNode());
    	
    	ProposalVersion businessParam= ProposalVersion.builder()  
    			.optDesc(desc)
                .build();
    	
        return businessParam;
    }
}
