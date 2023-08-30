package com.platon.browser.service;

import org.apache.commons.lang3.Conversion;
import org.springframework.util.Base64Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SpeedTest {

    public static void main(String[] args){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse("2023-08-28 02:14:24", dtf);
        long startBlockNumber = 1387680;

        LocalDateTime end = LocalDateTime.parse("2023-08-30 06:23:14", dtf);
        long endBlockNumber = 19039962;
        Duration duration = Duration.between(start, end);
        long seconds =   duration.toMillis() / 1000;
        System.out.println("中午");
        System.out.println("speed:=" + ((endBlockNumber  - startBlockNumber)/seconds) + "块/s");

        String base64 = "BA==";

        byte[] str = Base64Utils.decodeFromString(base64);
       long code =  Conversion.byteArrayToLong(str, 0, 0, 0, str.length);
        System.out.println("str: " + code);
    }


}
