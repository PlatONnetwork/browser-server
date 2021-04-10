package com.platon.browser.contract.erc20;

import com.alibaba.fastjson.JSON;
import com.platon.browser.v0152.contract.Erc20Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 部署Alat合约
 */
@Slf4j
public class Erc20Deploy extends Erc20Base {
    private Integer deployCount = 10;
    // 部署合约
    @Test
    public void deploy() throws Exception {
        for(int i =0;i<=deployCount;i++){
            log.error("adminWallet balance:{}",getBalance(adminWallet.getAddress()));
            String str = randomStr();
            Erc20Contract contract = Erc20Contract.deploy(
                    WEB3J,
                    adminWallet,
                    gasProvider,
                    new BigInteger("10000000000000000000"),
                    "name-"+str,
                    BigInteger.valueOf(16L),
                    "symbol-"+str,
                    adminWallet.getAddress()
            ).send();
            try{
                FileUtils.write(new File(contractAddressFilePath),contract.getContractAddress()+"\n", "UTF-8",true);
            }catch (IOException e){
                log.error("",e);
            }
            log.info(JSON.toJSONString(contract,true));
        }
    }
}
