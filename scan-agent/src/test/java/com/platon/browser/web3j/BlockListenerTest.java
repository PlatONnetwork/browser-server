package com.platon.browser.web3j;

import com.platon.protocol.Web3j;
import com.platon.protocol.websocket.WebSocketClient;
import com.platon.protocol.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BlockListenerTest {

    //private static Web3j web3j;


   /* @BeforeEach
    public void setup() {
        URI uri = URI.create("ws://192.168.10.91:6790");
        WebSocketClient webSocketClient = new WebSocketClient(uri);
        web3j = Web3j.build(new WebSocketService(webSocketClient, true));

        this.subscribeBlock(block -> {
            log.info("Sweet, block number {}  has just been created", block.getBlock().getNumber());
        });
    }*/

    @Test
    public void block() throws ExecutionException, InterruptedException, IOException {
        URI uri = URI.create("ws://192.168.10.91:6790");
        WebSocketClient webSocketClient = new WebSocketClient(uri);
        //Web3j web3j = Web3j.build(new WebSocketService(webSocketClient, true));
        WebSocketService  webSocketService = new WebSocketService("WS://192.168.10.91:6790", true);
        webSocketService.connect();
        Web3j web3j = Web3j.build(webSocketService);

        log.info("block number: {}", web3j.platonBlockNumber().send().getBlockNumber());

        web3j.blockObservable(true).subscribe(block -> {
            log.info("Sweet, block number {} has just been created.", block.getBlock().getNumber());
        }, Throwable::printStackTrace);

        TimeUnit.SECONDS.sleep(5);
    }

}
