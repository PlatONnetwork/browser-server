package com.platon.browser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticItem {
    private Long height;
    private Date time;
    private Long transaction;
}
