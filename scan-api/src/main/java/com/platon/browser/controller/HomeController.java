package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.home.QueryNavigationRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.home.BlockStatisticNewResp;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.response.home.QueryNavigationResp;
import com.platon.browser.response.home.StakingListNewResp;
import com.platon.browser.service.HomeService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 首页模块Contract。定义使用方法
 *
 * @author zhangrj
 * @file AppDocHomeController.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class HomeController {

    @Resource
    private I18nUtil i18n;

    @Resource
    private HomeService homeService;

    /**
     * 搜索导航
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.home.QueryNavigationResp>>
     * @date 2021/5/25
     */
    @PostMapping("home/queryNavigation")
    public Mono<BaseResp<QueryNavigationResp>> queryNavigation(@Valid @RequestBody QueryNavigationRequest req) {
        return Mono.create(sink -> {
            QueryNavigationResp resp = homeService.queryNavigation(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * 出块趋势
     *
     * @param
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.home.BlockStatisticNewResp>>
     * @date 2021/5/25
     */
    @SubscribeMapping("topic/block/statistic/new")
    @PostMapping("home/blockStatistic")
    public Mono<BaseResp<BlockStatisticNewResp>> blockStatisticNew() {
        return Mono.create(sink -> {
            BlockStatisticNewResp blockStatisticNewResp = homeService.blockStatisticNew();
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), blockStatisticNewResp));
        });
    }

    /**
     * 基础数据
     *
     * @param
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.home.ChainStatisticNewResp>>
     * @date 2021/5/25
     */
    @SubscribeMapping("/topic/chain/statistic/new")
    @PostMapping("home/chainStatistic")
    public Mono<BaseResp<ChainStatisticNewResp>> chainStatisticNew() {
        return Mono.create(sink -> {
            ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), chainStatisticNewResp));
        });
    }

    /**
     * 验证人列表
     *
     * @param
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.home.StakingListNewResp>>
     * @date 2021/5/25
     */
    @SubscribeMapping("topic/staking/list/new")
    @PostMapping("home/stakingList")
    public Mono<BaseResp<StakingListNewResp>> stakingListNew() {
        return Mono.create(sink -> {
            StakingListNewResp stakingListNewResp = homeService.stakingListNew();
            /**
             * 第一次返回都设为true
             */
            stakingListNewResp.setIsRefresh(true);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), stakingListNewResp));
        });
    }

}
