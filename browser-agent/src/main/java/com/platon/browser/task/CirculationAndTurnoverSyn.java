package com.platon.browser.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 10:08
 */
@Component
public class CirculationAndTurnoverSyn {

    @Scheduled(cron = "0/10 * * * * ?")
    protected void syn () {

    }

}
