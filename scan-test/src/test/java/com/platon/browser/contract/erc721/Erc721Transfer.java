package com.platon.browser.contract.erc721;

import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

@Slf4j
public class Erc721Transfer extends Erc721Base {
    /**
     * 铸币
     * @throws Exception
     */
    @Test
    public void mint() throws Exception {
        for (String contractAddress : contractAddressList) {
            Erc721Contract contract = loadArc721Contract(contractAddress, adminWallet);
            int count = 0;
            for (String address : walletAddressList) {
                TransactionReceipt transactionReceipt = contract.mint(address, new BigInteger("100000" + count), "http://192.168.9.190:5000/json/100000" + count + ".json").send();
                System.out.println("hash = " + transactionReceipt.getTransactionHash());
                List<ErcContract.ErcTxEvent> eventList = contract.getTxEvents(transactionReceipt);
                eventList.forEach(e ->log.info("\nlog:{}\nfrom:{}\nto:{}\ntokenId:{}",e.getLog(),e.getFrom(),e.getTo(),e.getValue()));
                count++;
            }
        }
    }
//
//    //查询代币余额
//    @Test
//    public void testGetBalance() throws Exception {
//        BigInteger balance = loadD().balanceOf(walletAddress).send();
//        System.out.println(balance);
//    }
//
//    //通过索引id查询每个代币的tokenId
//    @Test
//    public void testGetTokenByIndex() throws Exception {
//        int balance = loadD().balanceOf(walletAddress).send().intValue();
//        System.out.println("token总量: " + balance);
//        for(int i=0; i < balance; i++){
//            String index = String.valueOf(i);
//            BigInteger token = loadD().tokenOfOwnerByIndex(walletAddress,new BigInteger(index)).send();
//            System.out.println("第"+ (i+1) + "个token的tokenId: " + token);
//        }
//    }
//
//    //查询代币元数据
//    @Test
//    public void testGetTokenMetaData() throws Exception {
//        String name = loadD().name().send();
//        String symbol = loadD().symbol().send();
//        String tokenUrl = loadD().getTokenURI(new BigInteger("1000001")).send();
//        System.out.println("name: " + name);
//        System.out.println("symbol: " + symbol);
//        System.out.println("tokenUrl: " + tokenUrl);
//    }
//
//    //获取交易次数
//    @Test
//    public void testGetNonce() throws Exception{
//        BigInteger txCount = web3j.platonGetTransactionCount(walletAddress,
//                DefaultBlockParameterName.PENDING).send().getTransactionCount();
//        if(txCount.intValue() == 0){
//            txCount = web3j.platonGetTransactionCount(walletAddress,
//                    DefaultBlockParameterName.LATEST).send().getTransactionCount();
//        }
//        System.out.println(txCount);
//    }
//
//    @Test
//    public void testTransferFrom() throws Exception {
//        String hash = loadD().transferFrom(walletAddress, toAddress, new BigInteger("1000001")).send().getTransactionHash();
//        System.out.println("交易hash: " + hash);
//    }
//
//    //创建token交易
//    @Test
//    public void testTokenRawTransaction() throws Exception {
//        for(int i =1;i<=1;i++){
//            BigInteger txCount = web3j.platonGetTransactionCount(walletAddress,
//                    DefaultBlockParameterName.PENDING).send().getTransactionCount();
//            if(txCount.intValue() == 0){
//                txCount = web3j.platonGetTransactionCount(walletAddress,
//                        DefaultBlockParameterName.LATEST).send().getTransactionCount();
//            }
//            System.out.println("nonce: " + txCount);
//            //Thread.sleep(1000);
//            /*BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data*/ //原始交易对象的构造函数参数
//            BigInteger nonce = txCount;//交易的编号，一般是从链上获取你这是第几笔交易，不能乱序
//            BigInteger gasPrice = new BigInteger("10000000000");//交易的每个gas的价格，价格越高越容易被执行
//            BigInteger gasLimit = new BigInteger("210000");//gas的数量，太小了不能执行完交易
//            String to = contract;//合约地址
//            BigInteger value = new BigInteger("0");//交易的金额
//            String input = "";
//            //ERC20 合约的方法签名
//            final com.alaya.abi.solidity.datatypes.Function function = new com.alaya.abi.solidity.datatypes.Function(
//                    "transferFrom",
//                    Arrays.<Type>asList(new Address(walletAddress),
//                            new Address(toAddress),
//                            new Uint256(new BigInteger("1000003"))),
//                    Collections.<TypeReference<?>>emptyList());
//            input = FunctionEncoder.encode(function);//对方法进行编码
//            System.out.println("=====================================input: " + input);
//
//            RawTransaction transaction = RawTransaction.createTransaction(nonce,gasPrice,gasLimit,to,value,input);
//            System.out.println("待签名数据：" + this.object2JSON(transaction));
//            //第三个参数是交易的签名人，其实就是发送代币地址的私钥进行签名
//            byte[] signTransaction = TransactionEncoder.signMessage(transaction,chainId,Credentials.create(walletPrivate));//交易签名,需要加上链ID否则会报错{"code":-32000,"message":"invalid sender"}
//            //签完名就将签名数据转换成16进制
//            String hexSignedTransaction = Numeric.toHexString(signTransaction);
//            System.out.println("签名后的16进制数据：" + hexSignedTransaction);
//
//            //记录aton用的transaction{"data":"{"remark":"","signedData":""}","sign":""}
//            addRemarkAndThenSigned(hexSignedTransaction,Credentials.create(walletPrivate));
//
//            /*//将签名后数据发送到链上
//            PlatonSendTransaction response = web3j.platonSendRawTransaction(hexSignedTransaction).send();//发送后的接收对象
//            System.out.println("交易成功后的hash结果:" + response.getTransactionHash());//交易成功后的hash结果
//            System.out.println("交易成功后的response:" + response.getRawResponse());//交易成功后的response
//            System.out.println("交易成功后的result:" + response.getResult());//交易成功后的result*/
//        }
//    }
//
//    //创建{"data":"{"remark":"","signedData":""}","sign":""}中的sign
//    private void addRemarkAndThenSigned(String signData,Credentials sendCredentials){
//        String remark = "";
//        byte[] signedDataByte = Numeric.hexStringToByteArray(signData);
//        byte[] remarkByte = remark.getBytes(StandardCharsets.UTF_8);
//        byte[] message = new byte[signedDataByte.length+remarkByte.length];
//        System.arraycopy(signedDataByte,0,message,0, signedDataByte.length);
//        System.arraycopy(remarkByte,0,message,signedDataByte.length,remarkByte.length);
//
//        byte[] messageHash = Hash.sha3(message);
//
//        //签名 Sign.signMessage(message, ecKeyPair, true) 和  Sign.signMessage(messageHash, ecKeyPair, false) 等效
//        Sign.SignatureData signatureData = Sign.signMessage(messageHash, sendCredentials.getEcKeyPair(),false);
//
//        //加密工具包
//        class SignCodeUtils {
//            public byte[] encode(Sign.SignatureData signatureData){
//                byte[] v = signatureData.getV();
//                byte[] r = signatureData.getR();
//                byte[] s = signatureData.getS();
//
//                // 1 header + 32 bytes for R + 32 bytes for S
//                byte[] sign = new byte[65];
//                System.arraycopy(v,0,sign,0,1);
//                System.arraycopy(r,0,sign,1,32);
//                System.arraycopy(s,0,sign,33,32);
//                return sign;
//            }
//        }
//
//        SignCodeUtils signCodeUtils = new SignCodeUtils();
//        byte[] signByte = signCodeUtils.encode(signatureData);
//
//        //报文中sign数据， signHex等于下面打印的值
//        System.out.println("##################################");
//        System.out.println("sign: " + Numeric.toHexString(signByte));
//        System.out.println("remark: " + remark);
//        System.out.println("signedData: " + signData);
//        System.out.println("##################################");
//    }
//
//    //估算gas
//    @Test
//    public void testEstimatGas() throws Exception {
//        //atp124kpacd6pnexkpx8kt5m4ttvmvskh9nq2rm9uu
//        HttpService httpService = new HttpService(nodeUrl);
//        web3j = Web3j.build(httpService);//创建web3j实例
//        Function function = new Function(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE);
//        //to是合约地址
//        Transaction transaction = Transaction.createEthCallTransaction("from", "to", EncoderUtils.functionEncoder(function));
//        PlatonEstimateGas send = web3j.platonEstimateGas(transaction).send();
//        System.out.println(object2JSON(send));
//    }
//
//    @Test
//    public void testInputFunction(){
//        String toAddress = "atp1qpr3q4rg92vrsu46avxunr3k3nqylj3sn9f2zk";//合约地址
//        String input = "";
//        //ERC721 合约的方法签名 0x23b872dd transferFrom(address,address,uint256)
//        /*final com.alaya.abi.solidity.datatypes.Function function = new com.alaya.abi.solidity.datatypes.Function(
//                "transferFrom",
//                Arrays.<Type>asList(new Address(walletAddress),
//                        new Address(toAddress),
//                        new Uint256(new BigInteger("1000002"))),
//                Collections.<TypeReference<?>>emptyList());*/
//        //ERC20 合约的方法签名 0xa9059cbb transfer(address,uint256)
//        com.alaya.abi.solidity.datatypes.Function function = new com.alaya.abi.solidity.datatypes.Function(
//                "transfer",
//                Arrays.<Type>asList(new Address(walletAddress),
//                        new Uint256(new BigInteger("0"))),
//                Collections.<TypeReference<?>>emptyList());
//        input = FunctionEncoder.encode(function);//对方法进行编码  前10位为方法名字和参数签名：0x23b872dd
//        System.out.println("=====================================input: " + input);
//    }
//
//    @Test
//    public void testDecodeFunction(){
//        //0x23b872dd000000000000000000000000b1db86b82a6ab9295dc3c1ede5f1344942b113fd00000000000000000000000000471054682a983872baeb0dc98e368cc04fca3000000000000000000000000000000000000000000000000000000000000f4242
//        String input = "0x23b872dd000000000000000000000000b1db86b82a6ab9295dc3c1ede5f1344942b113fd00000000000000000000000000471054682a983872baeb0dc98e368cc04fca3000000000000000000000000000000000000000000000000000000000000f4242";
//        List<TypeReference<Type>> outputParameters = new ArrayList<>();
//        //outputParameters.add((TypeReference)new TypeReference<Utf8String>() {});
//        outputParameters.add((TypeReference)new TypeReference<Address>() {});
//        outputParameters.add((TypeReference)new TypeReference<Address>() {});
//        outputParameters.add((TypeReference)new TypeReference<Uint256>() {});
//        List<Type> decodeTypes = FunctionReturnDecoder.decode(input.substring(8), outputParameters, chainId);
//        System.out.println(object2JSON(decodeTypes));
//        Type type = FunctionReturnDecoder.decodeIndexedValue(input, new TypeReference<Utf8String>() {}, chainId);
//        System.out.println(object2JSON(type));
//    }
//
//    //将对象打印出来
//    private String object2JSON(Object o) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.writeValueAsString(o);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    @Test
//    public void testEncodeImage() throws Exception {
//        File file = new File("C:\\Users\\juzix\\Desktop\\4134613.jpg");
//        FileInputStream fis = new FileInputStream(file);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//        byte[] b = new byte[1000];
//        int n;
//        while ((n = fis.read(b)) != -1) {
//            bos.write(b, 0, n);
//        }
//        fis.close();
//        byte[] data = bos.toByteArray();
//        bos.close();
//
//        String s = Base64Utils.encodeToString(data);
//        System.out.println(s);
//    }
}
