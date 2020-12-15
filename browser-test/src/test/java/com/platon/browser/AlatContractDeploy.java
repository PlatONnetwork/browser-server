package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.contract.AlatContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 部署Alat合约
 */
@Slf4j
public class AlatContractDeploy extends ContractTestBase {

    @Test
    public void deploy() throws Exception {
        for(int i =3;i<=3;i++){
            AlatContract contract = AlatContract.deploy(
                    web3j,
                    ownerWallet,
                    gasProvider,
                    chainId,
                    new BigInteger("500000000000000000000000000"),
                    "aLAT-1"+i,
                    BigInteger.valueOf(16L),
                    "aLAT-1"+i ,
                    ownerWallet.getAddress()
            ).send();
            try{
                FileUtils.write(contractList,contract.getContractAddress()+"\n", "UTF-8",true);
            }catch (IOException e){
                log.error("",e);
            }
            log.info(JSON.toJSONString(contract,true));
        }
    }
}
