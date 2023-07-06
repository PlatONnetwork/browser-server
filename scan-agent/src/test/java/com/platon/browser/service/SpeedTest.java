package com.platon.browser.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SpeedTest {

    public static void main(String[] args){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse("2023-07-05 07:04:00", dtf);
        long startBlockNumber = 18602659;

        LocalDateTime end = LocalDateTime.parse("2023-07-06 01:57:30", dtf);
        long endBlockNumber = 21199533;
        Duration duration = Duration.between(start, end);
        long seconds =   duration.toMillis() / 1000;
        System.out.println("中午");
        System.out.println("speed:=" + ((endBlockNumber  - startBlockNumber)/seconds) + "块/s");
    }
}
