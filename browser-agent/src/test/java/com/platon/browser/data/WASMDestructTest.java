package com.platon.browser.data;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert.Unit;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @title  suicide()已弃用, 请使用 selfdestruct()
 * @description: 
 * @author: hudenian
 * @create: 2019/12/27
 */
public class WASMDestructTest extends BaseContractTest {

	private Logger logger = LoggerFactory.getLogger(WASMDestructTest.class);
	

    @Test
    public void selfKill() {
        try {

            Destory_contract destory_contract = Destory_contract.deploy(web3j, transactionManager, gasProvider,"0x60ceca9c1290ee56b98d4e160ef0453f7c40d219").send();

            String contractAddress = destory_contract.getContractAddress();
            TransactionReceipt tx = destory_contract.getTransactionReceipt().get();


            Transfer transfer = new Transfer(web3j, transactionManager);
            transfer.sendFunds(contractAddress, BigDecimal.TEN, Unit.LAT,GAS_PRICE,GAS_LIMIT).send();
            

            //调用自杀函数
            TransactionReceipt transactionReceipt1 = destory_contract.destroy().send();

            logger.debug("destory_contract kill successful.transactionHash:" + transactionReceipt1.getTransactionHash());
            logger.debug( "currentBlockNumber:" + transactionReceipt1.getBlockNumber());


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
