package com.platon.FilterTest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.platon.browser.dao.entity.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * User: dongqile
 * Date: 2019/1/25
 * Time: 10:32
 */
public class CacheTest {

    protected static Logger logger = LoggerFactory.getLogger(CacheTest.class);

    public void testCacha(){
        String key = "ip";
        Cache <String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
        try {
            cache.get(key, new Callable <String>() {
                @Override
                public String call () throws Exception {
                    return null;
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        String a = "01d033b5b07407e377a3eb268bdc3f07033774fb845b7826a6b741430c5e6b719bda5c4877514e8052fa5dbc2f20fb111a576f6696b6a16ca765de49e11e0541";
        System.out.println(a.replaceFirst("^0*", ""));
    }

}


