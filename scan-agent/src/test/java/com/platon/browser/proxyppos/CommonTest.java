package com.platon.browser.proxyppos;

import com.alibaba.fastjson.JSON;
import com.platon.abi.wasm.WasmFunctionEncoder;
import com.platon.abi.wasm.datatypes.WasmFunction;
import com.platon.contracts.ppos.RewardContract;
import com.platon.contracts.ppos.dto.resp.Reward;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;
import com.platon.tx.Transfer;
import com.platon.utils.Convert;
import com.platon.utils.Numeric;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
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

        TransactionManager transactionManager = new RawTransactionManager(defaultWeb3j, defaultCredentials);
        new Transfer(defaultWeb3j,transactionManager).sendFunds(proxyStakingContractAddress,new BigDecimal("50000000"), Convert.Unit.KPVON,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ defaultWeb3j.platonGetBalance(proxyStakingContractAddress, DefaultBlockParameterName.LATEST).send().getBalance());

        new Transfer(defaultWeb3j,transactionManager).sendFunds(proxyDelegateContractAddress,new BigDecimal("50000000"), Convert.Unit.KPVON,GAS_PRICE,GAS_LIMIT).send();
        System.out.println("balance="+ defaultWeb3j.platonGetBalance(proxyDelegateContractAddress, DefaultBlockParameterName.LATEST).send().getBalance());

        String address = delegateCredentials.getAddress();
        new Transfer(defaultWeb3j,transactionManager).sendFunds(address,new BigDecimal("50000000"), Convert.Unit.KPVON,GAS_PRICE,GAS_LIMIT).send();
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
    public void claimRewardList() throws Exception {

        //SpecialApi specialApi = new SpecialApi();
        //List<PPosInvokeContractInput> pposInfo = specialApi.getPPosInvokeInfo(defaultWeb3j,BigInteger.valueOf(42305));


        String nodeId1 = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
        String nodeId2 = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";

        RewardContract rewardContract = RewardContract.load(defaultWeb3j);
        List<Reward> rewards = rewardContract.getDelegateReward(
        "lax1ufjvfyxxxy6q3j5ayth97pcrn9pn475swqed9h",
                Arrays.asList(nodeId1,nodeId2)
        ).send().getData();
        //6489731995654434982104
        System.out.println(rewards);



        String data = "";
        String pubkey = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
        WasmFunction wasmFunction = new WasmFunction("record", Arrays.asList(pubkey), Void.class);
        data = WasmFunctionEncoder.encode(wasmFunction);

//        Transaction transaction = Transaction.createEthCallTransaction(proxyDelegateContractAddress, proxyDelegateContractAddress, data);
//        PlatonEstimateGas platonEstimateGas = defaultWeb3j.platonEstimateGas(transaction).send();
//        BigInteger amount = platonEstimateGas.getAmountUsed();
//        System.out.println(amount);
    }

    /**
     * 解码解委托返回的回执日志数据
     */
    @Test
    public void decodeUnDelegateReceiptLog() throws IOException {

        TransactionReceipt receipt = defaultWeb3j.platonGetTransactionReceipt("0xe101b5758e0c3e132ab6616412395f54a4534609341a4c6723356520088d2be2")
                .send().getTransactionReceipt().get();
        System.out.println(JSON.toJSONString(receipt.getLogs()));
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
