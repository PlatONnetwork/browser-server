package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.delegate.DelegateCreate;
import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class DelegateCreateConverter extends BusinessParamConverter {

    @Override
    public BusinessParam convert(CollectionTransaction tx) {
        DelegateCreateParam txParam = tx.getTxParam(DelegateCreateParam.class);

        BusinessParam businessParam= DelegateCreate.builder()

                .build();
        BeanUtils.copyProperties(txParam,businessParam);
        updateNodeCache(HexTool.prefix(txParam.getNodeId()),txParam.getNodeName());
        return businessParam;
    }
}
