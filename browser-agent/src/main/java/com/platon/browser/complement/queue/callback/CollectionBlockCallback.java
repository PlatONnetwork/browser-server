package com.platon.browser.complement.queue.callback;

import com.platon.browser.queue.callback.ConsumeCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

@Slf4j
@Component
public class CollectionBlockCallback implements ConsumeCallback<PlatonBlock> {
    @Override
    public void call(PlatonBlock param) {
        log.info("block number:{}",param.getBlock().getNumber());
    }
}
