package com.platon.browser.service.erc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alaya.crypto.Credentials;
import com.alaya.tx.gas.ContractGasProvider;
import com.alaya.tx.gas.GasProvider;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.Erc20Contract;
import com.platon.browser.v0152.contract.Erc721Contract;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.browser.v0152.enums.ErcTypeEnum;
import com.platon.browser.v0152.service.ErcDetectService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigInteger;

@Component
public class ErcServiceImpl {

    private static final Log log = LogFactory.get();

    private static final String PRIVATE_KEY = "4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7";

    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2104836);

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(100000000000L);

    private static final GasProvider GAS_PROVIDER = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private ErcDetectService ercDetectService;
    @Resource
    private BlockChainConfig chainConfig;

    /**
     * 获取地址代币余额, ERC20为金额，ERC721为tokenId数
     *
     * @param contractAddress 合约地址
     * @param account         用户地址
     * @return java.math.BigInteger
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    @Valid
    public BigInteger getBalance(String contractAddress, String account) {
        BigInteger balance = BigInteger.ZERO;
        ErcContractId ercContractId = ercDetectService.getContractId(contractAddress);
        ErcContract ercContract = getErcContract(contractAddress, ercContractId.getTypeEnum());
        try {
            if (ObjectUtil.isNotNull(ercContract)) {
                balance = ercContract.balanceOf(account).send();
            }
        } catch (Exception e) {
            log.error(e, "获取地址代币余额异常,contractAddress:{},account:{}", contractAddress, account);
        }
        return balance;
    }

    /**
     * 获取供应总量
     *
     * @param contractAddress 合约地址
     * @return java.math.BigInteger
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    public BigInteger getTotalSupply(String contractAddress) {
        BigInteger totalSupply = null;
        try {
            ErcContractId ercContractId = ercDetectService.getContractId(contractAddress);
            ErcContract ercContract = getErcContract(contractAddress, ercContractId.getTypeEnum());
            if (ObjectUtil.isNotNull(ercContract)) {
                totalSupply = ercContract.totalSupply().send();
            }
        } catch (Exception e) {
            log.error(e, "获取供应总量异常,contractAddress：{}", contractAddress);
        }
        return totalSupply;
    }

    /**
     * 根据contractAddress和ercTypeEnum获取对应类型的ErcContract
     *
     * @param contractAddress 合约地址
     * @param ercTypeEnum     合约类型
     * @return com.platon.browser.v0151.contract.ErcContract
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    private ErcContract getErcContract(String contractAddress, ErcTypeEnum ercTypeEnum) {
        ErcContract ercContract = null;
        if (ErcTypeEnum.ERC20.equals(ercTypeEnum)) {
            ercContract = Erc20Contract.load(contractAddress,platOnClient.getWeb3jWrapper().getWeb3j(),
                    ErcDetectService.CREDENTIALS,
                    ErcDetectService.GAS_PROVIDER,
                    chainConfig.getChainId());
        } else if (ErcTypeEnum.ERC721.equals(ercTypeEnum)) {
            ercContract = Erc721Contract.load(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(),
                    Credentials.create(PRIVATE_KEY),
                    GAS_PROVIDER, chainConfig.getChainId());
        }
        return ercContract;
    }

    /**
     * 获取TokenURI
     *
     * @param contractAddress 合约地址
     * @param tokenId         token id
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/18
     */
    public String getTokenURI(String contractAddress, BigInteger tokenId) {
        String tokenURI = "";
        try {
            ErcContractId ercContractId = ercDetectService.getContractId(contractAddress);
            ErcContract ercContract = getErcContract(contractAddress, ercContractId.getTypeEnum());
            if (ObjectUtil.isNotNull(ercContract)) {
                tokenURI = ercContract.getTokenURI(tokenId).send();
            }
        } catch (Exception e) {
            log.error(e, "getTokenURI异常，token_address：{},token_id:{}", contractAddress, tokenId);
        }
        return tokenURI;
    }

}
