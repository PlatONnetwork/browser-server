package com.platon.browser.v0152.contract;

import com.platon.bech32.Bech32;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.http.HttpService;
import com.platon.tx.ReadonlyTransactionManager;
import com.platon.tx.gas.DefaultGasProvider;
import org.junit.Test;
public class PScanQueryFacadeContractTest {

    @Test
    public void ercInfo() throws Exception {
        PScanQueryFacadeContract contract = load();
        System.out.println(contract.ercInfo(Bech32.addressEncode("lat", "0x46195B2E97a0E3d097c2DEA20313B2C2C58ac6EC")).send());
    }


    private PScanQueryFacadeContract load(){
        NetworkParameters.init(2022041901L, "lat");
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.146:6789"));
        return PScanQueryFacadeContract.load("lat1z2keqk9gynqx72d287h8we0cn0thcmq9e6efvl", web3j, new ReadonlyTransactionManager(web3j, "lat1z2keqk9gynqx72d287h8we0cn0thcmq9e6efvl"), new DefaultGasProvider());
    }
}