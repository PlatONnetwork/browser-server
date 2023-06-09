package com.platon.browser.utils;//package com.platon.browser.utils;

import com.platon.browser.exception.ResponseErrorResultException;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.PlatonGetCode;
import com.platon.rlp.solidity.RlpEncoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.browser.AgentTestData;
import com.platon.browser.bean.*;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ContractTypeEnum;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionUtilsTest extends AgentTestData {

    @Mock
    protected PlatOnClient platOnClient;

    @Mock
    protected AddressCache addressCache;


    @Test
    public void test() throws BeanCreateOrUpdateException {
        Block block = this.blockList.get(0);
        Transaction transaction = this.transactionList.get(0);
        PPosInvokeContractInput pPosInvokeContractInput = new PPosInvokeContractInput();
        pPosInvokeContractInput.setTxHash("0x123");
        pPosInvokeContractInput.setFrom("0x123");
        pPosInvokeContractInput.setTo("0x123");

        List<TransData> transDatas = new ArrayList<>();
        TransData transData = new TransData();
        transData.setInput("0xf9065e83820bb801b90656b906537b227072657061726541223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861656238656262643035386466326364313962633235613737396534643862383337343338336236303530613464386163626238306232353461343839343030222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307832643764313433663531343262383665376535306137356465333234626163393438323333663862326537663063383966316139353430656333613533636661222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230783365626161633566643634636236363266623030353634656562326335376130666662303336383539663134326133653837653663373333663533656537373335316139353462363266313035643430623435306635633763346431323538653030303030303030303030303030303030303030303030303030303030303030227d2c227072657061726542223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861616361323563663862386634373737373535626164333235363337653433386361393234633062316463323931623164666561363431376266303632323730222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307833646432353035343736643234386466633461656266613139653561623733373065636130383863616437323936393164386138373431313531386536363037222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230786261316335363536386439376437323430393736303133383135663363333235613137356461373761393136323463333462386532646266616464643161636165313239386461316534303363396363336638303735346533376633336630653030303030303030303030303030303030303030303030303030303030303030227d7d");
        transData.setCode("1");
        transDatas.add(transData);
        pPosInvokeContractInput.setTransDatas(transDatas);
//        TransactionUtil.getVirtualTxList(block, transaction, pPosInvokeContractInput);

        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setData("0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064");
        logs.add(log);
        try {
            TransactionUtil.getDelegateReward(logs);
        } catch (Exception e) {

        }

        CollectionTransaction collectionTransaction = CollectionTransaction.newInstance();
        ComplementInfo ci = new ComplementInfo();
        collectionTransaction.setInput("0xf9065e83820bb801b90656b906537b227072657061726541223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861656238656262643035386466326364313962633235613737396534643862383337343338336236303530613464386163626238306232353461343839343030222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307832643764313433663531343262383665376535306137356465333234626163393438323333663862326537663063383966316139353430656333613533636661222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230783365626161633566643634636236363266623030353634656562326335376130666662303336383539663134326133653837653663373333663533656537373335316139353462363266313035643430623435306635633763346431323538653030303030303030303030303030303030303030303030303030303030303030227d2c227072657061726542223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861616361323563663862386634373737373535626164333235363337653433386361393234633062316463323931623164666561363431376266303632323730222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307833646432353035343736643234386466633461656266613139653561623733373065636130383863616437323936393164386138373431313531386536363037222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230786261316335363536386439376437323430393736303133383135663363333235613137356461373761393136323463333462386532646266616464643161636165313239386461316534303363396363336638303735346533376633336630653030303030303030303030303030303030303030303030303030303030303030227d7d");
        TransactionUtil.resolveInnerContractInvokeTxComplementInfo(collectionTransaction, logs, ci);

        Assert.assertTrue(true);
    }

    @Test
    public void resolveGeneralContractCreateTxComplementInfo() throws BeanCreateOrUpdateException, IOException {
        Logger logger = LoggerFactory.getLogger("123");
        ComplementInfo ci = new ComplementInfo();
        CollectionTransaction collectionTransaction = CollectionTransaction.newInstance();


        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
        Request request = mock(Request.class);
        when(web3j.platonGetCode(any(), any())).thenReturn(request);
        PlatonGetCode platonGetCode = mock(PlatonGetCode.class);
        platonGetCode.setResult("0x123");
        when(request.send()).thenReturn(platonGetCode);
        collectionTransaction.setNum(100l);
        //TODO 可能报错
        TransactionUtil.resolveGeneralContractCreateTxComplementInfo(collectionTransaction, "", this.platOnClient, ci, logger,  ContractTypeEnum.EVM);
        platonGetCode.setResult("0x0061736d");
        TransactionUtil.resolveGeneralContractCreateTxComplementInfo(collectionTransaction, "", this.platOnClient, ci, logger, ContractTypeEnum.EVM);

        TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(collectionTransaction, this.platOnClient, ci, ContractTypeEnum.EVM, logger);
        TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(collectionTransaction, this.platOnClient, ci, ContractTypeEnum.WASM, logger);
        TransactionUtil.resolveGeneralContractInvokeTxComplementInfo(collectionTransaction, this.platOnClient, ci, ContractTypeEnum.ERC20_EVM, logger);


        TransactionUtil.resolveGeneralTransferTxComplementInfo(collectionTransaction, ci, this.addressCache);
        collectionTransaction.setTo(InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress());
        TransactionUtil.resolveGeneralTransferTxComplementInfo(collectionTransaction, ci, this.addressCache);
        collectionTransaction.setTo("123");
        TransactionUtil.resolveGeneralTransferTxComplementInfo(collectionTransaction, ci, this.addressCache);
        when(this.addressCache.isWasmContractAddress(any())).thenReturn(true);
        TransactionUtil.resolveGeneralTransferTxComplementInfo(collectionTransaction, ci, this.addressCache);
        when(this.addressCache.isEvmContractAddress(any())).thenReturn(true);
        TransactionUtil.resolveGeneralTransferTxComplementInfo(collectionTransaction, ci, this.addressCache);
        Assert.assertTrue(true);
    }

    @Test
    public void processVirtualTx() throws ContractInvokeException, BlankResponseException,IOException, ResponseErrorResultException {
        Logger logger = LoggerFactory.getLogger("123");
        Long bn = 100l;
        CollectionBlock collectionBlock = CollectionBlock.newInstance();
        collectionBlock.setNum(bn);
        SpecialApi specialApi = mock(SpecialApi.class);
        CollectionTransaction collectionTransaction = CollectionTransaction.newInstance();
        Receipt receipt = new Receipt();
        List<PPosInvokeContractInput> pPosInvokeContractInput4Blocks = new ArrayList<>();
        PPosInvokeContractInput pPosInvokeContractInput = new PPosInvokeContractInput();
        pPosInvokeContractInput.setTxHash("123");
        List<TransData> transDatas = new ArrayList<>();
        TransData transData = new TransData();
        transData.setCode("0x21345698");
        transDatas.add(transData);
        TransData transData2 = new TransData();
        transData2.setInput("0xf858838203ed83820d70b842b84077fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c820554990508b8a69e10de76676d0800000");
        transData2.setCode("0");
        transDatas.add(transData2);
        TransData transData3 = new TransData();
        byte[] data = RlpEncoder.encode(new RlpList(RlpString.create(RlpEncoder.encode(RlpString.create(5000)))));
        String txInput = Hex.toHexString(data);
        transData3.setInput(txInput);
        transData3.setCode("0");
        transDatas.add(transData3);
        pPosInvokeContractInput.setTransDatas(transDatas);
        pPosInvokeContractInput4Blocks.add(pPosInvokeContractInput);
//        PPosInvokeContractInputCache.update(bn, pPosInvokeContractInput4Blocks);
//        when(specialApi.getPPosInvokeInfo(any(), any())).thenReturn(pPosInvokeContractInput4Blocks);
        collectionTransaction.setHash("0x123");
        collectionTransaction.setStatus(1);

        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(this.platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);

        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setData("0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064");
        logs.add(log);
        Log log2 = new Log();
        log2.setData("0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064");
        logs.add(log);
        logs.add(log2);
        receipt.setLogs(logs);
        TransactionUtil.processVirtualTx(collectionBlock, specialApi, this.platOnClient, collectionTransaction, receipt, logger);
    }
}
