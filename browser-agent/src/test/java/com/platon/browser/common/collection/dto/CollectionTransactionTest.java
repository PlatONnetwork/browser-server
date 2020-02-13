package com.platon.browser.common.collection.dto;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Receipt;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
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

    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, BeanCreateOrUpdateException, IOException {
        CollectionTransaction transaction = CollectionTransaction.newInstance();

        Block block = blockList.get(0);
        CollectionBlock collectionBlock = CollectionBlock.newInstance();

        BeanUtils.copyProperties(block,collectionBlock);

        transaction.setNum(block.getNum());
        transaction.setTo(transactionList.get(0).getTo());
        transaction.setBHash(block.getHash());
        transaction.updateWithBlock(collectionBlock);

//        Transaction transaction1 = new Transaction();
//        transaction.updateWithRawTransaction(transaction1);

        ReceiptResult receipt = receiptResultList.get(0);
        Receipt receipt1 = receipt.getResult().get(0);

        Set<String> generalContractAddressCache = InnerContractAddrEnum.getAddresses();
        transaction.updateWithBlockAndReceipt(collectionBlock,receipt1,client,generalContractAddressCache);

        assertTrue(true);
    }
}
