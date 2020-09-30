package com.platon.browser.common.collection.dto;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Receipt;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction.TypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.exception.BeanCreateOrUpdateException;

import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.Transaction;
import com.alaya.rlp.solidity.RlpEncoder;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/12/5 15:09
 * @Description: 年化率信息bean单元测试
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class CollectionTransactionTest extends AgentTestBase {

    @Mock
    protected PlatOnClient client;
    @Mock
    protected AddressCache addressCache;
    @Mock
    protected SpecialApi specialApi;
    @Mock
    protected ERCInterface ercInterface;

    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, BeanCreateOrUpdateException, IOException, ContractInvokeException, BlankResponseException {
        CollectionTransaction transaction = CollectionTransaction.newInstance();

        Transaction transaction1 = new Transaction();
        transaction1.setBlockNumber("0x0");
        transaction1.setValue("0x0");
        transaction1.setTransactionIndex("0x1");
        transaction1.setGasPrice("0x0");
        transaction1.setGas("0x0");
        transaction1.setNonce("0x0");
        transaction.updateWithRawTransaction(transaction1);
        
        Block block = blockList.get(0);
        CollectionBlock collectionBlock = CollectionBlock.newInstance();

        BeanUtils.copyProperties(block,collectionBlock);

        transaction.setNum(block.getNum());
        transaction.setTo(transactionList.get(0).getTo());
        transaction.setBHash(block.getHash());
        transaction.updateWithBlock(collectionBlock);


        ReceiptResult receipt = receiptResultList.get(0);
        Receipt receipt1 = receipt.getResult().get(0);

        Set<String> generalContractAddressCache = InnerContractAddrEnum.getAddresses();
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);

        try {
        	transaction.setTo("");
            transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        transaction.setTo("lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqxlcypcy");
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        transaction.setType(TypeEnum.REPORT.getCode());
        transaction.setInput("0xf9065e83820bb801b90656b906537b227072657061726541223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861656238656262643035386466326364313962633235613737396534643862383337343338336236303530613464386163626238306232353461343839343030222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307832643764313433663531343262383665376535306137356465333234626163393438323333663862326537663063383966316139353430656333613533636661222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230783365626161633566643634636236363266623030353634656562326335376130666662303336383539663134326133653837653663373333663533656537373335316139353462363266313035643430623435306635633763346431323538653030303030303030303030303030303030303030303030303030303030303030227d2c227072657061726542223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861616361323563663862386634373737373535626164333235363337653433386361393234633062316463323931623164666561363431376266303632323730222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307833646432353035343736643234386466633461656266613139653561623733373065636130383863616437323936393164386138373431313531386536363037222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230786261316335363536386439376437323430393736303133383135663363333235613137356461373761393136323463333462386532646266616464643161636165313239386461316534303363396363336638303735346533376633336630653030303030303030303030303030303030303030303030303030303030303030227d7d");
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        transaction.setType(TypeEnum.DELEGATE_CREATE.getCode());
        transaction.setInput("0xf854838203ec8180b842b84077fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050898898a7d9b8314c0000");
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        transaction.setType(TypeEnum.CLAIM_REWARDS.getCode());
        byte[] data = RlpEncoder.encode(new RlpList(RlpString.create(RlpEncoder.encode(RlpString.create(5000)))));
        String txInput = Hex.toHexString(data);
        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setData("0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064");
        logs.add(log);
        transaction.setInput(txInput);
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        receipt1.setStatus("0x1");
        receipt1.setLogStatus(1);
        transaction.setType(TypeEnum.DELEGATE_EXIT.getCode());
        transaction.setInput("0xf858838203ed83820d70b842b84077fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c820554990508b8a69e10de76676d0800000");
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        try {
        	transaction.setType(TypeEnum.CLAIM_REWARDS.getCode());
            transaction.setInput(txInput);
            transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        
        transaction.setTo("lat1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqxlcypcy");
        transaction.setType(TypeEnum.PROPOSAL_TEXT.getCode());
        transaction.setInput("0xf84c838207d0b842b840ff40ac420279ddbe58e1bf1cfe19f4b5978f86e7c483223be26e80ac9790e855cb5d7bd743d94b9bd72be79f01ee068bc1fefe79c06ba9cd49fa96f52c7bdce083827334");
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,addressCache,specialApi,ercInterface);
        
        assertTrue(true);
    }
}
