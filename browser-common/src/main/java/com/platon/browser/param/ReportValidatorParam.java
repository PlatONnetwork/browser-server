package com.platon.browser.param;

import lombok.Data;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:17
 * txType=3000举报多签(举报验证人)
 */
@Data
public class ReportValidatorParam {

    /**
     * 证据的json值，格式为RPC接口Evidences的返回值
     */
    private List <EvidencesParam> data;

}
