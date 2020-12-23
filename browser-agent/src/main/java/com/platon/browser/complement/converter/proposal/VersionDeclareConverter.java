package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.VersionDeclareParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class VersionDeclareConverter extends BusinessParamConverter<NodeOpt> {
	
    @Override
    public NodeOpt convert(CollectionEvent event, Transaction tx) {
        VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();


        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return null;
    }
}
