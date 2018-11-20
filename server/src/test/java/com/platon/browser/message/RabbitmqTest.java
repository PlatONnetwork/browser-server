package com.platon.browser.message;

import com.platon.browser.ServiceApplication;
import com.platon.browser.common.dto.agent.BlockDto;
import com.platon.browser.common.dto.agent.NodeDto;
import com.platon.browser.common.dto.agent.PendingTransactionDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.common.enums.MqMessageTypeEnum;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.enums.TransactionTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class)
public class RabbitmqTest {

    @Autowired
    private MQSender sender;

    protected static Logger logger = LoggerFactory.getLogger(RabbitmqTest.class);

    @Test
    public void send() {


       /* for (int i = 0; i < 12; i++) {
            Node node = new Node();
            node.setChainId("ID-" + 1);
            node.setCreateTime(new Date());
            node.setIp("192.168.1.1" + i);
            node.setNodeName("dddddd");
            sender.send("1", MqMessageTypeEnum.NODE.name().toLowerCase(), node);
            System.out.println(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        Random r = new Random();
        while (true){
            int i = r.nextInt(100);
            if(i%2 == 0){
                NodeDto node = new NodeDto();
                node.setMiner("node-miner-"+i);
                node.setIp("14.215.177.39");
                node.setNodeName("node-name-"+i);
                node.setNodeId("node-id-"+i);
                node.setNodeAddress("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf"+i);
                sender.send("1", MqMessageTypeEnum.NODE.name().toLowerCase(), node);
            }
            if (i % 3 == 0) {
                BlockDto block = new BlockDto();
                block.setBlockReward("0.22222"+i);
                block.setEnergonAverage(BigInteger.valueOf(i));
                block.setEnergonLimit(BigInteger.valueOf(i));
                block.setEnergonUsed(BigInteger.valueOf(i));
                block.setExtraData("extradata-"+i);
                block.setHash("0x33333333"+i);
                block.setMiner("miner-"+i);
                block.setNonce(String.valueOf(System.currentTimeMillis()));
                block.setNumber(888883+i);
                block.setParentHash("548845"+i);
                block.setSize(2.6f);
                block.setTimestamp(System.currentTimeMillis());

                List<TransactionDto> transactionDtos = new ArrayList<>();
                TransactionDto transactionDto;

                for(int j=0;j<10;j++){
                    transactionDto = new TransactionDto();
                    transactionDto.setHash("0x1da4b53ae05c28e8ee1679f2681fab24ccdf327f94cb98e29760296a799dc02"+j);
                    transactionDto.setActualTxCoast(BigInteger.valueOf(j));
                    transactionDto.setBlockHash(block.getHash());
                    transactionDto.setBlockNumber(BigInteger.valueOf(block.getNumber()));
                    transactionDto.setEnergonLimit(block.getEnergonLimit());
                    transactionDto.setEnergonPrice(BigInteger.valueOf(i));
                    transactionDto.setEnergonUsed(block.getEnergonUsed().subtract(BigInteger.valueOf(j)));
                    transactionDto.setFrom("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf"+j);
                    transactionDto.setTo("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8Dab"+j);
                    transactionDto.setNonce(String.valueOf(System.currentTimeMillis()));
                    transactionDto.setInput("6545446474754654654654564");
                    transactionDto.setTimestamp(System.currentTimeMillis()+j);
                    transactionDto.setTxType(TransactionTypeEnum.TRANSFER.code);
                    transactionDto.setTransactionIndex(BigInteger.valueOf(j));
                    transactionDto.setValue("23.25"+j);
                    transactionDtos.add(transactionDto);
                }
                block.setTransaction(transactionDtos);
                sender.send("1", MqMessageTypeEnum.BLOCK.name().toLowerCase(), block);
            }

            if (i % 5 == 0) {
                PendingTransactionDto pendingTransactionDto = new PendingTransactionDto();
                pendingTransactionDto.setFrom("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf"+i);
                pendingTransactionDto.setTo("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8Dad"+i);
                pendingTransactionDto.setEnergonLimit(BigInteger.valueOf(i));
                pendingTransactionDto.setHash("0x33333333"+i);
                pendingTransactionDto.setInput("242544354354534354");
                pendingTransactionDto.setNonce(String.valueOf(System.currentTimeMillis()));
                pendingTransactionDto.setTimestamp(System.currentTimeMillis());
                pendingTransactionDto.setTxType(TransactionTypeEnum.TRANSFER.code);
                pendingTransactionDto.setValue("23.14"+i);
                sender.send("1", MqMessageTypeEnum.PENDING.name().toLowerCase(), pendingTransactionDto);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

