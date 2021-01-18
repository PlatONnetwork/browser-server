package com.platon.browser;

import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.http.HttpService;
import com.alaya.tx.gas.ContractGasProvider;
import com.alaya.tx.gas.GasProvider;
import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.utils.NetworkParams;
import com.platon.browser.v0151.contract.Erc721Contract;
import com.platon.browser.v0151.contract.ErcContract;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.util.Date;

public class Erc721ContractTest {
    private static final String PRIVATE_KEY = "4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7";
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2104836);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(100000000000L);
    private static final GasProvider GAS_PROVIDER = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
    private static final String CONTRACT_ADDRESS = "atp1cd44axgdn55y3n2yl65scnszj0kq0rt6ffcw8u";
    private static final Web3j WEB3J = Web3j.build(new HttpService("http://192.168.120.151:6789"));
    private static final ErcContract ERC_CONTRACT = Erc721Contract.load(
            "atp1cd44axgdn55y3n2yl65scnszj0kq0rt6ffcw8u",
            WEB3J,
            Credentials.create(PRIVATE_KEY),
            GAS_PROVIDER, NetworkParams.getChainId());

    public static void main(String[] args) throws Exception {
        BigInteger tokenId = BigInteger.valueOf(1000001);
        String url = ERC_CONTRACT.getTokenURI(tokenId).send();
        OkHttpClient client = new OkHttpClient();
        Request request = new Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String resp = response.body().string();
            TokenInventory tokenInventory = JSON.parseObject(resp,TokenInventory.class);
            Date date = new Date();
            tokenInventory.setCreateTime(date);
            tokenInventory.setUpdateTime(date);
            tokenInventory.setTokenId(tokenId.longValue());
            tokenInventory.setTokenAddress(CONTRACT_ADDRESS);

            System.out.println(resp);
        }
    }

}
