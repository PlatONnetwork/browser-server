package com.platon.browser.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SleepUtil {
    private SleepUtil(){}
    public static void sleep(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception ex) {
            log.error("",ex);
        }
    }
}
