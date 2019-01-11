package com.platon.browser.util;

import com.github.fartherp.framework.common.extension.ConcurrentHashSet;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import com.platon.browser.common.util.ConvertUtil;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import rx.Subscription;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestDataUtil.class);
    public final static Web3j web3j = Web3j.build(new HttpService("http://192.168.9.76:6788"));

    public static String getForeignRandomIp(){
        int a = 108 * 256 * 256 + 1 * 256 + 5;
        int b = 145 * 256 * 256 + 110 * 256 + 35;
        int c = new Random().nextInt(b - a) + a;
        String ip = "192." + (c / (256 * 256)) + "." + ((c / 256) % 256) + "." + (c % 256);
        return ip;
    }

    public static String getChinaRandomIp() {

        // ip范围
        int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
                { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
                { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
                { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
                { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
                { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
                { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
                { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
                { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };

        Random rdint = new Random();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }


    public static List<NodeRanking> generateNode(String chainId){
        List<NodeRanking> nodes = new ArrayList<>();

        for(int i=0;i<200;i++){
            NodeRanking node = new NodeRanking();
            node.setChainId(chainId);
            while (true){
                try {
                    String ip = TestDataUtil.getForeignRandomIp();
                    if(i%2==0){
                        ip = TestDataUtil.getChinaRandomIp();
                    }
                    //logger.info("IP: {}",ip);

                    CityResponse response = GeoUtil.getResponse(ip);
                    Location location = response.getLocation();
                    Country country = response.getCountry();
                    Subdivision subdivision = response.getMostSpecificSubdivision();
                    City city = response.getCity();
                    /*logger.info("国家：{}", country.getName());
                    logger.info("城市：{}", city.getName());
                    logger.info("纬度：{}", location.getLatitude());
                    logger.info("经度：{}", location.getLongitude());
                    logger.info("dma code：{}", country.getIsoCode());
                    logger.info("area code：{}", subdivision.getIsoCode());
                    logger.info("country code：{}", country.getIsoCode());*/
                    node.setIntro(country.getName().replace(" ",""));
                    if(StringUtils.isNotBlank(city.getName())){
                        node.setIntro(node.getIntro()+"."+city.getName().replace(" ",""));
                    }
                    node.setIp(ip);
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
            node.setUrl("https://www.platon.network/");

            nodes.add(node);
        }
        return nodes;
    }

    public static List<Block> generateBlock(String chainId){
        Set<Block> data = new ConcurrentHashSet<>();
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
        return blocks;
    }

    public static List<TransactionWithBLOBs> generateTransactionWithBLOB(String chainId) {
        Set<TransactionWithBLOBs> data = new ConcurrentHashSet<>();
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

                    data.add(bean);
                });

        try {
            TimeUnit.SECONDS.sleep(2);
            subscription.unsubscribe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<TransactionWithBLOBs> transactions = new ArrayList<>(data);
        return transactions;
    }

    public static List<Transaction> generateTransaction(String chainId) {
        List<TransactionWithBLOBs> data = generateTransactionWithBLOB(chainId);
        List<Transaction> set = new ArrayList<>();
        data.forEach(e->{
            Transaction bean = new Transaction();
            BeanUtils.copyProperties(e,bean);
            set.add(bean);
        });
        return set;
    }
}
