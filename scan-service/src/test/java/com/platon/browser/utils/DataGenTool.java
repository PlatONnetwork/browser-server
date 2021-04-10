package com.platon.browser.utils;
/*
package com.platon.browser.util;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestData;
import com.platon.browser.dao.entity.*;
import io.netty.util.internal.ConcurrentSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameter;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import rx.Subscription;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataGenTool extends TestData {
    private static final Logger logger = LoggerFactory.getLogger(DataGenTool.class);
    public static Web3j web3j;

    public static final void writeToFile(String chainId, TestDataFileNameEnum dataFileNameEnum, Collection data) {
        File dir = new File(testDataDir);
        if(!dir.exists()) dir.mkdirs();
        File file = new File(testDataDir+dataFileNameEnum.prefix+chainId+".json");
        if(file.exists()){
            file.delete();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(JSON.toJSONString(data,true));
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    */
/**
     * 生成节点数据
     * @param chainId
     * @param writeToFile
     * @return
     *//*

    public static List<NodeRanking> generateNode(String chainId, boolean writeToFile) {
        List<NodeRanking> nodes = new ArrayList<>();

        Random random = new Random();
        for(int i=1;i<=7;i++){
            NodeRanking node = new NodeRanking();
            node.setChainId(chainId);
            while (true){
                try {
                    String ip = i%2==0? IPGenTool.getForeignIp(): IPGenTool.getChinaIp();
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
            node.setChainId(chainId);
            node.setCreateTime(new Date());
            node.setElectionStatus(1);
            node.setAddress("0x"+UUID.randomUUID().toString().replace("-",""));
            node.setNodeId(NODE_IDS[random.nextInt(NODE_IDS.length)]+i);
            node.setUpdateTime(new Date());
            Org org = ORGS[random.nextInt(ORGS.length)];
            node.setOrgName(org.name);
            node.setOrgWebsite(org.website);
            node.setDeposit(String.valueOf(Math.abs(random.nextInt(20000))));
            node.setPort(Math.abs(random.nextInt(10000)));
            node.setJoinTime(new Date());
            node.setType(1);
            node.setRanking(i);
            node.setIsValid(1);
            node.setBeginNumber(Math.abs(Long.valueOf(random.nextInt(2000))));
            node.setEndNumber(Math.abs(Long.valueOf(random.nextInt(20000))));
            node.setBlockCount(Math.abs(Long.valueOf(random.nextInt(200000))));
            node.setIsValid(i%2==0?1:0);
            node.setRewardAmount(String.valueOf(Math.abs(random.nextInt(200000))));
            node.setProfitAmount(String.valueOf(Math.abs(random.nextInt(200000))));
            node.setBlockReward(String.valueOf(Math.abs(random.nextInt(200000))));

            double ratio = random.nextInt(10)/10;
            node.setRewardRatio(ratio);

            while (true){
                int logo = Math.abs(random.nextInt(43));
                if(logo>0){
                    String str = String.valueOf(logo);
                    node.setUrl(str);
                    break;
                }
            }

            nodes.add(node);
        }
        if(writeToFile) writeToFile(chainId, TestDataFileNameEnum.NODE,nodes);
        return nodes;
    }

    */
/**
     * 生成区块数据
     * @param chainId
     * @param writeToFile
     * @return
     *//*

    public static List<Block> generateBlock(String chainId, boolean writeToFile) {

        if(web3j==null){
            throw new RuntimeException("请先实例化web3j！");
        }

        Random random = new Random();
        Set<Block> data = new ConcurrentSet<>();
        BigInteger currentHeight = BigInteger.valueOf(1);
        Subscription subscription = web3j.catchUpToLatestBlockObservable(DefaultBlockParameter.valueOf(currentHeight),true)
        .subscribe(eblock -> {
            PlatonBlock.Block block = eblock.getBlock();
            if (block!=null){
                Block bean = new Block();
                BeanUtils.copyProperties(block,bean);

                bean.setNumber(block.getNumber().longValue());
                bean.setTimestamp(new Date(block.getTimestamp().longValue()));
                bean.setChainId(chainId);
                bean.setTransactionNumber(block.getTransactions().size());
                bean.setBlockReward(String.valueOf(Math.abs(random.nextInt(200000))));

                bean.setNonce(block.getNonce().toString());
                bean.setSize(block.getSize().intValue());
                bean.setEnergonUsed(block.getGasUsed().toString());
                bean.setEnergonLimit(block.getGasLimit().toString());
                bean.setEnergonAverage(String.valueOf(Math.abs(random.nextInt(200000))));
                bean.setActualTxCostSum(String.valueOf(Math.abs(random.nextInt(200000))));
                bean.setBlockCampaignAmount(Math.abs(random.nextLong()));
                bean.setBlockVoteAmount(Math.abs(random.nextLong()));
                bean.setBlockVoteNumber(Math.abs(random.nextLong()));
                bean.setNodeName("Node-"+random.nextDouble());
                bean.setNodeId(NODE_IDS[random.nextInt(NODE_IDS.length)]);
                data.add(bean);
            }
        });

        try {
            TimeUnit.SECONDS.sleep(2);
            subscription.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Block> returnData = new ArrayList<>(data);
        if(writeToFile) writeToFile(chainId, TestDataFileNameEnum.BLOCK,returnData);
        return returnData;
    }

    */
/**
     * 生成交易数据
     * @param chainId
     * @param writeToFile
     * @return
     *//*

    public static List<TransactionWithBLOBs> generateTransactionWithBLOB(String chainId, boolean writeToFile) {

        if(web3j==null){
            throw new RuntimeException("请先实例化web3j！");
        }

        Set<TransactionWithBLOBs> data = new ConcurrentSet<>();
        BigInteger currentHeight = BigInteger.valueOf(1);
        Subscription subscription = web3j.catchUpToLatestTransactionObservable(DefaultBlockParameter.valueOf(currentHeight))
                .subscribe(transaction -> {
                    TransactionWithBLOBs bean = new TransactionWithBLOBs();
                    BeanUtils.copyProperties(transaction,bean);

                    bean.setChainId(chainId);

                    BigInteger value = ConvertUtil.hexToBigInteger(transaction.getValue().toString());
                    bean.setValue(value.toString());
                    bean.setSequence(Long.valueOf(data.size()));
                    bean.setTimestamp(new Date(System.currentTimeMillis()));
                    Random random = new Random();
                    bean.setActualTxCost(String.valueOf(Math.abs(random.nextInt(200000))));
                    try {
                        PlatonBlock eblock = web3j.ethGetBlockByHash(transaction.getBlockHash(),false).send();
                        bean.setBlockNumber(eblock.getBlock().getNumber().longValue());
                        bean.setEnergonLimit(eblock.getBlock().getGasLimit().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bean.setEnergonUsed(transaction.getGas().toString());
                    bean.setInput(String.valueOf(Math.abs(random.nextInt(2000000))));
                    bean.setFailReason(String.valueOf(Math.abs(random.nextInt(2000000))));
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
        List<TransactionWithBLOBs> returnData = new ArrayList<>(data);
        if(writeToFile) writeToFile(chainId, TestDataFileNameEnum.TRANSACTION,returnData);
        return returnData;
    }

    public static List<PendingTx> generatePendingTx(String chainId, boolean writeToFile) {
        List<PendingTx> returnData = new ArrayList<>();
        Random random = new Random();
        for (int i=0;i<50;i++){
            PendingTx bean = new PendingTx();
            bean.setChainId(chainId);
            bean.setEnergonLimit(String.valueOf(Math.abs(random.nextInt(500000))));
            bean.setEnergonUsed(String.valueOf(Math.abs(random.nextInt(500000))));
            bean.setEnergonPrice(String.valueOf(Math.abs(random.nextInt(200000))));
            bean.setHash("0x"+UUID.randomUUID().toString().replace("-",""));
            bean.setValue(String.valueOf(Math.abs(random.nextInt(2000000))));
            bean.setTimestamp(new Date());
            bean.setCreateTime(new Date());
            bean.setUpdateTime(new Date());
            bean.setFrom("0x11c5a274ef2a924f59e7182abc7c031206cdcf66");
            bean.setTo("0x788641dc03b80240207f04bb1f8be5e10269ab8f");
            bean.setReceiveType("account");
            bean.setInput("8888888");

            if(i%2==0){
                bean.setTxType("voteTicket");
                bean.setTxInfo("{\"functionName\":\"VoteTicket\",\"parameters\":{\"price\":\"1000000000000000000\",\"count\":\"105\",\"nodeId\":\"fa147bc3625acc846a9f0e1e89172ca7470baa0f86516994f70860c6fb904ddbb1849e3cf2b40c58255e38401f40d2c3e4a3bd5c2f2849b98465a5bdb80ed6a0\"},\"type\":\"1000\"}");
            }else
            if(i%3==0){
                bean.setTxType("candidateApplyWithdraw");
                bean.setTxInfo("{\"functionName\":\"CandidateApplyWithdraw\",\"parameters\":{\"nodeId\":\"0xfa147bc3625acc846a9f0e1e89172ca7470baa0f86516994f70860c6fb904ddbb1849e3cf2b40c58255e38401f40d2c3e4a3bd5c2f2849b98465a5bdb80ed6a0\",\"withdraw\":\"999999999999999983222784\"},\"type\":\"1002\"}");
            }else
            if(i%5==0){
                bean.setTxType("candidateWithdraw");
                bean.setTxInfo("{\"functionName\":\"CandidateWithdraw\",\"parameters\":{\"nodeId\":\"0xfa147bc3625acc846a9f0e1e89172ca7470baa0f86516994f70860c6fb904ddbb1849e3cf2b40c58255e38401f40d2c3e4a3bd5c2f2849b98465a5bdb80ed6a0\"},\"type\":\"1003\"}");
            }else
            if(i%7==0){
                bean.setTxType("candidateDeposit");
                bean.setTxInfo("{\"functionName\":\"CandidateDeposit\",\"parameters\":{\"owner\":\"0x3b36c8cf428d15e7725affcc095d086cb2ecc754\",\"Extra\":\"{\\\"nodeName\\\":\\\"nodehuiyu\\\",\\\"nodePortrait\\\":\\\"01\\\",\\\"nodeDiscription\\\":\\\"\\\\u975e\\\\u5171\\\\u8bc6\\\\u8282\\\\u70b9\\\\u7684\\\\u66ff\\\\u6362\\\\uff0c\\\\u8d28\\\\u62bc\\\\u548c\\\\u6295\\\\u7968\\\\u64cd\\\\u4f5c11111111\\\",\\\"nodeDepartment\\\":\\\"\\\\u673a\\\\u6784111\\\",\\\"officialWebsite\\\":\\\"https://www.baidu.com\\\",\\\"time\\\":1550631713828}\",\"port\":\"16789\",\"fee\":\"2500\",\"host\":\"192.168.120.89\",\"nodeId\":\"0xfa147bc3625acc846a9f0e1e89172ca7470baa0f86516994f70860c6fb904ddbb1849e3cf2b40c58255e38401f40d2c3e4a3bd5c2f2849b98465a5bdb80ed6a0\"},\"type\":\"1001\"}");
            }else {
                bean.setTxType("transfer");
                bean.setTxInfo("{\"parameters\":{}}");
            }

            if(i%9==0){
                bean.setTxType("contractCreate");
                bean.setTxInfo("{\"functionName\":\"contract deploy\",\"parameters\":{},\"type\":\"1\"}");
            }

            returnData.add(bean);
        }
        if(writeToFile) writeToFile(chainId, TestDataFileNameEnum.PENDINGTX,returnData);
        return returnData;
    }

    public static List<Transaction> generateTransaction(String chainId, boolean writeToFile) {
        List<TransactionWithBLOBs> data = generateTransactionWithBLOB(chainId,writeToFile);
        List<Transaction> returnData = new ArrayList<>();
        data.forEach(e->{
            Transaction bean = new Transaction();
            BeanUtils.copyProperties(e,bean);
            returnData.add(bean);
        });
        return returnData;
    }

    public static <T> List<T> getTestData(String chainId, TestDataFileNameEnum dataFileNameEnum, Class<T> clazz) throws IOException {
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
        // generateNode("1",true);
        generatePendingTx("1",true);
        //generateTransaction("1",true);
        //generateBlock("1",true);
    }
}
*/
