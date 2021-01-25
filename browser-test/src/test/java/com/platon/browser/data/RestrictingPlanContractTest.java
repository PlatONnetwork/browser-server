package com.platon.browser.data;

import com.platon.contracts.ppos.RestrictingPlanContract;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.RestrictingPlan;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.resp.RestrictingItem;
import org.junit.Before;
import org.junit.Test;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.http.HttpService;
import com.platon.tx.gas.GasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 锁仓计划接口，包括，
 * 创建锁仓计划
 * 获取锁仓信息
 */
public class RestrictingPlanContractTest {


    private Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

    private String benifitAddress = "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e";

    private RestrictingPlanContract restrictingPlanContract;

    private Credentials credentials;
    
    protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(470000);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);

    @Before
    public void init() {

        credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");

        restrictingPlanContract = RestrictingPlanContract.load(web3j, credentials);
    }

    /**
     * 创建锁仓计划
     * account 锁仓释放到账账户
     * plan plan 为 RestrictingPlan 类型的列表（数组），RestrictingPlan 定义如下：type RestrictingPlan struct {     Epoch uint64    Amount：*big.Int}其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     */
    @Test
    public void createRestrictingPlan() {

        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(1000), new BigInteger("5000000000000000000")));
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(2000), new BigInteger("600000000000000000")));
        try {
            PlatonSendTransaction platonSendTransaction = restrictingPlanContract.createRestrictingPlanReturnTransaction(benifitAddress, restrictingPlans,new GasProvider() {
				
				@Override
				public BigInteger getGasPrice() {
					return GAS_PRICE;
				}
				
				@Override
				public BigInteger getGasLimit() {
					return GAS_LIMIT;
				}
			}).send();
            TransactionResponse baseResponse = restrictingPlanContract.getTransactionResponse(platonSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取锁仓信息。
     * account 锁仓释放到账账户
     */
    @Test
    public void getRestrictingPlanInfo() {
        try {
            CallResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(benifitAddress).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
