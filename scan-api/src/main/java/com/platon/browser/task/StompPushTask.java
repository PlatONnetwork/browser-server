package com.platon.browser.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platon.browser.config.BrowserCache;
import com.platon.browser.config.MessageDto;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.staking.AliveStakingListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.home.BlockStatisticNewResp;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.response.home.StakingListNewResp;
import com.platon.browser.response.staking.AliveStakingListResp;
import com.platon.browser.response.staking.StakingStatisticNewResp;
import com.platon.browser.service.*;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map.Entry;

/**
 * 推送任务
 *
 * @author zhangrj
 * @file StompPushJob.java
 * @description
 * @data 2019年8月31日
 */
@Component
public class StompPushTask {

    private static Logger logger = LoggerFactory.getLogger(StompPushTask.class);

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private I18nUtil i18n;

    @Resource
    private HomeService homeService;

    @Resource
    private StakingService stakingService;

    @Resource
    private ParameterService parameterService;

    @Resource
    private StatisticCacheService statisticCacheService;

    private boolean checkData() {
        NetworkStat networkStatRedis = this.statisticCacheService.getNetworkStatCache();
        if (networkStatRedis == null || networkStatRedis.getId() == null) {
            return false;
        }
        return true;
    }

    /**
     * 推送统计相关信息
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void pushChainStatisticNew() {
        // 只有程序正常运行才执行任务
        if (!AppStatusUtil.isRunning()) {
            return;
        }
        if (this.checkData()) {
            ChainStatisticNewResp chainStatisticNewResp = this.homeService.chainStatisticNew();
            BaseResp<ChainStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS), chainStatisticNewResp);
            this.messagingTemplate.convertAndSend("/topic/chain/statistic/new", resp);
        }
    }

    /**
     * 推送出块趋势相关信息
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void pushBlockStatisticNew() {
        if (this.checkData()) {
            BlockStatisticNewResp blockStatisticNewResp = this.homeService.blockStatisticNew();
            BaseResp<BlockStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS), blockStatisticNewResp);
            this.messagingTemplate.convertAndSend("/topic/block/statistic/new", resp);
        }
    }

    /**
     * 推送首页验证人相关信息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushStakingListNew() {
        if (this.checkData()) {
            StakingListNewResp stakingListNewResp = this.homeService.stakingListNew();
            BaseResp<StakingListNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS), stakingListNewResp);
            this.messagingTemplate.convertAndSend("/topic/staking/list/new", resp);
        }
    }

    /**
     * 推送验证人汇总相关信息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushStakingStatisticNew() {
        if (this.checkData()) {
            StakingStatisticNewResp stakingStatisticNewResp = this.stakingService.stakingStatisticNew();
            BaseResp<StakingStatisticNewResp> resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS), stakingStatisticNewResp);
            this.messagingTemplate.convertAndSend("/topic/staking/statistic/new", resp);
        }
    }

    /**
     * 推送验证人列表相关信息
     *
     * @throws JsonProcessingException
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushStakingChangeNew() throws JsonProcessingException {
        if (this.checkData()) {
            for (Entry<String, List<String>> m : BrowserCache.getKeys().entrySet()) {
                MessageDto messageDto = new MessageDto();
                messageDto = messageDto.analysisKey(m.getKey());
                AliveStakingListReq req = new AliveStakingListReq();
                BeanUtils.copyProperties(messageDto, req);
                RespPage<AliveStakingListResp> alives = this.stakingService.aliveStakingList(req);
                for (String userNo : m.getValue()) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        BrowserCache.sendMessage(userNo, mapper.writeValueAsString(alives));
                    } catch (Exception e) {
                        BrowserCache.getWebSocketSet().remove(userNo);
                        m.getValue().remove(userNo);
                        /**
                         * 只有没有用户列表时候才需要remove整个key
                         */
                        if (m.getValue().isEmpty()) {
                            BrowserCache.getKeys().remove(m.getKey());
                            break;
                        }
                        logger.error("连接异常清楚连接", e);
                    }
                }
            }
        }
    }

    /**
     * 定时获取配置
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateConfig() {
        if (this.checkData()) {
            this.parameterService.overrideBlockChainConfig();
        }
    }

}
