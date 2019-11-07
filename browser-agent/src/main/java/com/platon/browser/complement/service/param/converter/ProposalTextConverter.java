package com.platon.browser.complement.service.param.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.proposal.ProposalText;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.param.ProposalTextParam;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalTextConverter extends BusinessParamConverter<ProposalText> {

    @Autowired
    private BlockChainConfig chainConfig;
	
    @Override
    public ProposalText convert(CollectionTransaction tx) {
    	ProposalTextParam txParam = tx.getTxParam(ProposalTextParam.class);

    	ProposalText businessParam= ProposalText.builder()
    			.nodeId(txParam.getVerifier())
    			.pIDID(txParam.getPIDID())
//    			.url(url)
                .build();
        return businessParam;
    }
}
