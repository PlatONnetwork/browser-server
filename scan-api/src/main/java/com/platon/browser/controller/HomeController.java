package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.home.QueryNavigationRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.home.BlockStatisticNewResp;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.response.home.QueryNavigationResp;
import com.platon.browser.response.home.StakingListNewResp;
import com.platon.browser.service.CommonService;
import com.platon.browser.service.HomeService;
import com.platon.browser.utils.I18nUtil;
import com.platon.utils.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    @Resource
    private CommonService commonService;

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

    @SubscribeMapping("/topic/chain/statistic/new")
    @PostMapping("home/chainStatistic")
    public Mono<BaseResp<ChainStatisticNewResp>> chainStatisticNew() {
        return Mono.create(sink -> {
            ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), chainStatisticNewResp));
        });
    }

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

    /**
     * 获取总发行量(lat)
     *
     * @return: reactor.core.publisher.Mono<java.lang.String>
     * @date: 2021/8/17
     */
    @GetMapping("home/issueValue")
    public Mono<String> getIssueValue() {
        return Mono.create(sink -> {
            BigDecimal issueValue = commonService.getIssueValue();
            issueValue = Convert.fromVon(issueValue, Convert.Unit.KPVON).setScale(8, RoundingMode.DOWN);
            if (issueValue.compareTo(BigDecimal.ZERO) <= 0) {
                sink.error(new Exception("获取总发行量异常"));
            }
            sink.success(issueValue.toPlainString());
        });
    }

    /**
     * 获取流通量(lat)
     *
     * @return: reactor.core.publisher.Mono<java.lang.String>
     * @date: 2021/8/17
     */
    @GetMapping("home/circulationValue")
    public Mono<String> getCirculationValue() {
        return Mono.create(sink -> {
            BigDecimal circulationValue = commonService.getCirculationValue();
            circulationValue = Convert.fromVon(circulationValue, Convert.Unit.KPVON).setScale(8, RoundingMode.DOWN);
            sink.success(circulationValue.toPlainString());
        });
    }

}
