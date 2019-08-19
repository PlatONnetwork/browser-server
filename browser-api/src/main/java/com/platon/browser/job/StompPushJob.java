package com.platon.browser.job;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//@Component
public class StompPushJob {

//    private final Logger logger = LoggerFactory.getLogger(StompPushJob.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private HomeService homeService;

    
    /**
     * 推送统计相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushStatistics(){
    	ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
    	BaseResp<ChainStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),chainStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/chain/statistic/new", resp);
    }


}
