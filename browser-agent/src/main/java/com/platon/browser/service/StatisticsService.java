package com.platon.browser.service;

import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChainResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 21:46
 */
@Component
public class StatisticsService {

    public void addressInfoUpdate(List <CustomBlock> basicData, BlockChainResult bizData){
        bizData.getAddressExecuteResult().getAddAddress();
        bizData.getAddressExecuteResult().getUpdateAddress();
    }

    public void statisticsUpdate(List<CustomBlock> basicData, BlockChainResult bizData){}
}
