package com.platon.browser.service;

import cn.hutool.core.util.ObjectUtil;
import com.platon.AgentApplicationTest;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.ErcTypeEnum;
import com.platon.browser.v0152.contract.Erc1155Contract;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.service.ErcDetectService;
import com.platon.crypto.Credentials;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.math.BigInteger;

@Slf4j
@SpringBootTest(classes = { AgentApplicationTest.class })
@ActiveProfiles("platon")
public class ErcTokenBalanceTest {
    @Resource
    PlatOnClient platOnClient;

    Credentials credentials = Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7");

    /**
     * 根据contractAddress和ercTypeEnum获取对应类型的ErcContract
     *
     * @param contractAddress 合约地址
     * @param ercTypeEnum     合约类型
     * @return com.platon.browser.v0151.contract.ErcContract
     * @date 2021/1/18
     */
    private ErcContract getErcContract(String contractAddress, ErcTypeEnum ercTypeEnum) {
        ErcContract ercContract = null;
        if (ErcTypeEnum.ERC20.equals(ercTypeEnum)) {
            ercContract = Erc20Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), credentials, ErcDetectService.GAS_PROVIDER);
        } else if (ErcTypeEnum.ERC721.equals(ercTypeEnum)) {
            ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), credentials, ErcDetectService.GAS_PROVIDER);
        } else if (ErcTypeEnum.ERC1155.equals(ercTypeEnum)) {
            ercContract = Erc1155Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(), credentials, ErcDetectService.GAS_PROVIDER);
        }
        return ercContract;
    }

    /**
     * 获取地址代币余额, ERC20为金额，ERC721为tokenId数
     *
     * @param tokenAddress 合约地址
     * @param type         合约类型
     * @param account      用户地址
     * @param id           tokenId
     * @return java.math.BigInteger
     * @date 2021/1/20
     */
    public BigInteger getBalance(String tokenAddress, ErcTypeEnum type, String account, BigInteger id) {
        BigInteger balance = BigInteger.ZERO;
        try {
            ErcContract ercContract = getErcContract(tokenAddress, type);
            if (ObjectUtil.isNotNull(ercContract)) {
                balance = ercContract.balanceOf(account, id).send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }


    @SneakyThrows
    @Test
    public void testGetErcTokenBalance() {
        BigInteger balance = this.getBalance("lat1mguk50rlca3xg0m93drj9rx4rhnvaymds2225r", ErcTypeEnum.ERC20,"lat1w6zvpqwaw32rxxufef5h7l0wnr6slsn3d4xxdl", BigInteger.ZERO);

        System.out.println("balance:" + balance.toString());
    }

}
