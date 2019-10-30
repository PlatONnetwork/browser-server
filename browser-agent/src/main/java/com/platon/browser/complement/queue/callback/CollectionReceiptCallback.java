package com.platon.browser.complement.queue.callback;

import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.queue.callback.ConsumeCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CollectionReceiptCallback implements ConsumeCallback<ReceiptResult> {
    @Override
    public void call(ReceiptResult param) {
//        if(param==null){
//            log.error("param为空!");
//            return;
//        }
//        if(param.getResult()!=null&&!param.getResult().isEmpty()){
//            log.info("{}",param.getResult());
//        }
    }
}
