package com.platon.browser.service;

import com.platon.browser.request.staking.AliveStakingListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.api.StakingStatisticsResp;
import com.platon.browser.response.staking.AliveStakingListResp;
import com.platon.browser.response.staking.StakingStatisticNewResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApiService {

    @Resource
    private StakingService stakingService;
    public StakingStatisticsResp getStakingStatistics() {
        StakingStatisticsResp resp = new StakingStatisticsResp();
        StakingStatisticNewResp stakingStatisticNewResp = stakingService.stakingStatisticNew();
        resp.setStakingDelegationValue(stakingStatisticNewResp.getStakingDelegationValue().toPlainString());
        if(stakingStatisticNewResp.getStakingDenominator().compareTo(BigDecimal.ZERO) != 0){
            resp.setStakingRate(stakingStatisticNewResp.getStakingDelegationValue().divide(stakingStatisticNewResp.getStakingDenominator(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString() + "%");
        } else {
            resp.setStakingRate("0.00%");
        }

        AliveStakingListReq aliveStakingListReq = new AliveStakingListReq();
        aliveStakingListReq.setPageNo(1);
        aliveStakingListReq.setPageSize(300);
        aliveStakingListReq.setQueryStatus("active");
        RespPage<AliveStakingListResp> aliveStakingListPage = stakingService.aliveStakingList(aliveStakingListReq);
        List<AliveStakingListResp> itemList = aliveStakingListPage.getData().stream().filter(item -> !item.getIsInit()).collect(Collectors.toList());
        BigDecimal expectedIncome = BigDecimal.ZERO;
        BigDecimal deleAnnualizedRate   = BigDecimal.ZERO;
        int size = itemList.size();
        resp.setValidatorsNumber(aliveStakingListPage.getData().size());
        for (AliveStakingListResp item: itemList) {
            expectedIncome = expectedIncome.add(new BigDecimal(item.getExpectedIncome()));
            deleAnnualizedRate = deleAnnualizedRate.add(new BigDecimal(item.getDeleAnnualizedRate()));
        }
        if (size > 0){
            resp.setAprForValidators(expectedIncome.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "%");
            resp.setAprForDelegators(deleAnnualizedRate.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "%");
        } else {
            resp.setAprForValidators("0.00%");
            resp.setAprForDelegators("0.00%");
        }
        return resp;
    }
}
