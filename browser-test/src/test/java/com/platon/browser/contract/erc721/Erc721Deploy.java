package com.platon.browser.contract.erc721;

import com.alibaba.fastjson.JSON;
import com.platon.browser.v0152.contract.Erc721Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Erc721Deploy extends Erc721Base {
    private Integer deployCount = 10;
    // 部署合约
    @Test
    public void deploy() throws Exception {
        for(int i =0;i<=deployCount;i++){
            String str =randomStr();
            Erc721Contract contract = Erc721Contract.deploy(WEB3J,adminWallet,gasProvider,"name-"+str,"symbol-"+str).send();
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
