package com.platon.browser.complement.service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.complement.service.param.ParameterService;
import com.platon.browser.complement.service.supplement.SupplementService;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑服务类
 */
@Slf4j
@Service
public class BusinessService {

    @Autowired
    private SupplementService supplementService;
    @Autowired
    private ParameterService parameterService;

    public List<BusinessParam> getParameters(List<CollectionTransaction> transactions) {
        List<BusinessParam> businessParams = new ArrayList<>();
        if(transactions.isEmpty()) return businessParams;
        for (CollectionTransaction tx : transactions) {
            try{
                BusinessParam businessParam = parameterService.getParameter(tx);
                businessParams.add(businessParam);
            }catch (BusinessException e){
                log.debug("{}",e);
            }
            supplementService.supplement(tx);
        }
        return businessParams;
    }
}
