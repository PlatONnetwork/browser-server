package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.req.ticket.TicketListReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class TicketServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceTest.class);

    @Test
    public void getList(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Transaction transaction = getOneVoteTransaction(chainId);
            TicketListReq req = new TicketListReq();
            req.setCid(chainId);
            req.setTxHash(transaction.getHash());
            RespPage<Ticket> result = ticketService.getList(req);
            Assert.assertTrue(result.getData().size()>0);
        });
    }

    @Test
    public void getTicket() throws Exception {
        String res = platon.getTicketContract("1").GetTicketDetail("0xd528d8a52c994b4f017e1e300efaf7a2d2742a49676f93fe2582cb8935e9b5ce").send();
        logger.error("{}",res);
    }

    @Test
    public void getBatchTicket() throws Exception {
        String ticketIds = "0xd528d8a52c994b4f017e1e300efaf7a2d2742a49676f93fe2582cb8935e9b5ce:0x1a50854c2d139485172d05e1431a83867df122211896926759d6cc2f4544009d:0x8a11e494da77d6b97348f8958fe176d65a78ae20ba7f977ca1941bd6701756a6:0x6727b854256ea9a87854c1d2ae26046d51651c0d5b88a11ae33415790db50a5e:0x87f842ae06420f50c5b29514f24c79e2dad671e5cff44a467c83fc20e73c81b8:0xce3b76c6257f6c1d0bfaec5d393b8ad3d4563ef1cd7a99f1e91becfd6d4507ae:0x1b9aac7f041cd1e19df41b6852dd5df1cf2a718b9d2cd806a3362630d1ac772c:0xc24ce50c7390d7cc66eceb51acd701a837269b03ccebe2869c598292fd017ec3:0x4be8dcf77c1db638ea15f1abf1ac51d336c20b70636086ebf0861b60043be16a:0x43e86bec70bbfb453ec764f5a11fbdd3d75b563f4ae864d3ec01eca951cd20be:0xadc508c29748e27e00516bd5d4234088a7136585aa23cfa8a7efc93ce7accf1a:0x08b693f985575f453dbd8656822c62790e3b891b4f4b59af1513c571e8ee9501:0x2b3070fcd810c4d56030ee8e8330838c8df696815ea3c1a1e634572d8fa1c4d1:0x345eeee663385d33ba27c8c0847d03da10afab1fcd9735c6f4ac8d6da8707aba:0x77fe310e4e42ffc1339c17282f0ddc81de82ceca9f0e0ed1f5a949ae6b620ce9:0x0ad05f1fee9a79de0be36c3353d65b5be64727666d617278382376e26705932f:0xac52fa9485ce732353072a32bf07cf5eaa37a5ba1917b6c5b1402f65c9ff3ad1:0xb8535c3255df601ece7f5b206dd2b98200ae60554b9017ab72f36b897e10d9ab:0x9b86d0721d8e088626aee6126afdcacb86b78b8812efaff8e644894966c7b49e:0x0dfd543ef0c0160d53cfb8eb76a9375b3e0bd7b9174269ebc361bad42111caa6:0x4c3d0051695d4db3b1ac562a5925fe9920e9c3cc157ee4602cea811d62386c9c:0xfa62ac7358c10757f5ec5870ea433a65751054c406c5f8a7787aa60b7ed51196:0x81bccaa0276ff3a2a5ff1ebb67547b12f4656622239d367c60755847568299bf:0x185873dfe640a67f0c5a7dbb52a45c3a07b6c0ac6fffed05f0ec1f315e72f4b5:0x3df05d1742007d6d05dee8c3ae0c15b57086d369e7db7d5d6fe7d5e4f7c04967:0x94447008acd0867d37c9bb4822338682f1702bf4462b3df03d641b0f23d7eaed:0x74397b69d4cf1edf4b95202c7710db7c043df7909d51e5e9fa9f50de49ce5191:0x1f818a114b40fe75fcfca7ba512c7ae8446f46c21c65fd0263a4c514b7e03421:0x16ae17cc8d21ca3c8c8b8faa0b0e5743b332a58ff3c09414610578bf9b2a4a42:0x5fc808703d3186cc68dee2b8917c42ea1354922e678604274ca3a816d9cb9571:0x8594a2824386698554e93c5871c9cf677c40";
        String res = platon.getTicketContract("1").GetBatchTicketDetail(ticketIds).send();
        logger.error("{}",res);
    }

    @Test
    public void getIncomeTicket() throws Exception{
        String txHash = "0x3394160ff6a99e3cf2eb00df8f979ae6e9b05e3fcea7ca404e0e9828abde0c75";
        BigDecimal res = ticketService.getTicketIncome(txHash,"1");
        logger.error("{}",res);
    }
}
