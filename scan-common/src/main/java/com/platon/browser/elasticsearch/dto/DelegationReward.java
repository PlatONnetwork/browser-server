package com.platon.browser.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class DelegationReward {

    private Long id;

    private String hash;

    private Long bn;

    private String addr;

    private Date time;

    private Date creTime;

    private Date updTime;

    private String extra;

    private String extraClean;

    @Data
    public static class Extra {

        private String nodeName;

        private String nodeId;

        private String reward;

        /********把字符串类数值转换为大浮点数的便捷方法********/
        public BigDecimal decimalReward() {
            return new BigDecimal(this.getReward());
        }

    }

}