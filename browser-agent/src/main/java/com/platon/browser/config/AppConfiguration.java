package com.platon.browser.config;

import com.platon.browser.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.complement.handler.CollectionEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ICollectionEventHandler collectionEventHandler(){
        return new CollectionEventHandler();
    }

}
