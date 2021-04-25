package com.platon.browser.data;

import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Transfer;
import com.platon.utils.Convert.Unit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @title  suicide()已弃用, 请使用 selfdestruct()
 * @description: 
 * @author: hudenian
 * @create: 2019/12/27
 */
public class SuicideAndSelfdestructTest extends BaseContractTest {

	private Logger logger = LoggerFactory.getLogger(SuicideAndSelfdestructTest.class);
	

    @Test
    public void selfKill() {
        try {

            SuicideAndSelfdestruct suicideAndSelfdestruct = SuicideAndSelfdestruct.deploy(web3j, transactionManager, gasProvider,chainId).send();

            String contractAddress = suicideAndSelfdestruct.getContractAddress();
            TransactionReceipt tx = suicideAndSelfdestruct.getTransactionReceipt().get();


            Transfer transfer = new Transfer(web3j, transactionManager);
            transfer.sendFunds(contractAddress, BigDecimal.TEN, Unit.KPVON,GAS_PRICE,GAS_LIMIT).send();
            
            TransactionReceipt transactionReceipt = suicideAndSelfdestruct.increment().send();

            logger.debug("SuicideAndSelfdestructTest increment successful.transactionHash:" + transactionReceipt.getTransactionHash());
            logger.debug( "currentBlockNumber:" + transactionReceipt.getBlockNumber());

//            String count = suicideAndSelfdestruct.getCount().send().toString();
//
//            logger.debug("链上的count值为："+count);

            //调用自杀函数
            TransactionReceipt transactionReceipt1 = suicideAndSelfdestruct.kill().send();

            logger.debug("SuicideAndSelfdestructTest kill successful.transactionHash:" + transactionReceipt1.getTransactionHash());
            logger.debug( "currentBlockNumber:" + transactionReceipt1.getBlockNumber());


            BigInteger count1 = suicideAndSelfdestruct.getCount().send();

            logger.debug("自杀后链上的count值为："+count1);

        } catch (Exception e) {
            if(e.getMessage().startsWith("Empty")){
            	logger.debug("自杀后查询链上的count值为 Empty");
            }
            logger.debug(e.toString(), "ContractCallException");
        }
    }


    /**
     * @title do...while结果值
     * @description: 
     * @author: hudenian
     * @create: 2019/12/27
     */
    public static String dowhile(int x){
        int y = x+10;
        int z = x+9;
        do{
            x+=1;
            if(x>z) continue;
        }while (x<y);
        return  String.valueOf(x);
    }

    /**
     * @title for 循环后的结果值
     * @description: 
     * @author: hudenian
     * @create: 2019/12/27
     */
    public static String forsum(int x){
        int forSum = 0;
        for(int i=0;i<x;i++){
            forSum = forSum +i;
        }
        return  String.valueOf(forSum);
    }

}
