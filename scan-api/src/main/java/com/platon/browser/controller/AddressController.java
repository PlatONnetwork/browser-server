package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.address.QueryDetailRequest;
import com.platon.browser.request.address.QueryRPPlanDetailRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.address.QueryDetailResp;
import com.platon.browser.response.address.QueryRPPlanDetailResp;
import com.platon.browser.service.AddressService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigInteger;

/**
 * 地址具体实现Controller 提供地址详情页面使用
 *
 * @author zhangrj
 * @file AppDocAddressController.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class AddressController {

    @Resource
    private AddressService addressService;

    @Resource
    private I18nUtil i18n;

    /**
     * 查询地址详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.address.QueryDetailResp>>
     * @date 2021/1/29
     */
    @PostMapping("address/details")
    public Mono<BaseResp<QueryDetailResp>> details(@Valid @RequestBody QueryDetailRequest req) {
        return Mono.create(sink -> {
            QueryDetailResp resp = addressService.getDetails(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * 地址锁仓详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.address.QueryRPPlanDetailResp>>
     * @date 2021/5/25
     */
    @PostMapping("address/rpplanDetail")
    public Mono<BaseResp<QueryRPPlanDetailResp>> rpplanDetail(@Valid @RequestBody QueryRPPlanDetailRequest req) {
        return Mono.create(sink -> {
            QueryRPPlanDetailResp resp = addressService.rpplanDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * 查询多个地址的锁仓总额
     * 这个接口目前没有前端调用，准备用于运维统计大户相关地址的锁仓总额。（还未实施，并且即使实施也是临时方案，最终方案是: 在特殊节点实现一个rpc api，输入多地址，返回锁仓总额）
     *
     * @param addresses， 多个地址间，用;分隔
     * @return 锁仓总额（每个地址的锁仓总额=已用于质押和委托的金额+剩余可用于质押和锁仓的金额）
     */
    @PostMapping("address/totalRestrictingAmount")
    public Mono<BigInteger> totalRestrictingAmount(@RequestParam(value="addresses", required=true) String addresses) {
        return Mono.create(sink -> {
            BigInteger totalAmount = addressService.totalRestrictingAmount(addresses);
            sink.success(totalAmount);
        });
    }
}
