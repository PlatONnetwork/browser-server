package com.platon.browser.util.decode;

import com.platon.browser.param.RestrictingCreateParam;
import com.platon.browser.param.TxParam;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;

import java.util.List;

/**
 * @description: 创建验证人交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:13:04
 **/
public class RestrictingCreateDecoder extends Decoder {

    static TxParam decode(RlpList rootList) {
        //创建锁仓计划
        //锁仓释放到账账户
        String account = stringResolver((RlpString) rootList.getValues().get(1));
        // RestrictingPlan 类型的列表（数组）
        List<RestrictingCreateParam.RestrictingPlan> plans = resolvePlan((RlpString) rootList.getValues().get(2));

        RestrictingCreateParam param = RestrictingCreateParam.builder()
                .account(account)
                .plans(plans)
                .build();
        return param;
    }
}
