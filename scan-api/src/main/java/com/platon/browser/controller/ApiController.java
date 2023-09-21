package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.api.StakingStatisticsResp;
import com.platon.browser.service.ApiService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 对外api接口
 */
@Slf4j
@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ApiController {

    @Resource
    private I18nUtil i18n;

    @Resource
    private ApiService apiService;

    /**
     * 查询质押统计信息
     */
    @GetMapping("getStakingStatistics")
    public Mono<BaseResp<StakingStatisticsResp>> getStakingStatistics() {
        return Mono.create(sink -> {
            StakingStatisticsResp resp = apiService.getStakingStatistics();
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }
}
