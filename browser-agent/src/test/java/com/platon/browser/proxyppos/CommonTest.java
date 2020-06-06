package com.platon.browser.proxyppos;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class CommonTest extends TestBase {
    /**
     * TODO: INIT-STEP-02 给proxyStakingContractAddress和proxyDelegateContractAddress转账
     * 让这两个合约有足够的钱可以代理发起PPOS操作
     * 给delegateCredentials转账
     * @throws Exception
     */
    @Test
    public void transfer() throws Exception {

        TransactionManager transactionManager = new RawTransactionManager(defaultWeb3j, defaultCredentials, chainId);
        new Transfer(defaultWeb3j,transactionManager).sendFunds(proxyStakingContractAddress,new BigDecimal("50000000"), Convert.Unit.LAT,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ defaultWeb3j.platonGetBalance(proxyStakingContractAddress, DefaultBlockParameterName.LATEST).send().getBalance());

        new Transfer(defaultWeb3j,transactionManager).sendFunds(proxyDelegateContractAddress,new BigDecimal("50000000"), Convert.Unit.LAT,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ defaultWeb3j.platonGetBalance(proxyDelegateContractAddress, DefaultBlockParameterName.LATEST).send().getBalance());

        String address = delegateCredentials.getAddress(chainId);
        new Transfer(defaultWeb3j,transactionManager).sendFunds(address,new BigDecimal("50000000"), Convert.Unit.LAT,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ defaultWeb3j.platonGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance());
    }

    @Test
    public void blockNumber() throws Exception {
        BigInteger blockNumber = defaultWeb3j.platonBlockNumber().send().getBlockNumber();
        System.out.println("Current Block Number:"+blockNumber);
    }

    /**
     * 解码解委托返回的回执日志数据
     */
    @Test
    public void decodeUnDelegateReceiptLog() throws IOException {

        TransactionReceipt receipt = defaultWeb3j.platonGetTransactionReceipt("0xe101b5758e0c3e132ab6616412395f54a4534609341a4c6723356520088d2be2")
                .send().getTransactionReceipt().get();
        System.out.println(JSON.toJSONString(receipt.getLogs(),true));
//        String data0 = "0xc3308180";
//        String data1 = "0xc786333031313039";
//        String data2 = "0x000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000001300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000063330313130390000000000000000000000000000000000000000000000000000";

        String data0 = "0xc3308180";
        String data1 = "0xc786333031313039";
        String data2 = "0x000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000001300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000063330313130390000000000000000000000000000000000000000000000000000";


        decodeLogForReward(data0);
        decodeLogForReward(data1);
        decodeLogForReward(data2);

    }

    private void decodeLogForReward(String data){
        try{
            RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
            List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
            String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
            int statusCode = Integer.parseInt(decodedStatus);
            if(statusCode!=0){
                String error = ERRORS.getProperty(String.valueOf(statusCode));
                System.out.println("=================================");
                System.out.println("输入:"+data);
                System.out.println("结果:交易出错【"+error+"】");
                return;
            }
            try{
                BigInteger reward = ((RlpString)(RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())).getValues().get(0)).asPositiveBigInteger();
                System.out.println("=================================");
                System.out.println("输入:"+data);
                System.out.println("结果:解码金额成功【"+reward+"】");
            }catch (Exception e){
                System.out.println("=================================");
                System.out.println("输入:"+data);
                System.out.println("结果:解码金额出错【"+e.getMessage()+"】");
            }
        }catch (Exception e){
            System.out.println("=================================");
            System.out.println("输入:"+data);
            System.out.println("结果:Rlp解码出错【"+e.getMessage()+"】");
        }


    }
}
