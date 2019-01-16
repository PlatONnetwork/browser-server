package com.platon.browser.util;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestData;
import com.platon.browser.common.util.ConvertUtil;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import io.netty.util.internal.ConcurrentSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Subscription;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataGenTool extends TestData {
    private static final Logger logger = LoggerFactory.getLogger(DataGenTool.class);
    public static Web3j web3j;

    public static final void writeToFile(String chainId, TestDataFileNameEnum dataFileNameEnum, Collection data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(testDataDir+dataFileNameEnum.prefix+chainId+".json"));
        writer.write(JSON.toJSONString(data));
        writer.flush();
        writer.close();
    }

    public static List<NodeRanking> generateNode(String chainId, boolean writeToFile) {
        List<NodeRanking> nodes = new ArrayList<>();

        for(int i=0;i<200;i++){
            NodeRanking node = new NodeRanking();
            node.setChainId(chainId);
            while (true){
                try {
                    String ip = IPGenTool.getForeignRandomIp();
                    if(i%2==0){
                        ip = IPGenTool.getChinaRandomIp();
                    }

                    GeoUtil.IpLocation location = GeoUtil.getIpLocation(ip);
                    if(StringUtils.isBlank(location.getCountryCode())){
                        continue;
                    }
                    node.setIntro(location.getLocation());
                    node.setCountryCode(location.getCountryCode());
                    node.setLatitude(location.getLatitude());
                    node.setLongitude(location.getLongitude());
                    node.setLocation(location.getLocation());
                    node.setIp(ip);
                    node.setName(location.getLocation());
                    break;
                }catch (Exception e){}
            }
            node.setChainId("1");
            node.setCreateTime(new Date());
            node.setElectionStatus(1);
            node.setAddress("0x493301712671ada506ba6ca7891f436d2918582"+i);
            node.setNodeId("0x1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429");
            node.setUpdateTime(new Date());
            node.setOrgName("platon");
            node.setOrgWebsite("https://www.platon.network/");
            node.setDeposit("87854");
            node.setPort(808+i);
            node.setJoinTime(new Date());
            node.setType(1);
            node.setRanking(i);
            node.setIsValid(1);
            node.setBeginNumber(1l);
            node.setEndNumber(199l);
            node.setBlockCount(55l);

            Random random = new Random();
            while (true){
                int logo = random.nextInt(43);
                if(logo>0){
                    String str = String.valueOf(logo);
                    if(str.length()==1){
                        str = "0"+str;
                    }
                    node.setUrl(str);
                    break;
                }
            }

            nodes.add(node);
        }
        if(writeToFile) {
            try {
                writeToFile(chainId,TestDataFileNameEnum.NODE,nodes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    public static List<Block> generateBlock(String chainId, boolean writeToFile) {

        if(web3j==null){
            throw new RuntimeException("请先实例化web3j！");
        }

        Set<Block> data = new ConcurrentSet<>();
        BigInteger currentHeight = BigInteger.valueOf(1);
        Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter.valueOf(currentHeight),true)
        .subscribe(eblock -> {
            EthBlock.Block block = eblock.getBlock();
            if (block!=null){
                Block bean = new Block();
                BeanUtils.copyProperties(block,bean);

                bean.setNumber(block.getNumber().longValue());
                bean.setTimestamp(new Date(block.getTimestamp().longValue()));
                bean.setChainId(chainId);
                bean.setTransactionNumber(block.getTransactions().size());
                bean.setBlockReward("0.265");

                bean.setNonce(block.getNonce().toString());
                bean.setSize(block.getSize().intValue());
                bean.setEnergonUsed(block.getGasUsed().toString());
                bean.setEnergonLimit(block.getGasLimit().toString());
                bean.setEnergonAverage("0.2365");
                bean.setActualTxCostSum("33434");
                bean.setBlockCampaignAmount(3333l);
                bean.setBlockVoteAmount(3333l);
                bean.setBlockVoteNumber(432l);

                data.add(bean);
            }
        });

        try {
            TimeUnit.SECONDS.sleep(2);
            subscription.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Block> blocks = new ArrayList<>(data);
        if(writeToFile) {
            try {
                writeToFile(chainId,TestDataFileNameEnum.BLOCK,blocks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    public static List<TransactionWithBLOBs> generateTransactionWithBLOB(String chainId, boolean writeToFile) {

        if(web3j==null){
            throw new RuntimeException("请先实例化web3j！");
        }

        Set<TransactionWithBLOBs> data = new ConcurrentSet<>();
        BigInteger currentHeight = BigInteger.valueOf(1);
        Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter.valueOf(currentHeight))
                .subscribe(transaction -> {
                    TransactionWithBLOBs bean = new TransactionWithBLOBs();
                    BeanUtils.copyProperties(transaction,bean);

                    bean.setChainId(chainId);

                    BigInteger value = ConvertUtil.hexToBigInteger(transaction.getValue().toString());
                    bean.setValue(value.toString());
                    bean.setSequence(Long.valueOf(data.size()));
                    bean.setTimestamp(new Date(System.currentTimeMillis()));
                    bean.setActualTxCost("0.23566");
                    try {
                        EthBlock eblock = web3j.ethGetBlockByHash(transaction.getBlockHash(),false).send();
                        bean.setBlockNumber(eblock.getBlock().getNumber().longValue());
                        bean.setEnergonLimit(eblock.getBlock().getGasLimit().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bean.setEnergonUsed(transaction.getGas().toString());
                    bean.setInput("33434");
                    bean.setFailReason("4545433");
                    bean.setTxType("transfer");
                    bean.setEnergonPrice(transaction.getGasPrice().toString());
                    bean.setNonce(transaction.getNonce().toString());
                    bean.setTransactionIndex(transaction.getTransactionIndex().intValue());
                    bean.setTxReceiptStatus(1);
                    bean.setReceiveType("account");

                    bean.setTxInfo("{\"type\":\"1\",\"functionName\":\"getData\"}");

                    if(StringUtils.isBlank(bean.getTo())){
                        bean.setTo("0x");
                    }

                    data.add(bean);
                });

        try {
            TimeUnit.SECONDS.sleep(2);
            subscription.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<TransactionWithBLOBs> transactions = new ArrayList<>(data);
        if(writeToFile) {
            try {
                writeToFile(chainId,TestDataFileNameEnum.TRANSACTION,transactions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    public static List<Transaction> generateTransaction(String chainId, boolean writeToFile) {
        List<TransactionWithBLOBs> data = generateTransactionWithBLOB(chainId,writeToFile);
        List<Transaction> set = new ArrayList<>();
        data.forEach(e->{
            Transaction bean = new Transaction();
            BeanUtils.copyProperties(e,bean);
            set.add(bean);
        });
        return set;
    }

    public static <T> List<T> getTestData(String chainId,TestDataFileNameEnum dataFileNameEnum, Class<T> clazz) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(testDataDir+dataFileNameEnum.prefix+chainId+".json"));
        StringBuffer sb = new StringBuffer();
        reader.lines().forEach(line->sb.append(line));
        reader.close();
        if(sb.length()>0){
            List<T> data = JSON.parseArray(sb.toString(),clazz);
            return data;
        }
        return Collections.EMPTY_LIST;
    }

    public static void main(String[] args) {
        generateBlock("1",true);
        generateTransaction("1",true);
        generateNode("1",true);
    }
}
