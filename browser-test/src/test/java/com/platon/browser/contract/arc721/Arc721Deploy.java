package com.platon.browser.contract.arc721;

import com.alibaba.fastjson.JSON;
import com.platon.browser.v0151.contract.Erc721Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Arc721Deploy extends Arc721Base {
    private Integer deployCount = 10;
    // 部署合约
    @Test
    public void deploy() throws Exception {
        for(int i =0;i<=deployCount;i++){
            log.error("adminWallet balance:{}",getBalance(adminWallet.getAddress()));
            String str =randomStr();
            Erc721Contract contract = Erc721Contract.deploy(WEB3J,adminWallet,gasProvider,CHAIN_ID,"name-"+str,"symbol-"+str).send();
            log.info("合约地址：{}",contract.getContractAddress());
            try{
                FileUtils.write(new File(contractAddressFilePath),contract.getContractAddress()+"\n", "UTF-8",true);
            }catch (IOException e){
                log.error("",e);
            }
            log.info(JSON.toJSONString(contract,true));
        }
    }
}
