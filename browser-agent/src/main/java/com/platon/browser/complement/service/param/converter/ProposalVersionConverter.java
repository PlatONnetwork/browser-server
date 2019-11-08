package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.VersionDeclareParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalVersionConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private NetworkStatCache networkStatCache;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
    	VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);
 
        String desc = CustomNodeOpt.TypeEnum.VERSION.getTpl()
                .replace("NODE_NAME",txParam.getNodeName())
                .replace("VERSION",String.valueOf(txParam.getVersion()))
                .replace("ACTIVE_NODE", txParam.getActiveNode());
    	
        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(txParam.getActiveNode());
        nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.PROPOSALS.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(event.getBlock().getNum());
        nodeOpt.setTime(event.getBlock().getTime());
        return Optional.ofNullable(nodeOpt);
    }
}
