package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.util.TransactionAnalysis;
import com.platon.browser.utils.FilterTool;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Date;
import java.util.Random;

public class PendingBean extends PendingTx{
    public void initData(Transaction initData){
        BeanUtils.copyProperties(initData,this);
        this.setEnergonLimit(initData.getGas().toString());
        this.setEnergonPrice(initData.getGasPrice().toString());
        this.setTimestamp(new Date());
        this.setValue(FilterTool.valueConversion(initData.getValue()));
        this.setInput(initData.getInput());
        AnalysisResult analysisResult = new AnalysisResult();
        try {
            analysisResult = TransactionAnalysis.analysis(!initData.getInput().equals(null) ? initData.getInput() : "0x", false);
            String type = TransactionAnalysis.getTypeName(analysisResult.getType());
            this.setTxType(type);
            String txinfo = JSON.toJSONString(analysisResult);
            this.setTxInfo(txinfo);
        }catch (Exception e){
            this.setTxType("unknown");
            this.setTxInfo("{}");
            e.getMessage();
        }

    }
}
