package com.platon.browser.controller;

import com.platon.browser.req.search.SearchReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SearchControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(SearchControllerTest.class);

    @Test
    public void search() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                SearchReq req = new SearchReq();
                req.setCid(chainId);
                req.setParameter("52");
                sendPost("/home/query",req);

                req.setParameter("0x25455");
                sendPost("/home/query",req);

                req.setParameter("0x64f34a510b8aba47af32383499c6f364c963ab9436865dc32aad9ab74a1cab5f");
                sendPost("/home/query",req);

                req.setParameter("0x6ce6f581afc92989fb9e367a66c4a4b5557a9a1e7fe1978fcf020d32254d589d");
                sendPost("/home/query",req);

                req.setParameter("0x1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429");
                sendPost("/home/query",req);

                req.setParameter("dd");
                sendPost("/home/query",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
