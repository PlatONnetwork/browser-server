package com.platon.browser.data;//package com.platon.browser.data;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.engine.bean.AnnualizedRateInfo;
//import com.platon.browser.engine.bean.PeriodValueElement;
//import com.platon.browser.enums.InnerContractAddrEnum;
//import org.bouncycastle.util.encoders.Hex;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.alaya.crypto.Credentials;
//import com.alaya.protocol.Web3j;
//import com.alaya.protocol.core.DefaultBlockParameter;
//import com.alaya.protocol.core.DefaultBlockParameterName;
//import com.alaya.protocol.http.HttpService;
//import com.alaya.tx.Transfer;
//import com.alaya.utils.Convert;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/8/15 20:58
// * @Description:
// */
//public class AccountTool {
//    private static Logger logger = LoggerFactory.getLogger(AccountTool.class);
////    private String chainId = "101";
//    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
////    private Credentials credentials = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");
//
//    private String chainId = "100";
////    private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
//    private Credentials credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
////    private Credentials credentials = WalletUtils.loadCredentials("88888888","D:\\Workspace\\browser-server\\browser-agent\\src\\test\\resources\\0127de1d120dc61b57ab51afcc0fa59022a1be94.json");
//
//    // 充钱
//    @Test
//    public void charge() throws Exception {
//        ExecutorService executor = Executors.newFixedThreadPool(100);
//
//        while (true){
//            CountDownLatch latch = new CountDownLatch(100);
//            for (int i=0;i<100;i++){
//                executor.submit(()->{
//                    try {
//                        Transfer.sendFunds(
//                                currentValidWeb3j,
//                                credentials,
//                                chainId,
//                                "0x0127de1d120dc61b57ab51afcc0fa59022a1be94",
//                                BigDecimal.valueOf(5),
//                                Convert.Unit.VON
//                        ).send();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//        }
//    }
//
//    //@Test
//    public void privateKey() throws UnsupportedEncodingException {
//        byte[] byteArray = credentials.getEcKeyPair().getPrivateKey().toByteArray();
//        String privateKey = Hex.toHexString(byteArray);
//
//        logger.debug("Private Key:{}",privateKey);
//    }
//
//
//    //@Test
//    public void test(){
//        String str = "{\"cost\":[{\"period\":73,\"value\":6000000999999999999999999},{\"period\":72,\"value\":6000000999999999999999999},{\"period\":71,\"value\":6000000999999999999999999},{\"period\":70,\"value\":6000000999999999999999999}],\"profit\":[{\"period\":73,\"value\":593134276272582179965699027},{\"period\":72,\"value\":586220162784294254205518035},{\"period\":71,\"value\":579306049296006328445337043},{\"period\":70,\"value\":572391935807718402685156051}]}";
//        AnnualizedRateInfo ari = JSON.parseObject(str, AnnualizedRateInfo.class);
//        BigDecimal cost = BigDecimal.ZERO;
//        BigDecimal profit = BigDecimal.ZERO;
//        for (PeriodValueElement pve:ari.getCost()){
//            cost = cost.add(new BigDecimal(pve.getValue()));
//        }
//        for (PeriodValueElement pve:ari.getProfit()){
//            profit = profit.add(new BigDecimal(pve.getValue()));
//        }
//
//        BigDecimal tmp = profit.divide(cost,16, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(14)).multiply(BigDecimal.valueOf(100)).setScale(2,RoundingMode.FLOOR);
//        System.out.println(tmp);
//    }
//
//
//    //@Test
//    public void getIncentiveBalance(){
//        // 激励池账户地址
//        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
//        String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress();
//        try {
//            // 根据激励池地址查询前一增发周期末激励池账户余额：查询前一增发周期末块高时的激励池账户余额
//            BigInteger incentivePoolAccountBalance = web3j.platonGetBalance(incentivePoolAccountAddr, DefaultBlockParameter.valueOf(BigInteger.valueOf(3399)))
//                    .send().getBalance();
//            logger.error("地址余额：{}",incentivePoolAccountBalance.toString());
//        }catch (Exception e){
//            logger.error("查询出错：{}",e.getMessage());
//        }
//    }
//
//    //@Test
//    public void keystoreParse(){
//        String str = "{\n" +
//                "  \"status\": {\n" +
//                "    \"code\": 0,\n" +
//                "    \"name\": \"OK\"\n" +
//                "  },\n" +
//                "  \"completions\": [\n" +
//                "    {\n" +
//                "      \"total_score\": 2.0711111111111116,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 2\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"4475293306243408fa5958dc63847b4b83930f0c\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"stellar\": {\n" +
//                "          \"val\": \"GAVGTA6XFXE2OE2F7QP7F2Y5PZXYMJC3OEGGN6JNWXCQLL35SDUKFHKT\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max Krohn\",\n" +
//                "          \"score\": 0.010000000000000002\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"maxtaco\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxtaco\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"reddit\": {\n" +
//                "          \"val\": \"maxtaco\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"hackernews\": {\n" +
//                "          \"val\": \"maxtaco\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"mastodon.social\": {\n" +
//                "          \"val\": \"keybase\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"oneshallpass.com\",\n" +
//                "            \"protocol\": \"https:\",\n" +
//                "            \"score\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"val\": \"keybase.io\",\n" +
//                "            \"protocol\": \"https:\",\n" +
//                "            \"score\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"val\": \"oneshallpass.com\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"val\": \"nutflex.com\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"val\": \"maxk.org\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0.011111111111111112\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"dbb165b7879fe7b1174df73bed0b9500\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/95d58167b7831a9042cdbc42931be305_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1.04,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxy\",\n" +
//                "          \"score\": 0.04000000000000001\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"880953fc63dfd6021e265966218c910e5e805dcf\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"19748b5328ee4d960ff62db9c15e0e19\",\n" +
//                "      \"thumbnail\": null,\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1.0285714285714285,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"max707\",\n" +
//                "          \"score\": 0.02857142857142857\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"19bbe7bb89253d6ccde31e7d66c19c19\",\n" +
//                "      \"thumbnail\": null,\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1.025,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maadmax1986\",\n" +
//                "          \"score\": 0.016666666666666666\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        },\n" +
//                "        \"reddit\": {\n" +
//                "          \"val\": \"maadmax1986\",\n" +
//                "          \"score\": 0.008333333333333333\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"f617068e54838f2703d274911e7f0019\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/afa88d606cd1c74646021f4396127f05_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1.019642857142857,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"mschoening\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"a8b99aac2e6b291f743b0c45bc223aecf57a58d9\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max Schoening\",\n" +
//                "          \"score\": 0.007142857142857143\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"mschoening\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"max.wtf\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0.0125\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"ee66c9b52a4e416222395821be051200\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/8fa1a76d28be5f3a9135f4da8eb11805_200_200_square_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1.0142857142857142,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"chunsi\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"42bed2dc6998d24c5899ed68a82b1a17fcb3adf1\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxicc\",\n" +
//                "          \"score\": 0.014285714285714285\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"chun.si\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"efa54d6350a0811a395220288f05b619\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/4e2c68c9ab3009937360f3e44509bb05_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"chologringo\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"e383c3e0e975993c34c81c2d8ae71e19\",\n" +
//                "      \"thumbnail\": null,\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"linnil\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"394ace6feb6e84026aee2eb1465b6519\",\n" +
//                "      \"thumbnail\": null,\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 1,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"legg\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"b664ebaa1667f51bf973a1bde9d2f40b4715a4d7\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"stellar\": {\n" +
//                "          \"val\": \"GDVCJKR6PVONTRHJJTYCA7UCONGTVY7GGJNZE4XDGDUMYII35GAMKMT4\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"max\",\n" +
//                "          \"score\": 1\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"722a387c9fa081da0b98c6f9fc6ea719\",\n" +
//                "      \"thumbnail\": null,\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.08867424242424243,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxa\",\n" +
//                "          \"score\": 0.04000000000000001\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"__maxallan\",\n" +
//                "          \"score\": 0.009090909090909092\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maximilianallan\",\n" +
//                "          \"score\": 0.00625\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"maxallan.me\",\n" +
//                "            \"protocol\": \"http:\",\n" +
//                "            \"score\": 0.008333333333333333\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"10be13383b1a3ef912c59020b3898a19\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/4190e14d485efc286ba89853c552d805_200_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.08214285714285716,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxholman\",\n" +
//                "          \"score\": 0.020000000000000004\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"629a41e26fd0cfa933451d67b194f24cb0424675\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"maxholman\",\n" +
//                "          \"score\": 0.010000000000000002\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxholman\",\n" +
//                "          \"score\": 0.010000000000000002\n" +
//                "        },\n" +
//                "        \"reddit\": {\n" +
//                "          \"val\": \"maxholman\",\n" +
//                "          \"score\": 0.010000000000000002\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"maxholman.com\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0.007142857142857143\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"727422bd2f92a7369be92972e1d4d219\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/492e001f649024fe08c6f9c386989105_200_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.075,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxthat\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"20509d4039b6636f5f6d1a280a6bb749b0cc66a9\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxthat\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"reddit\": {\n" +
//                "          \"val\": \"maxthat\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"2337ed8b2c99dc0fcb5ec4f2fa4e9400\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/ae12adc87cda1fcb5c66686479630b05_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.07291666666666669,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxp\",\n" +
//                "          \"score\": 0.04000000000000001\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"260d9167f8d939133564e5717d964d3361142acf\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max R.D. Parmer\",\n" +
//                "          \"score\": 0.00625\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxrp\",\n" +
//                "          \"score\": 0.016666666666666666\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"trystero.is\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0\n" +
//                "          },\n" +
//                "          {\n" +
//                "            \"val\": \"maxp.info\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0.010000000000000002\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"ccbdd7bec6a4131156446c0a1b3f0a00\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/5bd6597d728c2a892c963b2220064d05_200_200_square_200.png\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.07194805194805196,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"kmax12\",\n" +
//                "          \"score\": 0.02857142857142857\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max Kanter\",\n" +
//                "          \"score\": 0.009090909090909092\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"maxk\",\n" +
//                "          \"score\": 0.020000000000000004\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"kmax12\",\n" +
//                "          \"score\": 0.014285714285714285\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"404ef75b3381935cb8e70b6432afd919\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/31770820d0bd972be2eb27e59d1ec005_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.07023809523809524,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxbee\",\n" +
//                "          \"score\": 0.02857142857142857\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"max-b\",\n" +
//                "          \"score\": 0.016666666666666666\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"11b590e16ccd3e638ca16f7909c2f719\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/a59c1058dc82f472204bb844045f7505_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.06880341880341881,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxh\",\n" +
//                "          \"score\": 0.04000000000000001\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"9c419568fb36ce8cc055954a5de049ecd6da7827\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"stellar\": {\n" +
//                "          \"val\": \"GBWRXZ7NIT3FQ7HDIYXAIJRLZGB6DJTDZTDLGV6DCL76HBEODMR3ZDJY\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max Hodak\",\n" +
//                "          \"score\": 0.010000000000000002\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxhodak\",\n" +
//                "          \"score\": 0.011111111111111112\n" +
//                "        },\n" +
//                "        \"hackernews\": {\n" +
//                "          \"val\": \"frisco\",\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"maxhodak.com\",\n" +
//                "            \"protocol\": \"dns\",\n" +
//                "            \"score\": 0.007692307692307693\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"b04527b622118cef1d1a1696a270dc00\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/ef58fc6bdae489765f2eac53bf306505_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.06785714285714287,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxamg\",\n" +
//                "          \"score\": 0.02857142857142857\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"05982b9f278866841911c61b70384842485024b3\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 2048,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxamg\",\n" +
//                "          \"score\": 0.014285714285714285\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"609b309f9dea31cab6d2c0a65c433619\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/ab76303766fea531ff8066d9ec3e6705_200_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.065,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxt\",\n" +
//                "          \"score\": 0.04000000000000001\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"19c96e912ceae6e5a3527b581d8138a5e76ad746\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"max_tet\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"max-tet\",\n" +
//                "          \"score\": 0.0125\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"53752bc6bd66e500b6df188e45c91200\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/8404e246a63fb56cfc6683ebaf7e7405_200_200_square_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.06060606060606061,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"miramax281\",\n" +
//                "          \"score\": 0.018181818181818184\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Max\",\n" +
//                "          \"score\": 0.025\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"maxpower281\",\n" +
//                "          \"score\": 0.008333333333333333\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"miramax281\",\n" +
//                "          \"score\": 0.009090909090909092\n" +
//                "        }\n" +
//                "      },\n" +
//                "      \"uid\": \"798dce80bfcc845af4082eaa920a7a19\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/beccb24fe924cba07964b0a8e9d73a05_200_200.jpeg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"total_score\": 0.06033882783882784,\n" +
//                "      \"components\": {\n" +
//                "        \"username\": {\n" +
//                "          \"val\": \"maxburkhardt\",\n" +
//                "          \"score\": 0.015384615384615385\n" +
//                "        },\n" +
//                "        \"key_fingerprint\": {\n" +
//                "          \"val\": \"9c9589b0ffb1514c7a12f3bf0e5c97bd980f60c2\",\n" +
//                "          \"algo\": 1,\n" +
//                "          \"nbits\": 4096,\n" +
//                "          \"score\": 0\n" +
//                "        },\n" +
//                "        \"full_name\": {\n" +
//                "          \"val\": \"Maximilian Burkhardt\",\n" +
//                "          \"score\": 0.004761904761904762\n" +
//                "        },\n" +
//                "        \"twitter\": {\n" +
//                "          \"val\": \"maxb\",\n" +
//                "          \"score\": 0.020000000000000004\n" +
//                "        },\n" +
//                "        \"github\": {\n" +
//                "          \"val\": \"maxburkhardt\",\n" +
//                "          \"score\": 0.007692307692307693\n" +
//                "        },\n" +
//                "        \"websites\": [\n" +
//                "          {\n" +
//                "            \"val\": \"maxb.fm\",\n" +
//                "            \"protocol\": \"https:\",\n" +
//                "            \"score\": 0.0125\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      },\n" +
//                "      \"uid\": \"709cfec88e1fc717d83a5144116b5a19\",\n" +
//                "      \"thumbnail\": \"https://s3.amazonaws.com/keybase_processed_uploads/c1306162931ada179b66ed9237806605_200_200.jpg\",\n" +
//                "      \"is_followee\": false\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}\n";
//
//    }
//
//}
