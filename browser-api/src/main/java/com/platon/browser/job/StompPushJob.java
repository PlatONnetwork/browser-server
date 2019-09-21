package com.platon.browser.job;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.config.BrowserCache;
import com.platon.browser.config.MessageDto;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;

/**
 * 	推送任务
 *  @file StompPushJob.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Component
public class StompPushJob {

	private static Logger logger = LoggerFactory.getLogger(StompPushJob.class);
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private HomeService homeService;
    @Autowired
    private StakingService stakingService;
    
    /**
     * 	推送统计相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushChainStatisticNew(){
    	ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
    	BaseResp<ChainStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),chainStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/chain/statistic/new", resp);
    }
    
    /**
     * 	推送出块趋势相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushBlockStatisticNew(){
    	BlockStatisticNewResp blockStatisticNewResp = homeService.blockStatisticNew();
    	BaseResp<BlockStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockStatisticNewResp);
		messagingTemplate.convertAndSend("/topic/block/statistic/new", resp);
    }
    
    /**
     *	 推送首页区块相关信息
     */
    @Scheduled(cron="0/3 * * * * ?")
    public void pushBlockListNew() {
    	List<BlockListNewResp> lists = homeService.blockListNew();
		BaseResp<List<BlockListNewResp>> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),lists);
		messagingTemplate.convertAndSend("/topic/block/list/new", resp);
    }
    
    /**
     * 	推送首页验证人相关信息
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
    
    /**
     * 推送验证人列表相关信息
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void pushStakingChangeNew() {
    	for (Entry<String, List<String>> m : BrowserCache.getKeys().entrySet()) {
    		MessageDto messageDto = new MessageDto();
    		messageDto = messageDto.analysisKey(m.getKey());
    		AliveStakingListReq req = new AliveStakingListReq();
    		BeanUtils.copyProperties(messageDto, req);
    		RespPage<AliveStakingListResp> alives = stakingService.aliveStakingList(req);
    		for(String userNo:  m.getValue()) {
    			try {
    				BrowserCache.sendMessage(userNo, JSONObject.toJSONString(alives));
    			}catch (IllegalStateException e) {
    				BrowserCache.getWebSocketSet().remove(userNo);
    				m.getValue().remove(userNo);
    				/**
    				 * 只有没有用户列表时候才需要remove整个key
    				 */
    				if(m.getValue().isEmpty()) {
    					BrowserCache.getKeys().remove(m.getKey());
    				}
					logger.error("连接异常清楚连接",e);
				}
    		}
    	}
    }
}
