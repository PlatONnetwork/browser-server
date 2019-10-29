package com.platon.browser.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SleepUtil {
    public static final void sleep(long seconds){
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (Exception ex) {
            log.error("",ex);
        }
    }
}
