package com.platon.browser.job;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;
import com.platon.browser.util.I18nUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class StompPushJob {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private HomeService homeService;
    @Autowired
    private StakingService stakingService;
    
    /**
     * 推送统计相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushChainStatisticNew(){
    	ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
    	BaseResp<ChainStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),chainStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/chain/statistic/new", resp);
    }
    
    /**
     * 推送出块趋势相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushBlockStatisticNew(){
    	BlockStatisticNewResp blockStatisticNewResp = homeService.blockStatisticNew();
    	BaseResp<BlockStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/block/statistic/new", resp);
    }
    
    /**
     * 推送首页区块相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushBlockListNew() {
    	List<BlockListNewResp> lists = homeService.blockListNew();
		BaseResp<List<BlockListNewResp>> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),lists);
		messagingTemplate.convertAndSend("/topic/block/list/new", resp);
    }
    
    /**
     * 推送首页验证人相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushStakingListNew() {
    	StakingListNewResp stakingListNewResp = homeService.stakingListNew();
		BaseResp<StakingListNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),stakingListNewResp);
		messagingTemplate.convertAndSend("/topic/staking/list/new", resp);
    }
    
    
    /**
     * 推送验证人汇总相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushStakingStatisticNew() {
    	StakingStatisticNewResp stakingStatisticNewResp = stakingService.stakingStatisticNew();
		BaseResp<StakingStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),stakingStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/staking/statistic/new", resp);
    }
}
