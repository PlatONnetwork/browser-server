package com.platon.browser.complement.service;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.complement.service.param.BlockParameterService;
import com.platon.browser.queue.collection.event.CollectionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 周期切换业务服务类
 */
@Slf4j
@Service
public class ComplementEpochService {

    @Autowired
    private BlockParameterService parameterService;

    public List<BusinessParam> getParameters(CollectionEvent event) {
        List<BusinessParam> businessParams = parameterService.getParameter(event);
        return businessParams;
    }
}
