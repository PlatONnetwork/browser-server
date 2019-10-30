package com.platon.browser.old.task.bean;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/9 18:30
 * @Description:
 */
public class TransactionBean extends CustomTransaction {
    private String root;
    public void setRoot(String str){
        this.root=str;
    }
    public String getRoot(){return root;}
    private String cumulativeGasUsed;
    public void setCumulative_gas_used(String str){
        this.cumulativeGasUsed=str;
    }
    public String getCumulativeGasUsed(){return cumulativeGasUsed;}
    private String logsBloom;
    public void setLogs_bloom(String str){
        this.logsBloom=str;
    }
    public String getLogsBloom(){return logsBloom;}
    private List<Log> logs;
    public void setLogs(String logs){
        this.logs = JSON.parseArray(logs,Log.class);
    }
    public List<Log> getLogs(){return logs;}

    @Override
    public void setGasUsed(String gasUsed) {
        super.setGasUsed(Numeric.encodeQuantity(new BigInteger(gasUsed)));
    }
}
