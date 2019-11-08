package com.platon.browser.complement.service.param.converter;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.proposal.ProposalVersion;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.param.VersionDeclareParam;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalVersionConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {
	
	
    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
    	VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);
 
        String desc = CustomNodeOpt.TypeEnum.VERSION.getTpl()
                .replace("NODE_NAME",txParam.getNodeName())
                .replace("VERSION",String.valueOf(txParam.getVersion()))
                .replace("ACTIVE_NODE", txParam.getActiveNode());
    	
        ComplementNodeOpt c = ComplementNodeOpt.newInstance();
        c.setNodeId(txParam.getActiveNode());
        c.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.PROPOSALS.getCode()));
        c.setDesc(desc);
        c.setTxHash(tx.getHash());
        c.setBNum(event.getBlock().getNum());
        c.setTime(event.getBlock().getTime());
        return Optional.ofNullable(c);
    }
}
